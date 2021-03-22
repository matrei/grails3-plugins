package com.github

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import groovy.transform.CompileStatic

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@CompileStatic
class GithubOwner {
    String login
    Long id
    String avatarUrl
    String gravatarId
    String url
    String htmlUrl
    String followersUrl
    String followingUrl
    String gistsUrl
    String starredUrl
    String subscriptionsUrl
    String organizationsUrl
    String reposUrl
    String eventsUrl
    String receivedEventsUrl
    String type
    Boolean siteAdmin
}
