package com.github

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.client.BlockingHttpClient
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.exceptions.HttpClientResponseException

@Slf4j
@CompileStatic
class GithubReadmeService {
    private final static String RAWGITHUBUSERCONTENTURL = 'https://raw.githubusercontent.com'
    BlockingHttpClient client = HttpClient.create(new URL(RAWGITHUBUSERCONTENTURL)).toBlocking()

    String fetchMarkdown(String githubSlug) {
        fetchUrl("/${githubSlug}/master/README.md".toString())
    }

    String fetchAsciidoc(String githubSlug) {
        fetchUrl("/${githubSlug}/master/README.adoc".toString())
    }

    private String fetchUrl(String url) {
        try {
            HttpResponse<String> response = client.exchange(HttpRequest.GET(url), String)
            log.debug("fetch README of {}", url)
            return response.body
        } catch (HttpClientResponseException e) {
            log.warn 'Response {}. Could not fetch README {}', e.status.code, url
        }
        null
    }
}