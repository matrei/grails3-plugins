package grailsplugins.config

import groovy.transform.CompileStatic
import io.micronaut.context.annotation.ConfigurationProperties

import javax.validation.constraints.NotEmpty

@CompileStatic
@ConfigurationProperties(PREFIX)
class GithubConfig {

    private static final String PREFIX = 'grailsplugins.client.github-api'

    @NotEmpty String username
    @NotEmpty String token
    @NotEmpty String url = 'https://api.github.com'

}
