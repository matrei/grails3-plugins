package grailsplugins.services

import grailsplugins.PluginId
import grailsplugins.PluginRegistry
import grailsplugins.http.GithubRepoHandler
import grailsplugins.http.MetadataClient
import grailsplugins.http.MetadataHandler
import grailsplugins.models.GithubRepo
import grailsplugins.models.GrailsPlugin
import grailsplugins.util.SoftwareVersion
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.reactivestreams.Publisher
import org.springframework.context.ApplicationContext

import javax.inject.Singleton

import static grails.async.Promises.task

@Slf4j
@Singleton
@CompileStatic
class PluginService {

    private final PluginRegistry pluginRegistry
    private final GithubService githubService
    private final GithubReadmeService readmeService
    private final TwitterService twitterService
    private final ApplicationContext appContext
    private final MetadataClient metadataClient

    PluginService(
            PluginRegistry pluginRegistry,
            GithubService githubService,
            GithubReadmeService githubReadmeService,
            TwitterService twitterService,
            MetadataClient metadataClient,
            ApplicationContext appContext
    ) {
        this.pluginRegistry = pluginRegistry
        this.githubService = githubService
        this.readmeService = githubReadmeService
        this.twitterService = twitterService
        this.metadataClient = metadataClient
        this.appContext = appContext
    }

    void refreshPluginRegistry() {
        metadataClient.fetchPlugins().subscribe appContext.getBean(MetadataHandler)
    }

    void processPlugin(GrailsPlugin plugin) {
        log.debug 'Saving plugin {}', plugin.name
        pluginRegistry.savePlugin plugin
        task { tweetIfNewVersion plugin }
        task {
            updateGithubRepoForPlugin plugin
            updateGithubReadmeForPlugin plugin
        }
    }

    void tweetIfNewVersion(GrailsPlugin plugin) {
        def previousVersion = pluginRegistry.findPreviousVersionOf plugin
        if (previousVersion && isThereANewVersion(plugin, previousVersion)) {
            twitterService.tweet composeTweetMessage(plugin)
        }
    }

    private static String composeTweetMessage(GrailsPlugin plugin) {
        "$plugin.name $plugin.latestVersion released: https://plugins.grails.org/plugin/$plugin.owner/$plugin.name"
    }

    static boolean isThereANewVersion(GrailsPlugin plugin, String previousVersion) {

        if (!previousVersion) return false

        def latestVersion = plugin.latestVersion
        try {
            def previous = SoftwareVersion.build previousVersion
            def current = SoftwareVersion.build latestVersion
            if (!current || !previous || current.snapshot) return false
            def comparison = current <=> previous
            return comparison > 0
        } catch (NumberFormatException ignore) {
            if (previousVersion && latestVersion && previousVersion != latestVersion) {
                return true
            }
        }
        return false
    }

    void updateGithubReadmeForPlugin(GrailsPlugin plugin) {
        if(plugin.githubSlug) {
            // First try to fetch a markdown readme in the github repo
            def documentText = readmeService.fetchMarkdown plugin.githubSlug
            if (documentText) {
                plugin.readme = readmeService.renderMarkdown(documentText) ?: 'Problem rendering readme.'
            }
            else {
                // Else try to fetch an asciidoc readme in the github repo
                documentText = readmeService.fetchAsciidoc plugin.githubSlug
                if (documentText) {
                    plugin.readme = readmeService.renderAsciidoc(documentText) ?: 'Problem rendering readme'
                }
            }
        }
    }

    void updateGithubRepoForPlugin(GrailsPlugin plugin) {
        Optional<Publisher<GithubRepo>> optionalPublisher = githubService.fetchGithubRepo plugin.vcsUrl
        optionalPublisher.ifPresent(repoPublisher -> {
            repoPublisher.subscribe(new GithubRepoHandler(plugin))
        })
    }

    void backup() { pluginRegistry.backup() }
    void restoreBackup() { pluginRegistry.restoreBackup() }
    void clearStalePlugins() { pluginRegistry.clearStalePlugins() }
    Collection<GrailsPlugin> getAllPlugins() { pluginRegistry.allPlugins }
    Collection<GrailsPlugin> getTopRatedPlugins() { pluginRegistry.topRatedPlugins }
    Collection<GrailsPlugin> getLatestPlugins() { pluginRegistry.latestPlugins }
    int getTotalPluginCount() { pluginRegistry.totalPluginCount }
    GrailsPlugin findPlugin(PluginId pluginId) { pluginRegistry.findPlugin pluginId }
    Collection<GrailsPlugin> findPluginsByQuery(String query) { pluginRegistry.findPluginsByQuery query }
    Collection<GrailsPlugin> findPluginsByOwner(String owner) { pluginRegistry.findPluginsByOwner owner }
    GrailsPlugin findPluginByName(String name) { pluginRegistry.findPluginByName name }
}