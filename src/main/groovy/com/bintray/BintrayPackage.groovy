package com.bintray

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.github.GithubService
import groovy.transform.CompileStatic

@CompileStatic
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
class BintrayPackage {
    String name
    String repo
    String owner
    String desc
    List<String> labels
    List<String> attributeNames
    List<String> licenses
    List<String> customLicenses
    Integer followersCount
    String created
    String websiteUrl
    String issueTrackerUrl
    List<String> linkedToRepos
    List<String> permissions
    List<String> versions
    String latestVersion
    String updated
    Integer ratingCount
    List<String> systemIds
    String vcsUrl
    String maturity
    String mavenMetadataUrl
    String githubSlug

    void setVcsUrl(String vcsUrl) {
        this.vcsUrl = vcsUrl
        this.githubSlug = GithubService.ownerAndRepo(vcsUrl)
    }

    String getGithubSlug() {
        if (!this.githubSlug) {
            this.githubSlug = GithubService.ownerAndRepo(vcsUrl)
        }
        return this.githubSlug
    }
}
