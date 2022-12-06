package org.grails.plugins.grails.navigation

import grailsplugins.markup.pages.HtmlPage
import grailsplugins.markup.plugins.grails.navigation.GenericPage
import grailsplugins.markup.plugins.grails.navigation.PluginsPage

class GrailsNavigationTagLib {

    static namespace = 'grailsnavigation'

    def mainHeader = { attrs, body ->
        HtmlPage page
        if ( attrs.page == 'plugins' ) {
            page = new PluginsPage()
            page.active = attrs.active as boolean
        } else {
            page = new GenericPage()
        }
        out << page?.mainHeader()
    }

    def footer = { attrs, body ->
        PluginsPage pluginsPage = new PluginsPage()
        out << pluginsPage.renderFooter()
    }

    def scriptAtClosingBody = { attrs, body ->
        PluginsPage pluginsPage = new PluginsPage()
        out << pluginsPage.scriptAtClosingBody()
    }
}