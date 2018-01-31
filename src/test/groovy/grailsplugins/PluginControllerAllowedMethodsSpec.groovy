package grailsplugins

import grails.testing.web.controllers.ControllerUnitTest
import grailsplugins.PluginController
import spock.lang.Specification
import spock.lang.Unroll
import static javax.servlet.http.HttpServletResponse.SC_METHOD_NOT_ALLOWED

class PluginControllerAllowedMethodsSpec extends Specification implements ControllerUnitTest<PluginController> {

    @Unroll
    def "test PluginController.index does not accept #method requests"(String method) {
        when:
        request.method = method
        controller.index()

        then:
        response.status == SC_METHOD_NOT_ALLOWED

        where:
        method << ['PATCH', 'DELETE', 'POST', 'PUT']
    }

    @Unroll
    def "test PluginController.refresh does not accept #method requests"(String method) {
        when:
        request.method = method
        controller.refresh()

        then:
        response.status == SC_METHOD_NOT_ALLOWED

        where:
        method << ['PATCH', 'DELETE', 'GET', 'PUT']
    }

    @Unroll
    def "test PluginController.plugin does not accept #method requests"(String method) {
        when:
        request.method = method
        controller.plugin()

        then:
        response.status == SC_METHOD_NOT_ALLOWED

        where:
        method << ['PATCH', 'DELETE', 'POST', 'PUT']
    }


    @Unroll
    def "test PluginController.pluginWithOwner does not accept #method requests"(String method) {
        when:
        request.method = method
        controller.pluginWithOwner()

        then:
        response.status == SC_METHOD_NOT_ALLOWED

        where:
        method << ['PATCH', 'DELETE', 'POST', 'PUT']
    }

}
