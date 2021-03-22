package com.github

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import groovy.transform.CompileStatic

@JsonIgnoreProperties(ignoreUnknown = true)
@CompileStatic
class GithubLicense {
    String key
    String name
    String spdxId
    String url

    String getSpdxid() {
        spdxId
    }
}
