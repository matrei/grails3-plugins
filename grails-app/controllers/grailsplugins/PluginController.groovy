package grailsplugins

import grails.config.Config
import grails.core.support.GrailsConfigurationAware
import groovy.transform.CompileStatic

@CompileStatic
class PluginController implements GrailsConfigurationAware {

    static allowedMethods = [
            index: 'GET',
            refresh: 'POST',
            plugin: 'GET',
            pluginWithOwner: 'GET',
            legacyPlugins: 'GET'
    ]

    GrailsPluginsRepository grailsPluginsRepository
    GrailsPluginsService grailsPluginsService

    boolean refreshEnabled

    @Override
    void setConfiguration(Config co) {
        refreshEnabled = co.getProperty('com.grails.plugins.refresh.enabled', Boolean, false)
    }

    def index(String query) {
        List<GrailsPlugin> pluginList
        int total
        if ( query ) {
            pluginList = grailsPluginsRepository.findByQuery(query)
            total = grailsPluginsRepository.count()
        } else {
            pluginList = grailsPluginsRepository.findAll()
            total = pluginList.size()
        }
        render(view: 'index', model: [
                pluginList: pluginList,
                query: query,
                pluginTotal: total,
                topRatedPlugins: grailsPluginsRepository.topRatedPlugins(),
                latestPlugins: grailsPluginsRepository.latestPlugins(),
        ])
    }

    def legacyPlugins() {
        List<GrailsPlugin> pluginList = grailsPluginsRepository.findAll()
        render(view: 'legacyPlugins', model: [
            pluginList: pluginList,
            query: null,
            pluginTotal: pluginList.size(),
            topRatedPlugins: grailsPluginsRepository.topRatedPlugins(),
            latestPlugins: grailsPluginsRepository.latestPlugins(),
        ])
    }

    def refresh() {
        if ( !refreshEnabled ) {
            render status: 404
            return
        }
        grailsPluginsService.refresh()
        redirect action: 'index'
    }

    def plugin(String pluginName) {
        GrailsPlugin plugin = grailsPluginsRepository.findByPluginName(pluginName)
        if ( !plugin ) {
            response.sendError(404)
            return
        }
        [plugin: plugin]
    }

    def pluginWithOwner(String ownerName, String pluginName) {

        GrailsPlugin plugin = grailsPluginsRepository.find(new BintrayKey(owner: ownerName, name: pluginName))

        if ( !plugin ) {
            response.sendError(404)
            return
        }
        render view: 'plugin', model: [plugin: plugin]
    }
}
