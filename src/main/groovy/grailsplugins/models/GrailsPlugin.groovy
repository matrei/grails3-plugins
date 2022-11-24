package grailsplugins.models

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.CompileStatic
import io.micronaut.core.annotation.Introspected
import io.micronaut.serde.annotation.Serdeable

import java.time.ZonedDateTime

@CompileStatic
@Introspected
@JsonIgnoreProperties(ignoreUnknown = true)
@Serdeable
class GrailsPlugin {

    @JsonProperty(required = false)
    String displayName

    @JsonProperty(required = false)
    String comment

    @JsonProperty(required = false)
    String deprecated

    @JsonProperty('bintrayPackage')
    PackageInfo packageInfo

    String documentationUrl

    @JsonProperty(required = false)
    String readme // Fallback to this if set and no readme found in repo

    @JsonIgnore
    GithubRepo githubRepo

    @JsonIgnore
    ZonedDateTime lastUpdatedInRegistry

    String getName() { displayName ?: packageInfo.name }
    String getOwner() { packageInfo.owner }
    String getDescription() { packageInfo.desc }
    List<String> getLabels() { packageInfo.labels ?: [] }
    List<String> getLicenses() { packageInfo.licenses ?: [] }
    String getIssueTrackerUrl() { packageInfo.issueTrackerUrl }
    String getLatestVersion() { packageInfo.latestVersion }
    String getUpdated() { packageInfo.updated }
    List<String> getSystemIds() { packageInfo.systemIds ?: [] }
    String getVcsUrl() { packageInfo.vcsUrl }
    String getGithubSlug() { packageInfo.githubSlug }

    String getGroupId() {
        systemIds ? systemIds[0].split(':').first() : null
    }

    String getArtifactId() {
        systemIds ? systemIds[0].split(':').last() : null
    }
}
