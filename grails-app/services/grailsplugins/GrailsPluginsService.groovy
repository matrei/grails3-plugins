package grailsplugins

import com.bintray.BintrayPackage
import com.github.GithubReadmeService
import com.github.GithubRepository
import com.github.GithubService
import com.twitter.TwitterService
import grails.config.Config
import grails.core.support.GrailsConfigurationAware
import groovy.time.TimeCategory
import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.BlockingHttpClient
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.exceptions.HttpClientResponseException

import static grails.async.Promises.task

@Slf4j
@CompileStatic
class GrailsPluginsService implements GrailsConfigurationAware {

    static final String GRAILS_PLUGINS_METADATA_BASE_URL = "https://grails.github.io"
    static final String GRAILS_PLUGINS_METADATA_PATH = "/grails-plugins-metadata/grails-plugins.json"

    GrailsPluginsRepository grailsPluginsRepository
    GithubService githubService
    GithubReadmeService githubReadmeService
    AsciidocRenderService asciidocRenderService
    MarkdownRenderService markdownRenderService
    TwitterService twitterService

    List<String> blacklist

    @Override
    void setConfiguration(Config co) {
        blacklist = co.getProperty('grails.plugins.blacklist', List, [])
    }

    @CompileDynamic
    Date oneDayAgo() {
        Date now = new Date()
        use(TimeCategory) {
            now -= 1.day
        }
        now
    }

    void refresh() {
        grailsPluginsRepository.clearNotUpdatedSince(oneDayAgo())
        refreshFromGithub()
    }

    void refreshFromGithub() {
        final BlockingHttpClient githubClient = HttpClient.create(new URL(GRAILS_PLUGINS_METADATA_BASE_URL)).toBlocking()
        HttpResponse<List<GrailsPlugin>> response
        try {
            response = githubClient.exchange(
                    HttpRequest.GET(GRAILS_PLUGINS_METADATA_PATH), Argument.listOf(GrailsPlugin))
            List<GrailsPlugin> plugins = response.body()
            if (response.status() == HttpStatus.OK && plugins) {
                plugins
                        .stream()
                        .filter({ plugin -> !blacklist.contains(plugin.bintrayPackage.name) })
                        .peek({ plugin -> tweetNewVersion(plugin) })
                        .forEach({ plugin -> process(plugin) })
            }
        } catch (HttpClientResponseException e) {
            log.warn 'Response {}. Could not fetch Grails plugin metadata from Gituhb with error {}', response.status.code, e.message
        }
    }

    private void process(GrailsPlugin plugin) {
        final BintrayKey key = getKey(plugin.bintrayPackage)
        final String oldVcsUrl = grailsPluginsRepository.find(key)?.bintrayPackage?.vcsUrl
        grailsPluginsRepository.save(key, plugin)
        task {
            fetchGithubRepository(key, oldVcsUrl, plugin.bintrayPackage.vcsUrl)
            fetchGithubReadme(key)
        }

    }

    private void tweetNewVersion(GrailsPlugin plugin) {
        final BintrayPackage bintrayPackage = plugin.bintrayPackage
        final BintrayKey key = this.getKey(bintrayPackage)
        String previousVersion = grailsPluginsRepository.findPreviousLatestVersion(key)
        if (previousVersion && isThereANewVersion(bintrayPackage, previousVersion)) {
            tweetAboutNewVersion(bintrayPackage)
        }
    }

    @SuppressWarnings('GrMethodMayBeStatic')
    private BintrayKey getKey(BintrayPackage bintrayPackage) {
        BintrayKey.of(bintrayPackage)
    }

    void tweetAboutNewVersion(BintrayPackage bintrayPackage) {
        twitterService.tweet "${bintrayPackage.name} ${bintrayPackage.latestVersion} released: https://plugins.grails.org/plugin/${bintrayPackage.owner}/${bintrayPackage.name}"
    }

    boolean isThereANewVersion(BintrayPackage bintrayPackage, String previousVersion) {
        if (!previousVersion) {
            return false
        }
        try {
            SoftwareVersion previousSoftwareVersion = SoftwareVersion.build(previousVersion)
            SoftwareVersion softwareVersion = SoftwareVersion.build(bintrayPackage.latestVersion)
            if (!softwareVersion || !previousSoftwareVersion || softwareVersion.isSnapshot()) {
                return false
            }
            return softwareVersion <=> previousSoftwareVersion

        } catch (NumberFormatException e) {

            if (previousVersion && bintrayPackage.latestVersion && previousVersion != bintrayPackage.latestVersion) {
                return true
            }

        }
        return false
    }

    void fetchGithubReadme(BintrayKey key) {
        String vcsUrl = grailsPluginsRepository.find(key)?.bintrayPackage?.vcsUrl
        final String githubSlug = grailsPluginsRepository.find(key)?.bintrayPackage?.githubSlug
        if (vcsUrl && githubSlug) {
            String markdown = githubReadmeService.fetchMarkdown(githubSlug)
            if (markdown) {
                String html = markdownRenderService.renderMarkdown(markdown)
                if (html) {
                    grailsPluginsRepository.updateGithubRepositoryReadme(key, html)
                }
            } else {
                String asciidoc = githubReadmeService.fetchAsciidoc(githubSlug)
                if (asciidoc) {
                    String html = asciidocRenderService.renderAsciidoc(asciidoc)
                    if (html) {
                        grailsPluginsRepository.updateGithubRepositoryReadme(key, html)
                    }
                }
            }
        }
    }

    void fetchGithubRepository(BintrayKey key, String oldVcsUrl, String newVcsUrl) {
        GithubRepository githubRepository = githubService.fetchGithubRepository(newVcsUrl)
        if (githubRepository) {
            grailsPluginsRepository.updateGithubRepository(key, githubRepository)
        }
    }
}
