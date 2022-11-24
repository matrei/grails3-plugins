package grailsplugins.http

import grailsplugins.config.MetadataClientConfig
import grailsplugins.models.GrailsPlugin
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Header
import io.micronaut.http.client.annotation.Client
import io.micronaut.retry.annotation.Retryable
import org.reactivestreams.Publisher

import static io.micronaut.http.HttpHeaders.ACCEPT
import static io.micronaut.http.HttpHeaders.USER_AGENT

@Client(value = '${grailsplugins.client.metadata.url}', path = '${grailsplugins.client.metadata.path}', configuration = MetadataClientConfig)
@Retryable(attempts = '${grailsplugins.client.metadata.retry.attempts:3}', delay = '${grailsplugins.client.metadata.retry.delay:1s}')
@Header(name = ACCEPT, value = '${grailsplugins.client.metadata.accept:application/vnd.github.v3+json, application/json}')
@Header(name = USER_AGENT, value = '${grailsplugins.client.metadata.user-agent:Grails Plugin Portal}')
interface MetadataClient {
    @Get
    Publisher<List<GrailsPlugin>> fetchPlugins()
}