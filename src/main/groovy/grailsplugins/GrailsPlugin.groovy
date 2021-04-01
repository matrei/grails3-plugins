package grailsplugins

import com.bintray.BintrayPackage
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.github.GithubRepository
import groovy.transform.CompileStatic
import io.micronaut.core.annotation.Introspected

@Introspected
@JsonIgnoreProperties(ignoreUnknown = true, value = ["version"])
@CompileStatic
class GrailsPlugin {
    BintrayPackage bintrayPackage

    @JsonIgnore
    GithubRepository githubRepository

    String documentationUrl
    String mavenMetadataUrl

    @JsonProperty(required = false)
    String readme

    @JsonIgnore
    Date lastUpdated

}
