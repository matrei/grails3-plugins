package com.github.sheehan

import grails.config.Config
import grails.converters.JSON
import grails.core.support.GrailsConfigurationAware
import groovy.json.JsonSlurper

import java.text.DateFormat
import java.text.SimpleDateFormat

class PluginController implements GrailsConfigurationAware {

    static allowedMethods = [
            index: 'GET',
            refresh: 'POST',
            plugin: 'GET',
            pluginWithOwner: 'GET',
    ]

    boolean refreshEnabled

    @Override
    void setConfiguration(Config co) {
        refreshEnabled = co.getProperty('com.grails.plugins.refresh.enabled', Boolean, false)
    }


    PluginService pluginService

    def index(String query) {
        render(view: 'index', model: [
                pluginList: pluginService.findAll(query),
                query: query,
                pluginTotal: pluginService.count(),
                topRatedPlugins: pluginService.topRatedPlugins(),
                latestPlugins: pluginService.latestPlugins(),
        ])
    }

    def refresh() {
        if ( !refreshEnabled ) {
            render status: 404
            return
        }
        pluginService.refreshPlugins()
        redirect action: 'index'
    }

    def plugin(String pluginName) {
        Map plugin = pluginService.findByPluginName(pluginName)
        if ( !plugin ) {
            response.sendError(404)
            return
        }
        [plugin: plugin]
    }

    def pluginWithOwner(String ownerName, String pluginName) {
        Map plugin = pluginService.findByOwnerNameAndPluginName(ownerName, pluginName)

        if ( !plugin ) {
            response.sendError(404)
            return
        }
        render view: 'plugin', model: [plugin: plugin]
    }
}
