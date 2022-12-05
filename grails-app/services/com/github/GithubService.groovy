package com.github

import grails.config.Config
import grails.core.support.GrailsConfigurationAware
import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.client.BlockingHttpClient
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.exceptions.HttpClientResponseException

@CompileStatic
@Slf4j
class GithubService implements GrailsConfigurationAware {

    private static final String GITHUB_API_URL = 'https://api.github.com'
    public static final String GITHUB_SLUG_REGEX = /.*github\.com\/([^\/]+\/[^\/\.]+).*/
    String username
    String token
    String userAgent
    BlockingHttpClient client = HttpClient.create(new URL(GITHUB_API_URL)).toBlocking()

    @Override
    void setConfiguration(Config config) {
        username = config.getProperty('github.username', String)
        token = config.getProperty('github.token', String)
        userAgent = config.getProperty('github.userAgent', String, 'Grails Plugins')
    }

    @CompileDynamic
    static String ownerAndRepo(String vcsUrl) {
        def matcher = vcsUrl =~ GITHUB_SLUG_REGEX
        if (matcher.matches()) {
            return matcher[0][1]
        }
        null
    }

    GithubRepository fetchGithubRepository(String vcsUrl) throws IOException {
        String ownerAndRepo = ownerAndRepo(vcsUrl)
        if ( !ownerAndRepo ) {
            return null
        }
        final String url = "/repos/${ownerAndRepo}".toString()
        try {
            HttpResponse<GithubRepository> response = client.exchange(HttpRequest.GET(url).basicAuth(username, token)
                    .header("User-Agent", userAgent), GithubRepository)
            log.trace("fetched {} github repository", ownerAndRepo)
            return response.body()
        } catch(HttpClientResponseException e) {
            log.warn 'Response {}. Could not fetch github repository at {}', e.status.code, vcsUrl
        }
        null
    }
}
