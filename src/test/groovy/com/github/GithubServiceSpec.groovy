package com.github

import grails.testing.services.ServiceUnitTest
import spock.lang.IgnoreIf
import spock.lang.Specification
import spock.lang.Unroll
import spock.util.concurrent.PollingConditions

class GithubServiceSpec extends Specification implements ServiceUnitTest<GithubService> {

    @Unroll
    def "ownerAndRepo for #vcsUrl is #expected"(String vcsUrl, String expected) {

        expect:
        expected == service.ownerAndRepo(vcsUrl)

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
        PollingConditions conditions = new PollingConditions(timeout: 10)

        service.username = System.getProperty('GP_GITHUB_USERNAME')?: System.getenv('GP_GITHUB_USERNAME')
        service.token = System.getProperty('GP_GITHUB_TOKEN')?: System.getenv('GP_GITHUB_TOKEN')

        when:
        GithubRepository repository = service.fetchGithubRepository('https://github.com/rvanderwerf/grails-alexa-skills')

        then:
        conditions.eventually {
            assert repository != null
        }
    }
}
