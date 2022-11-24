package grailsplugins

import grails.converters.JSON
import grailsplugins.config.PluginControllerConfig
import grailsplugins.services.PluginService
import groovy.transform.CompileStatic
import jakarta.inject.Inject

@CompileStatic
class PluginController {

    final static Map allowedMethods = [
        index: 'GET',
        refresh: 'POST',
        plugin: 'GET',
        pluginWithOwner: 'GET',
        legacyPlugins: 'GET'
    ]

    private final PluginService pluginService
    private final boolean refreshEnabled

    @Inject
    PluginController(PluginService pluginService, PluginControllerConfig config) {
        this.pluginService = pluginService
        this.refreshEnabled = config.refreshEnabled
    }

    def index(String query) {
        def plugins = query ? pluginService.findPluginsByQuery(query) : pluginService.allPlugins
        render view: 'index', model: [
            pluginList: plugins,
            query: query,
            pluginTotal: pluginService.totalPluginCount,
            topRatedPlugins: pluginService.topRatedPlugins,
            latestPlugins: pluginService.latestPlugins,
        ]
    }

    def legacyPlugins() {
        def plugins = pluginService.allPlugins
        render view: 'legacyPlugins', model: [
            pluginList: plugins,
            query: null,
            pluginTotal: plugins.size(),
            topRatedPlugins: pluginService.topRatedPlugins,
            latestPlugins: pluginService.latestPlugins,
        ]
    }

    def refresh() {
        if (!refreshEnabled) { render status: 404; return }
        pluginService.refreshPluginRegistry()
        redirect action: 'index'
    }

    def plugin(String pluginName) {
        def plugin = pluginService.findPluginByName pluginName
        if (!plugin) { render status: 404; return }
        [plugin: plugin]
    }

    def pluginWithOwner(String ownerName, String pluginName) {
        def plugin = pluginService.findPlugin(PluginId.of ownerName, pluginName)
        if (!plugin) { render status: 404; return }
        render view: 'plugin', model: [plugin: plugin]
    }

    def json() {
        render pluginService.allPlugins as JSON
    }
}
