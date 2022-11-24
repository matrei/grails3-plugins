package grailsplugins

import grailsplugins.models.GrailsPlugin
import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode

@CompileStatic
@EqualsAndHashCode
class PluginId {

    final String owner
    final String name

    private PluginId(String owner, String name) {
        this.owner = owner
        this.name = name
    }

    static PluginId of(String owner, String name) {
        new PluginId(owner, name)
    }

    static PluginId of(GrailsPlugin plugin) {
        of plugin.owner, plugin.name
    }

    @Override
    String toString() { "$owner/$name" }
}