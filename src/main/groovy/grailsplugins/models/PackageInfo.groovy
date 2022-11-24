package grailsplugins.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import grailsplugins.services.GithubService
import groovy.transform.CompileStatic
import io.micronaut.core.annotation.Introspected
import io.micronaut.serde.annotation.Serdeable

@CompileStatic
@Introspected
@JsonIgnoreProperties(ignoreUnknown = true)
@Serdeable
class PackageInfo {

    String name
    String repo
    String owner
    String desc
    List<String> labels
    List<String> licenses
    String issueTrackerUrl
    String latestVersion
    String updated
    List<String> systemIds
    String vcsUrl

    String getGithubSlug() {
        GithubService.ownerSlashRepo vcsUrl
    }
}
