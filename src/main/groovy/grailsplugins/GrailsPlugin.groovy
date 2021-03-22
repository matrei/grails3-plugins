package grailsplugins

import com.bintray.BintrayPackage
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.github.GithubRepository
import groovy.transform.CompileStatic

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@CompileStatic
class GrailsPlugin {
    BintrayPackage bintrayPackage
    GithubRepository githubRepository
    String readme
    Date lastUpdated

}
