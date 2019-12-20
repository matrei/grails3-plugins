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
import groovy.time.TimeCategory
import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

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
        Integer startAt = 0
        BintrayPackageResponse rsp = bintrayService.fetchBintrayPackagesByStartPosition(startAt)
        if ( rsp != null ) {
            log.trace 'BintrayPackageResponse start: {} end: {} total: {}', rsp.start, rsp.end, rsp.total
            fetch(rsp)
            List<Integer> positions = bintrayService.expectedExtraStarPositions(rsp.total, rsp.start, rsp.end)
            log.trace("positions {}", positions)
            positions.each { Integer start ->
                task {
                    log.trace("fetching bintray packages with start {}", start)
                    bintrayService.fetchBintrayPackagesByStartPosition(start)
                }.onComplete { BintrayPackageResponse bintrayPackageResponse ->
                    log.trace 'BintrayPackageResponse start: {} end: {} total: {}', bintrayPackageResponse.start, bintrayPackageResponse.end, bintrayPackageResponse.total
                    if ( bintrayPackageResponse != null ) {
                        fetch(bintrayPackageResponse)
                    } else {
                        log.warn("no bintray package response for {}", start)
                    }
                }
            }
        }
    }

    void fetch(BintrayPackageResponse rsp) {
        for (BintrayPackageSimple bintrayPackageSimple : rsp.bintrayPackageList ) {
            log.debug("fetching bintray package {}", bintrayPackageSimple.toString())
            fetch(bintrayPackageSimple)
        }
    }

    void fetch(BintrayPackageSimple bintrayPackageSimple) {
        if ( !blacklist.contains(bintrayPackageSimple.name) ) {
            task {
                log.trace("fetch bintray package {}", bintrayPackageSimple.name)
                bintrayService.fetchBintrayPackage(bintrayPackageSimple.name)
            }.onComplete { BintrayPackage bintrayPackage ->
                if ( bintrayPackage ) {
                    BintrayKey key = BintrayKey.of(bintrayPackage)
                    String previousVersion = grailsPluginsRepository.findPreviousLatestVersion(key)

                    if ( previousVersion &&
                         isThereANewVersion(bintrayPackage, previousVersion) ) {
                        tweetAboutNewVersion(bintrayPackage)
                    }
                    log.trace("saving {}", bintrayPackage.name)
                    key = grailsPluginsRepository.save(bintrayPackage)
                    fetchGithubRepository(key)
                    fetchGithubReadme(key)
                } else {
                    log.warn("could not fetch bintray package {}", bintrayPackageSimple.name)
                }
            }
        }
    }

    void tweetAboutNewVersion(BintrayPackage bintrayPackage) {
        twitterService.tweet "${bintrayPackage.name} ${bintrayPackage.latestVersion} released: http://plugins.grails.org/plugin/${bintrayPackage.owner}/${bintrayPackage.name}"
    }

    boolean isThereANewVersion(BintrayPackage bintrayPackage, String previousVersion) {
        if ( !previousVersion ) {
            return false
        }
        try {
            SoftwareVersion previousSoftwareVersion = SoftwareVersion.build(previousVersion)
            SoftwareVersion softwareVersion = SoftwareVersion.build(bintrayPackage.latestVersion)
            if ( !softwareVersion || !previousSoftwareVersion || softwareVersion.isSnapshot() ) {
                return false
            }
            return softwareVersion.compareTo(previousSoftwareVersion) as boolean

        } catch(NumberFormatException e) {

            if ( previousVersion && bintrayPackage.latestVersion && previousVersion != bintrayPackage.latestVersion ) {
                return true
            }

        }
        false
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