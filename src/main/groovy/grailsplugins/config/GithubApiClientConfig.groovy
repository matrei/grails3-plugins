package grailsplugins.config

import io.micronaut.context.annotation.ConfigurationProperties
import io.micronaut.http.client.HttpClientConfiguration
import io.micronaut.runtime.ApplicationConfiguration

import java.time.Duration

@ConfigurationProperties(PREFIX)
class GithubApiClientConfig extends HttpClientConfiguration {

    static final String PREFIX = 'grailsplugins.client.github-api'

    final GithubRepoClientConnectionPoolConfig connectionPoolConfiguration

    URI url

    GithubApiClientConfig(
        ApplicationConfiguration appConfig,
        GithubRepoClientConnectionPoolConfig conPoolConfig)
    {
        super(appConfig)
        this.connectionPoolConfiguration = conPoolConfig
    }


    @ConfigurationProperties(HttpClientConfiguration.ConnectionPoolConfiguration.PREFIX)
    static class GithubRepoClientConnectionPoolConfig extends HttpClientConfiguration.ConnectionPoolConfiguration {}

    @ConfigurationProperties(GithubRepoClientRetryConfig.PREFIX)
    static class GithubRepoClientRetryConfig {

        static final String PREFIX = 'retry'

        Duration delay
        int attempts
    }
}
