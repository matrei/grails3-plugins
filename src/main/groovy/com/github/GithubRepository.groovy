package com.github

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import groovy.transform.CompileStatic

@JsonIgnoreProperties(ignoreUnknown = true)
@CompileStatic
class GithubRepository {
    Long id
    String name
    GithubOwner owner
    String htmlUrl
    Integer stargazersCount
    Boolean hasPages
}
