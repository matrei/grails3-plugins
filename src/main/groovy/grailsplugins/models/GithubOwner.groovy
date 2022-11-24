package grailsplugins.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import groovy.transform.CompileStatic
import io.micronaut.core.annotation.Introspected
import io.micronaut.serde.annotation.Serdeable

@CompileStatic
@Introspected
@JsonIgnoreProperties(ignoreUnknown = true)
@Serdeable
class GithubOwner {
    String login
}
