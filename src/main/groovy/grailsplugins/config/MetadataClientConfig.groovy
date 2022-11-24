package grailsplugins.config

import io.micronaut.context.annotation.ConfigurationProperties
import io.micronaut.http.client.HttpClientConfiguration
import io.micronaut.runtime.ApplicationConfiguration

import java.time.Duration

@ConfigurationProperties(PREFIX)
class MetadataClientConfig extends HttpClientConfiguration {

    static final String PREFIX = 'grailsplugins.client.metadata'

    final MetadataClientConnectionPoolConfig connectionPoolConfiguration

    URI url
    String path

    MetadataClientConfig(final ApplicationConfiguration applicationConfiguration, final MetadataClientConnectionPoolConfig connectionPoolConfiguration) {
        super(applicationConfiguration)
        this.connectionPoolConfiguration = connectionPoolConfiguration
    }

    @Override
    ConnectionPoolConfiguration getConnectionPoolConfiguration() { connectionPoolConfiguration }

    @ConfigurationProperties(HttpClientConfiguration.ConnectionPoolConfiguration.PREFIX)
    static class MetadataClientConnectionPoolConfig extends HttpClientConfiguration.ConnectionPoolConfiguration {}

    @ConfigurationProperties(MetadataClientRetryConfig.PREFIX)
    static class MetadataClientRetryConfig {

        static final String PREFIX = 'retry'

        Duration delay
        int attempts
    }
}
