package grailsplugins.http

import grailsplugins.config.GithubApiClientConfig
import grailsplugins.models.GithubRepo
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Header
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.client.annotation.Client
import org.reactivestreams.Publisher

import static io.micronaut.http.HttpHeaders.ACCEPT
import static io.micronaut.http.HttpHeaders.USER_AGENT

@Client(value = '${grailsplugins.client.github-api.url}', configuration = GithubApiClientConfig)
@Header(name = ACCEPT, value = '${grailsplugins.client.github-api.accept:application/vnd.github.v3+json, application/json}')
@Header(name = USER_AGENT, value = '${grailsplugins.client.github-api.user-agent:Grails Plugin Portal}')
interface GithubApiClient {

    @Get('/repos/{owner}/{repo}')
    Publisher<GithubRepo> getRepo(@PathVariable String owner, @PathVariable String repo)

}
