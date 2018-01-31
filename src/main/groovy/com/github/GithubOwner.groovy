package com.github

import groovy.transform.CompileStatic

@CompileStatic
class GithubOwner {
    String login
    Long id
    String avatar_url
    String gravatar_id
    String url
    String html_url
    String followers_url
    String following_url
    String gists_url
    String starred_url
    String subscriptions_url
    String organizations_url
    String repos_url
    String events_url
    String received_events_url
    String type
    Boolean site_admin

    String getAvatarUrl() {
        return avatar_url
    }

    String getGravatarId() {
        return gravatar_id
    }

    String getHtmlUrl() {
        return html_url
    }

    String getFollowersUrl() {
        return followers_url
    }

    String getFollowingUrl() {
        return following_url
    }

    String getGistsUrl() {
        return gists_url
    }

    String getStarredUrl() {
        return starred_url
    }

    String getSubscriptionsUrl() {
        return subscriptions_url
    }

    String getOrganizationsUrl() {
        return organizations_url
    }

    String getReposUrl() {
        return repos_url
    }

    String getEventsUrl() {
        return events_url
    }

    String getReceivedEventsUrl() {
        return received_events_url
    }

    Boolean getSiteAdmin() {
        return site_admin
    }
}
