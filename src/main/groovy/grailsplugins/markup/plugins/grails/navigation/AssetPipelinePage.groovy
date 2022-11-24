package grailsplugins.markup.plugins.grails.navigation

import groovy.transform.CompileStatic
import grailsplugins.markup.pages.Page

@CompileStatic
abstract class AssetPipelinePage extends Page {

    @Override
    String renderImage(String image, String alt, String className) {
        "<img src=\"/assets/${image}\" class=\"${className}\" alt=\"${alt}\"/>" as String
    }
}

