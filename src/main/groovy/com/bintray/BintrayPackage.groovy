package com.bintray

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.github.GithubService
import groovy.transform.CompileStatic

@CompileStatic
@JsonIgnoreProperties(ignoreUnknown = true)
class BintrayPackage {
    String name
    String repo
    String owner
    String desc
    List<String> labels
    List<String> licenses
    String issueTrackerUrl
    String latestVersion
    String updated
    List<String> systemIds
    String vcsUrl
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
