package com.github

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import groovy.transform.CompileStatic

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
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
