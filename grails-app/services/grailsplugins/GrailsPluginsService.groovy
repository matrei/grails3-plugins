package grailsplugins

import com.bintray.BintrayPackage
import com.bintray.BintrayPackageResponse
import com.bintray.BintrayPackageSimple
import com.bintray.BintrayService
import com.github.GithubReadmeService
import com.github.GithubRepository
import com.github.GithubService
import com.twitter.TwitterService
import grails.config.Config
import grails.core.support.GrailsConfigurationAware
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.grails.model.GrailsVersion

import static grails.async.Promises.task

@Slf4j
@CompileStatic
class GrailsPluginsService implements GrailsConfigurationAware {

    GrailsPluginsRepository grailsPluginsRepository
    BintrayService bintrayService
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


    void refresh() {
        grailsPluginsRepository.clear()
        Integer startAt = 0
        BintrayPackageResponse rsp = bintrayService.fetchBintrayPackagesByStartPosition(startAt)
        fetch(rsp)
        List<Integer> positions = bintrayService.expectedExtraStarPositions(rsp.total, rsp.start, rsp.end)
        positions.each { Integer start ->
            task {
                bintrayService.fetchBintrayPackagesByStartPosition(start)
            }.onComplete { BintrayPackageResponse bintrayPackageResponse ->
                fetch(bintrayPackageResponse)
            }
        }
    }

    void fetch(BintrayPackageResponse rsp) {
        for (BintrayPackageSimple bintrayPackageSimple : rsp.bintrayPackageList ) {
            fetch(bintrayPackageSimple)
        }
    }

    void fetch(BintrayPackageSimple bintrayPackageSimple) {
        if ( !blacklist.contains(bintrayPackageSimple.name) ) {
            task {
                bintrayService.fetchBintrayPackage(bintrayPackageSimple.name)
            }.onComplete { BintrayPackage bintrayPackage ->
                if ( bintrayPackage ) {
                    BintrayKey key = BintrayKey.of(bintrayPackage)
                    String previousVersion = grailsPluginsRepository.findPreviousLatestVersion(key)

                    if ( previousVersion &&
                         !GrailsVersion.build(bintrayPackage.latestVersion).isSnapshot() &&
                         isThereANewVersion(bintrayPackage, previousVersion) ) {
                        tweetAboutNewVersion(bintrayPackage)
                    }
                    key = grailsPluginsRepository.save(bintrayPackage)
                    fetchGithubRepository(key)
                    fetchGithubReadme(key)
                }
            }
        }
    }

    void tweetAboutNewVersion(BintrayPackage bintrayPackage) {
        twitterService.tweet "${bintrayPackage.name} ${bintrayPackage.latestVersion} released: http://plugins.grails.org/plugin/${bintrayPackage.owner}/${bintrayPackage.name}"
    }

    boolean isThereANewVersion(BintrayPackage bintrayPackage, String previousVersion) {
        if ( previousVersion == null ) {
            return false
        }
        GrailsVersion previousSoftwareVersion = GrailsVersion.build(previousVersion)
        GrailsVersion softwareVersion = GrailsVersion.build(bintrayPackage.latestVersion)
        softwareVersion.compareTo(previousSoftwareVersion) as boolean
    }

    void fetchGithubReadme(BintrayKey key) {
        String vcsUrl = grailsPluginsRepository.find(key)?.bintrayPackage?.vcsUrl
        if ( vcsUrl ) {
            task {
                githubReadmeService.fetchMarkdown(vcsUrl)
            }.onComplete { String markdown ->
                if ( markdown ) {
                    task {
                        markdownRenderService.renderMarkdown(markdown)
                    }.onComplete { String html ->
                        if ( html ) {
                            grailsPluginsRepository.updateGithubRepositoryReadme(key, html)
                        }
                    }
                } else {
                    task {
                        githubReadmeService.fetchAsciidoc(vcsUrl)
                    }.onComplete { String asciidoc ->
                        if ( asciidoc ) {
                            task {
                                asciidocRenderService.renderAsciidoc(asciidoc)
                            }.onComplete { String html ->
                                if ( html ) {
                                    grailsPluginsRepository.updateGithubRepositoryReadme(key, html)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    void fetchGithubRepository(BintrayKey key) {
        String vcsUrl = grailsPluginsRepository.find(key)?.bintrayPackage?.vcsUrl
        if ( vcsUrl ) {
            task {
                githubService.fetchGithubRepository(vcsUrl)
            }.onComplete { GithubRepository githubRepository ->
                if ( githubRepository ) {
                    grailsPluginsRepository.updateGithubRepository(key, githubRepository)
                }
            }
        }
    }

}