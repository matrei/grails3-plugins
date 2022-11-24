package grailsplugins.http

import grailsplugins.services.PluginService
import grailsplugins.config.MetadataHandlerConfig
import grailsplugins.models.GrailsPlugin
import groovy.transform.CompileStatic
import io.micronaut.context.annotation.Prototype
import io.micronaut.http.client.exceptions.HttpClientResponseException
import org.reactivestreams.Subscription

@Prototype
@CompileStatic
class MetadataHandler extends Handler<List<GrailsPlugin>> {

    private final PluginService pluginService
    private final String[] blacklist

    MetadataHandler(PluginService pluginService, MetadataHandlerConfig config) {
        this.pluginService = pluginService
        this.blacklist = config.blacklist
    }

    @Override
    void onSubscribe(Subscription s) {
        pluginService.backup()
        pluginService.clearStalePlugins()
        s.request 1
    }

    @Override
    void handle(List<GrailsPlugin> plugins) {
        plugins
            .findAll {!blacklist.contains(it.packageInfo.name) }
            .each {pluginService.processPlugin it }
    }

    @Override
    void onError(Throwable t) {
        pluginService.restoreBackup()
        super.onError t
    }

    @Override
    String createHttpClientResponseExceptionMessage(HttpClientResponseException e) {
        "Response $e.status.code. Could not fetch Grails plugin metadata from Github with error $e.message"
    }
}