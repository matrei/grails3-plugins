package com.bintray

import groovy.transform.CompileStatic

@CompileStatic
class BintrayPackage {
    String name
    String repo
    String owner
    String desc
    List<String> labels
    List<String> attribute_names
    List<String> licenses
    List<String> custom_licenses
    Integer followers_count
    String created
    String website_url
    String issue_tracker_url
    List<String> linked_to_repos
    List<String> permissions
    List<String> versions
    String latest_version
    String updated
    Integer rating_count
    List<String> system_ids
    String vcs_url
    String maturity

    List<String> getAttributeNames() {
        attribute_names
    }

    List<String> getCustomLicenses() {
        custom_licenses
    }

    Integer getFollowersCount() {
        followers_count
    }

    String getWebsiteUrl() {
        website_url
    }

    String getIssueTrackerUrl() {
        issue_tracker_url
    }

    List<String> getLinkedToRepos() {
        linked_to_repos
    }

    String getLatestVersion() {
        latest_version
    }

    Integer getRatingCount() {
        rating_count
    }

    List<String> getSystemIds() {
        system_ids
    }

    String getVcsUrl() {
        vcs_url
    }
}
