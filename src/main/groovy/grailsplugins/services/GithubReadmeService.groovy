package grailsplugins.services

import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.parser.Parser
import com.vladsch.flexmark.util.data.MutableDataSet
import grailsplugins.config.GithubReadmeConfig
import grailsplugins.http.ReadmeClient
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import io.micronaut.http.client.exceptions.HttpClientResponseException
import jakarta.inject.Singleton
import org.asciidoctor.Asciidoctor
import org.asciidoctor.Options
import org.asciidoctor.SafeMode

import static org.asciidoctor.Asciidoctor.Factory.create

@Slf4j
@Singleton
@CompileStatic
class GithubReadmeService {

    private final GithubReadmeConfig config
    private final ReadmeClient client
    private final Parser markdownParser
    private final HtmlRenderer markdownHtmlRenderer
    private final Asciidoctor asciidoctor
    private final Options asciiDoctorOptions

    GithubReadmeService(
        GithubReadmeConfig config,
        ReadmeClient client
    ) {
        this.config = config
        this.client = client
        MutableDataSet options = new MutableDataSet()
        this.markdownParser = Parser.builder(options).build()
        this.markdownHtmlRenderer = HtmlRenderer.builder(options).build()
        this.asciidoctor = create()
        this.asciiDoctorOptions.builder().safe(SafeMode.SECURE).build()
    }

    String fetchMarkdown(String githubSlug) { findAndFetchReadme config.markdownFilenames, githubSlug }
    String fetchAsciidoc(String githubSlug) { findAndFetchReadme config.asciidocFilenames, githubSlug }

    private String findAndFetchReadme(String[] filenames, String slug) {
        filenames.findResult { String filename -> fetchUrl "/$slug/HEAD/$filename" }
    }

    private String fetchUrl(String path) {
        log.debug 'Trying to fetch README at {}{}', config.url, path
        String readme = null
        try {
            readme = client.fetchReadme path
            if(!readme) { // 404
                log.debug 'Response 404 (this is normal, multiple readme filenames are checked). Tried to fetch README at {}{}', config.url, path
            }
        }
        catch (HttpClientResponseException e) {
            log.error 'Response {}. Tried to fetch README at {}{}', e.status.code, config.url, path
        }
        readme
    }

    String renderMarkdown(String markdown) { markdownHtmlRenderer.render markdownParser.parse(markdown) }
    String renderAsciidoc(String asciidoc) { asciidoctor.convert asciidoc, asciiDoctorOptions }
}
