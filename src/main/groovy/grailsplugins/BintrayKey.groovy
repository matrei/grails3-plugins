package grailsplugins

import com.bintray.BintrayPackage
import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode

@EqualsAndHashCode
@CompileStatic
class BintrayKey {
    String owner
    String name

    static BintrayKey of(BintrayPackage bintrayPackage) {
        new BintrayKey(owner: bintrayPackage.owner, name: bintrayPackage.name)
    }
}
