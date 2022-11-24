package grailsplugins.services

import grailsplugins.http.GithubApiClient
import grailsplugins.models.GithubRepo
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import jakarta.inject.Singleton
import org.reactivestreams.Publisher

@Slf4j
@Singleton
@CompileStatic
class GithubService {

    private static final String GITHUB_SLUG_REGEX = /.*github\.com\/([^\/]+\/[^\/\.]+).*/

    private final GithubApiClient client

    GithubService(GithubApiClient client) {
        this.client = client
    }

    static String ownerSlashRepo(String vcsUrl) {
        def matcher = vcsUrl =~ GITHUB_SLUG_REGEX
        matcher.matches() ? matcher.group(1) : null
    }

    Optional<Publisher<GithubRepo>> fetchGithubRepo(String vcsUrl) {
        def ownerSlashRepo = ownerSlashRepo vcsUrl
        def ownerSlashRepoSplit = ownerSlashRepo ? ownerSlashRepo.split('/') : null
        Optional.ofNullable(ownerSlashRepoSplit ? client.getRepo(ownerSlashRepoSplit[0], ownerSlashRepoSplit[1]) : null)
    }
}
