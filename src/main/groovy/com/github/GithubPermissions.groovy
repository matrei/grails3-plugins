package com.github

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import groovy.transform.CompileStatic

@JsonIgnoreProperties(ignoreUnknown = true)
@CompileStatic
class GithubPermissions {
    Boolean admin
    Boolean push
    Boolean pull
}
