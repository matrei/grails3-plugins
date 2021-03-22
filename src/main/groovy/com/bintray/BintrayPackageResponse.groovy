package com.bintray

import groovy.transform.CompileStatic
import groovy.transform.ToString

/**
 * @deprecated We are not longer relying on the Bintray API.
 */
@ToString
@CompileStatic
@Deprecated
class BintrayPackageResponse {
    Integer total
    Integer start
    Integer end
    List<BintrayPackageSimple> bintrayPackageList = []
}
