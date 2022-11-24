package grailsplugins.markup.model

import groovy.transform.CompileStatic

@CompileStatic
class IconMenuItem implements MenuItem {
    String image
    String href
    String alt
}
