package grailsplugins.config

import groovy.transform.CompileStatic
import io.micronaut.context.annotation.ConfigurationProperties

@CompileStatic
@ConfigurationProperties(PREFIX)
class MetadataHandlerConfig {

    private static final String PREFIX = 'grailsplugins'

    String[] blacklist = []
}
