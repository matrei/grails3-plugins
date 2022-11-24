package grailsplugins.config

import io.micronaut.context.annotation.ConfigurationProperties

import javax.validation.constraints.NotEmpty

@ConfigurationProperties(PREFIX)
class GithubReadmeConfig {

    private static final String PREFIX = 'grailsplugins.client.readme'

    String url = 'https://raw.githubusercontent.com'
    String[] markdownFilenames =  ['README.md']
    String[] asciidocFilenames = ['README.adoc']

}
