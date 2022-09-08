package com.bintray

import groovy.transform.CompileStatic
import groovy.transform.ToString

/**
 * @deprecated We are no longer relying on the Bintray API.
 */
@ToString
@CompileStatic
@Deprecated
class BintrayPackageSimple {
    String name
    Boolean linked
}
