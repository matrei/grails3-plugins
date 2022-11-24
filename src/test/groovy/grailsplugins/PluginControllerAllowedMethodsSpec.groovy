package grailsplugins

import grails.testing.mixin.integration.Integration
import grails.testing.spock.OnceBefore
import io.micronaut.http.HttpMethod
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.exceptions.HttpClientResponseException
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

@Integration(applicationClass=Application)
class PluginControllerAllowedMethodsSpec extends Specification {

    @Shared
    @AutoCleanup
    HttpClient client

    @OnceBefore
    void initClient() { client = HttpClient.create("http://localhost:$serverPort".toURL()) }

    @Unroll
    def "test PluginController.index does not accept #method requests"(String method) {
        when:
        client.toBlocking().exchange HttpRequest.create(HttpMethod.parse(method), '')

        then:
        HttpClientResponseException e = thrown()
        e.status == HttpStatus.METHOD_NOT_ALLOWED

        where:
        method << ['PATCH', 'DELETE', 'POST', 'PUT']
    }

    @Unroll
    def "test PluginController.refresh does not accept #method requests"(String method) {
        when:
        client.toBlocking().exchange HttpRequest.create(HttpMethod.parse(method), '/plugin/refresh')

        then:
        HttpClientResponseException e = thrown()
        e.status == HttpStatus.METHOD_NOT_ALLOWED

        where:
        method << ['PATCH', 'DELETE', 'GET', 'PUT']
    }

    @Unroll
    def "test PluginController.plugin does not accept #method requests"(String method) {
        when:
        client.toBlocking().exchange HttpRequest.create(HttpMethod.parse(method), '/plugin/grails-alexa-skills')

        then:
        HttpClientResponseException e = thrown()
        e.status == HttpStatus.METHOD_NOT_ALLOWED

        where:
        method << ['PATCH', 'DELETE', 'POST', 'PUT']
    }


    @Unroll
    def "test PluginController.pluginWithOwner does not accept #method requests"(String method) {
        when:
        client.toBlocking().exchange HttpRequest.create(HttpMethod.parse(method), '/plugin/rvanderwerf/grails-alexa-skills')

        then:
        HttpClientResponseException e = thrown()
        e.status == HttpStatus.METHOD_NOT_ALLOWED

        where:
        method << ['PATCH', 'DELETE', 'POST', 'PUT']
    }

}
