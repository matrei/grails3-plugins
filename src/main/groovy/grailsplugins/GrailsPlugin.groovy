package grailsplugins

import com.bintray.BintrayPackage
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.github.GithubRepository
import groovy.transform.CompileStatic

@JsonIgnoreProperties(ignoreUnknown = true)
@CompileStatic
class GrailsPlugin {
    BintrayPackage bintrayPackage
    GithubRepository githubRepository
    String mavenMetadataUrl
    String readme
    Date lastUpdated

}
