package grailsplugins

import com.bintray.BintrayPackage
import com.bintray.BintrayService
import com.github.GithubReadmeService
import com.github.GithubService
import grails.testing.services.ServiceUnitTest
import groovy.time.TimeCategory
import spock.lang.IgnoreIf
import spock.lang.Specification
import spock.lang.Unroll
import spock.util.concurrent.PollingConditions

class GrailsPluginsServiceSpec extends Specification implements ServiceUnitTest<GrailsPluginsService> {

    Closure doWithSpring() {{ ->
        grailsPluginsRepository(GrailsPluginsRepositoryService)
        bintrayService(BintrayService)
        githubService(GithubService)
        githubReadmeService(GithubReadmeService)
        asciidocRenderService(AsciidocRenderService)
        markdownRenderService(MarkdownRenderService)
    }}

    def "test oneDayAgoReturns a date older than 23 hours"() {
        given:
        Date d = new Date()
        use(TimeCategory) {
            d -= 23.hour
        }

        expect:
        expect: service.oneDayAgo().before(d)
    }

    @IgnoreIf({
        !System.getProperty('GP_BINTRAY_TOKEN') ||
        !System.getProperty('GP_BINTRAY_USERNAME') ||
        !System.getProperty('GP_GITHUB_TOKEN') ||
        !System.getProperty('GP_GITHUB_USERNAME')
    })
    def "test fetchBintrayPackages"() {
        given:
        service.bintrayService.token = System.getProperty('GP_BINTRAY_TOKEN')
        service.bintrayService.username = System.getProperty('GP_BINTRAY_USERNAME')

        service.githubService.username = System.getProperty('GP_GITHUB_USERNAME')
        service.githubService.token = System.getProperty('GP_GITHUB_TOKEN')

        when:
        service.refresh()

        then:
        new PollingConditions(timeout: 10).eventually {
            assert service.grailsPluginsRepository.count() >= 232
        }
    }

    @Unroll
    def "#version #description #previousVersion"(String version, String previousVersion, boolean expected, String description) {
        expect:
        expected == service.isThereANewVersion(new BintrayPackage(latest_version: version), previousVersion)

        where:
        version | previousVersion | expected
        '1.0.0' | '1.0.0.RC2'     | true
        '1.0.0' | '0.0.9'         | true
        '1.0.1' | '1.0.1'         | false
        '1.0.2' | '1.0.1'         | true
        description = expected ? 'is newer than' : 'is older than'
    }
}
