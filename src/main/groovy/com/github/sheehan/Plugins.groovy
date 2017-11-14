package com.github.sheehan

import groovy.transform.CompileStatic

@CompileStatic
class Plugins {

    private static List<Map> plugins = []

    static List<Map> get() {
        plugins
    }

    static void set(List<Map> plugins) {
        this.plugins = plugins
    }

    static void setReadmeHTML(String name, String readmeHtml) {
        if ( plugins.find { it.name == name } ) {
            plugins.find { it.name == name }.readmeHTML = readmeHtml
        }
    }
}
