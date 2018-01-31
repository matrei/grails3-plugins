package com.github

import groovy.transform.CompileStatic

@CompileStatic
class GithubLicense {
    String key
    String name
    String spdx_id
    String url

    String getSpdxid() {
        spdx_id
    }
}
