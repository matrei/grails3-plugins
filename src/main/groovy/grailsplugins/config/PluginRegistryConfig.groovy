package grailsplugins.config

import io.micronaut.context.annotation.ConfigurationProperties


@ConfigurationProperties(PREFIX)
class PluginRegistryConfig {

    private static final String PREFIX = 'grailsplugins'

    int numberOfLatestPlugins = 5
    int numberOfTopRatedPlugins = 5
    int staleAfterMinutes = 1440

}
