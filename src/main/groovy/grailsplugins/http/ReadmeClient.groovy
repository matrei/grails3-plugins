package grailsplugins.http


import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Header
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.client.annotation.Client

import static io.micronaut.http.HttpHeaders.USER_AGENT

@Client('${grailsplugins.client.readme.url}')
@Header(name = USER_AGENT, value = '${grailsplugins.client.readme.user-agent:Grails Plugin Portal}')
interface ReadmeClient {
    @Get('{+path}')
    String fetchReadme(@PathVariable String path)
}