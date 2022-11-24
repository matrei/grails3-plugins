package grailsplugins.markup.plugins.grails.navigation

import groovy.transform.CompileStatic
import grailsplugins.markup.model.MenuItem

@CompileStatic
class GenericPage extends AssetPipelinePage {
    String mainContent() { '' }
    String slug = ''
    String bodyClass = ''
    String title = 'Grails'

    boolean active = true

    @Override
    MenuItem menuItem() {
        null
    }
}
