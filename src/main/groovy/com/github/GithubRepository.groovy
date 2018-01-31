package com.github

import groovy.transform.CompileStatic

@CompileStatic
class GithubRepository {
    Long id
    String name
    String full_name
    GithubOwner owner
    //private
    String html_url
    String description
    Boolean fork
    String url
    String forks_url
    String keys_url
    String collaborators_url
    String teams_url
    String hooks_url
    String issue_events_url
    String assignees_url
    String branches_url
    String tags_url
    String blobs_url
    String git_tags_url
    String git_refs_url
    String trees_url
    String statuses_url
    String languages_url
    String stargazers_url
    String contributors_url
    String subscribers_url
    String subscription_url
    String commits_url
    String git_commits_url
    String comments_url
    String issue_comment_url
    String contents_url
    String compare_url
    String merges_url
    String archive_url
    String downloads_url
    String issues_url
    String pulls_url
    String milestones_url
    String notifications_url
    String labels_url
    String releases_url
    String deployments_url
    String created_at
    String updated_at
    String pushed_at
    String git_url
    String ssh_url
    String clone_url
    String svn_url
    String homepage
    Integer size
    Integer stargazers_count
    Integer watchers_count
    String language
    Boolean has_issues
    Boolean has_projects
    Boolean has_downloads
    Boolean has_wiki
    Boolean has_pages
    Integer forks_count
    String mirror_url
    Boolean archived
    Integer open_issues_count
    GithubLicense license
    Integer forks
    Integer open_issues
    Integer watchers
    String default_branch
    GithubPermissions permissions
    Integer network_count
    Integer subscribers_count

    String getFullName() {
        return full_name
    }

    String getHtmlUrl() {
        return html_url
    }

    String getForksUrl() {
        return forks_url
    }

    String getKeysUrl() {
        return keys_url
    }

    String getCollaboratorsUrl() {
        return collaborators_url
    }

    String getTeamsUrl() {
        return teams_url
    }

    String getHooksUrl() {
        return hooks_url
    }

    String getIssueEventsUrl() {
        return issue_events_url
    }

    String getAssigneesUrl() {
        return assignees_url
    }

    String getBranchesUrl() {
        return branches_url
    }

    String getTagsUrl() {
        return tags_url
    }

    String getBlobsUrl() {
        return blobs_url
    }

    String getGitTagsUrl() {
        return git_tags_url
    }

    String getGitRefsUrl() {
        return git_refs_url
    }

    String getTreesUrl() {
        return trees_url
    }

    String getStatusesUrl() {
        return statuses_url
    }

    String getLanguagesUrl() {
        return languages_url
    }

    String getStargazersUrl() {
        return stargazers_url
    }

    String getContributorsUrl() {
        return contributors_url
    }

    String getSubscribersUrl() {
        return subscribers_url
    }

    String getSubscriptionUrl() {
        return subscription_url
    }

    String getCommitsUrl() {
        return commits_url
    }

    String getGitCommitsUrl() {
        return git_commits_url
    }

    String getCommentsUrl() {
        return comments_url
    }

    String getIssueCommentUrl() {
        return issue_comment_url
    }

    String getContentsUrl() {
        return contents_url
    }

    String getCompareUrl() {
        return compare_url
    }

    String getMergesUrl() {
        return merges_url
    }

    String getArchiveUrl() {
        return archive_url
    }

    String getDownloadsUrl() {
        return downloads_url
    }

    String getIssuesUrl() {
        return issues_url
    }

    String getPullsUrl() {
        return pulls_url
    }

    String getMilestonesUrl() {
        return milestones_url
    }

    String getNotificationsUrl() {
        return notifications_url
    }

    String getLabelsUrl() {
        return labels_url
    }

    String getReleasesUrl() {
        return releases_url
    }

    String getDeploymentsUrl() {
        return deployments_url
    }

    String getCreatedAt() {
        return created_at
    }

    String getUpdatedAt() {
        return updated_at
    }

    String getPushedAt() {
        return pushed_at
    }

    String getGitUrl() {
        return git_url
    }

    String getSshUrl() {
        return ssh_url
    }

    String getCloneUrl() {
        return clone_url
    }

    String getSvnUrl() {
        return svn_url
    }

    Integer getStargazersCount() {
        return stargazers_count
    }

    Integer getWatchersCount() {
        return watchers_count
    }

    Boolean getHasIssues() {
        return has_issues
    }

    Boolean getHasProjects() {
        return has_projects
    }

    Boolean getHasDownloads() {
        return has_downloads
    }

    Boolean getHasWiki() {
        return has_wiki
    }

    Boolean getHasPages() {
        return has_pages
    }

    Integer getForksCount() {
        return forks_count
    }

    String getMirrorUrl() {
        return mirror_url
    }

    Integer getOpenIssuesCount() {
        return open_issues_count
    }

    GithubLicense getLicense() {
        return license
    }

    Integer getOpenIssues() {
        return open_issues
    }

    String getDefaultBranch() {
        return default_branch
    }

    Integer getNetworkCount() {
        return network_count
    }

    Integer getSubscribersCount() {
        return subscribers_count
    }
}
