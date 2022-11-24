package grailsplugins

import grails.testing.services.ServiceUnitTest
import grailsplugins.http.GithubRepoHandler
import grailsplugins.models.GithubRepo
import grailsplugins.models.GrailsPlugin
import grailsplugins.models.PackageInfo
import grailsplugins.services.GithubService
import org.reactivestreams.Publisher
import spock.lang.IgnoreIf
import spock.lang.Specification
import spock.lang.Unroll
import spock.util.concurrent.PollingConditions

class GithubServiceSpec extends Specification implements ServiceUnitTest<GithubService> {

    @Unroll
    def "ownerAndRepo for #vcsUrl is #expected"(String vcsUrl, String expected) {

        expect:
        expected == service.ownerSlashRepo(vcsUrl)

        where:
        vcsUrl                                               | expected
        'https://github.com/rvanderwerf/grails-alexa-skills' | 'rvanderwerf/grails-alexa-skills'
        'https://gitlab.com/ysb33r/GradlePluginWorkshop'     | null
        null                                                 | null
    }

    @IgnoreIf({
        !(System.getProperty('GP_GITHUB_TOKEN') || System.getenv('GP_GITHUB_TOKEN')) ||
        !(System.getProperty('GP_GITHUB_USERNAME') || System.getenv('GP_GITHUB_USERNAME'))
    })
    def "fetchGithubRepository returns object"() {
        given:
        PollingConditions conditions = new PollingConditions(timeout: 30)
        String vcsUrl = 'https://github.com/rvanderwerf/grails-alexa-skills'

        when:
        Optional<Publisher<GithubRepo>> optionalRepo = service.fetchGithubRepo vcsUrl

        then:
        optionalRepo.isPresent()

        when:
        def plugin = new GrailsPlugin(packageInfo: new PackageInfo(vcsUrl: vcsUrl))
        optionalRepo.get().subscribe(new GithubRepoHandler(plugin))

        then:
        conditions.eventually {
            assert plugin.githubRepo != null
        }
    }
}
