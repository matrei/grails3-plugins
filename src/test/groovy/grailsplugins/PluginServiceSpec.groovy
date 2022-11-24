package grailsplugins

import grails.testing.services.ServiceUnitTest
import grailsplugins.models.GrailsPlugin
import grailsplugins.models.PackageInfo
import grailsplugins.services.PluginService
import spock.lang.IgnoreIf
import spock.lang.Specification
import spock.lang.Unroll
import spock.util.concurrent.PollingConditions

class PluginServiceSpec extends Specification implements ServiceUnitTest<PluginService> {

    @IgnoreIf({
        !(System.getProperty('GP_GITHUB_TOKEN') || System.getenv('GP_GITHUB_TOKEN')) ||
        !(System.getProperty('GP_GITHUB_USERNAME') || System.getenv('GP_GITHUB_USERNAME'))
    })
    def "test refreshPlugins"() {
        when:
        service.refreshPluginRegistry()

        then:
        new PollingConditions(timeout: 120).eventually {
            assert service.totalPluginCount >= 232
        }
    }

    @Unroll
    def "#version #description #previousVersion"(String version, String previousVersion, boolean expected, String description) {
        given:
        def plugin = new GrailsPlugin(packageInfo: new PackageInfo(latestVersion: version))

        expect:
        def newVersionExists = service.isThereANewVersion(plugin, previousVersion)
        expected == newVersionExists

        where:
        version | previousVersion | expected
        '1.0.0' | '1.0.0.RC2'     | true
        '1.0.0' | '0.0.9'         | true
        '1.0.1' | '1.0.1'         | false
        '1.0.2' | '1.0.1'         | true
        '1.0.1' | '1.0.2'         | false
        description = expected ? 'is newer than' : 'is not newer than'
    }
}
