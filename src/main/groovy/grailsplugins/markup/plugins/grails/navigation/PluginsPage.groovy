package grailsplugins.markup.plugins.grails.navigation

import groovy.transform.CompileStatic
import grailsplugins.markup.Navigation
import grailsplugins.markup.model.MenuItem

@CompileStatic
class PluginsPage extends AssetPipelinePage  {
    String mainContent() { '' }
    String slug = ''
    String bodyClass = ''
    String title = 'Grails 3/4 Plugins'

    boolean active = true

    @Override
    MenuItem menuItem() {
        active ? Navigation.pluginsMenuItem() : null
    }
}

