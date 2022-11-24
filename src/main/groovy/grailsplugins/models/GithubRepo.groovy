package grailsplugins.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.CompileStatic
import io.micronaut.core.annotation.Introspected
import io.micronaut.serde.annotation.Serdeable

@CompileStatic
@Introspected
@JsonIgnoreProperties(ignoreUnknown = true)
@Serdeable
class GithubRepo {
    String name
    GithubOwner owner
    @JsonProperty('html_url')
    String htmlUrl
    @JsonProperty('stargazers_count')
    Integer stargazersCount
}
