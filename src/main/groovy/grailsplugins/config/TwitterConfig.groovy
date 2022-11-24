package grailsplugins.config

import groovy.transform.CompileStatic
import io.micronaut.context.annotation.ConfigurationProperties
import io.micronaut.core.annotation.Nullable

@CompileStatic
@ConfigurationProperties(PREFIX)
class TwitterConfig {

    private static final String PREFIX = 'grailsplugins.twitter'

    boolean enabled = false
    boolean debugEnabled = false
    String consumerKey
    String consumerSecret
    String accessToken
    String accessTokenSecret

}
