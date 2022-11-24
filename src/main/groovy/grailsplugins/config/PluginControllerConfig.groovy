package grailsplugins.config

import io.micronaut.context.annotation.ConfigurationProperties

@ConfigurationProperties(PREFIX)
class PluginControllerConfig {

    private static final String PREFIX = 'grailsplugins'

    boolean refreshEnabled = false

}
