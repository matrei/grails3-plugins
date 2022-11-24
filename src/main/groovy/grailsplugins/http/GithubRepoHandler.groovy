package grailsplugins.http

import grailsplugins.models.GithubRepo
import grailsplugins.models.GrailsPlugin
import groovy.transform.CompileStatic
import io.micronaut.http.client.exceptions.HttpClientResponseException

@CompileStatic
class GithubRepoHandler extends Handler<GithubRepo> {

    private final GrailsPlugin plugin

    GithubRepoHandler(GrailsPlugin plugin) { this.plugin = plugin }

    @Override
    void handle(GithubRepo repo) { plugin.githubRepo = repo }

    @Override
    String createHttpClientResponseExceptionMessage(HttpClientResponseException e) {
        "Response $e.status.code. Could not fetch github repository at $plugin.vcsUrl"
    }
}
