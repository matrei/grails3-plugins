package grailsplugins

import grailsplugins.models.GrailsPlugin
import groovy.transform.CompileStatic
import io.micronaut.context.annotation.DefaultImplementation

@CompileStatic
@DefaultImplementation(DefaultPluginRegistry)
interface PluginRegistry {

    void backup()
    void restoreBackup()
    void clearStalePlugins()

    void savePlugin(GrailsPlugin plugin)

    Collection<GrailsPlugin> getAllPlugins()
    Collection<GrailsPlugin> getTopRatedPlugins()
    Collection<GrailsPlugin> getLatestPlugins()
    int getTotalPluginCount()

    GrailsPlugin findPlugin(PluginId pluginId)
    Collection<GrailsPlugin> findPluginsByQuery(String query)
    Collection<GrailsPlugin> findPluginsByOwner(String owner)
    GrailsPlugin findPluginByName(String name)
    String findPreviousVersionOf(GrailsPlugin plugin)

}
