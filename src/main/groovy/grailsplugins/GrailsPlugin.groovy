package grailsplugins

import com.bintray.BintrayPackage
import com.github.GithubRepository
import groovy.transform.CompileStatic

@CompileStatic
class GrailsPlugin {
    BintrayPackage bintrayPackage
    GithubRepository githubRepository
    String readme
    Date lastUpdated
}
