package com.bintray

import groovy.transform.CompileStatic
import groovy.transform.ToString

@ToString
@CompileStatic
class BintrayPackageResponse {
    Integer total
    Integer start
    Integer end
    List<BintrayPackageSimple> bintrayPackageList = []
}
