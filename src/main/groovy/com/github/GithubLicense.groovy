package com.github

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import groovy.transform.CompileStatic

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
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
