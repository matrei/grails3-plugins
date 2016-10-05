import com.github.sheehan.Plugins
import grails.util.Environment
import groovy.json.JsonSlurper

class BootStrap {

    def init = { servletContext ->
        if (Environment.developmentMode) {
            List plugins = new JsonSlurper().parseText(this.class.getClassLoader().getResourceAsStream('plugins.json').text) as List
            Plugins.set plugins
        }
    }

    def destroy = {
    }
}
