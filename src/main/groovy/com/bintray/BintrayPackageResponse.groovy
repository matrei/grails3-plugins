package com.bintray

import groovy.transform.CompileStatic

@CompileStatic
class BintrayPackageResponse {
    Integer total
    Integer start
    Integer end
    List<BintrayPackageSimple> bintrayPackageList = []
}
