package com.github.sheehan

import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.parser.Parser
import com.vladsch.flexmark.util.options.MutableDataSet
import com.vladsch.flexmark.ast.Node
import grails.config.Config
import grails.core.support.GrailsConfigurationAware
import grails.plugins.Plugin
import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import groovyx.net.http.HttpResponseDecorator
import groovyx.net.http.HttpResponseException
import groovyx.net.http.RESTClient
import org.asciidoctor.Asciidoctor

import java.text.DateFormat
import java.text.SimpleDateFormat

import static org.asciidoctor.Asciidoctor.Factory.create

@Slf4j
@CompileStatic
class PluginService implements GrailsConfigurationAware {

    public static final DateFormat UTC = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    public static final DateFormat DISPLAY = new SimpleDateFormat('MMM d, yyyy')

    GithubService githubService
    BintrayService bintrayService

    int numberOfLatestGuides
    int numberOfTopRatedGuides

    @Override
    void setConfiguration(Config co) {
        numberOfLatestGuides = co.getProperty('grails.plugins.latestGuides.max', Integer, 5)
        numberOfTopRatedGuides = co.getProperty('grails.plugins.topRated.max', Integer, 5)
    }

    @CompileDynamic
    Compare refreshPlugins() {
        List<Map> bintrayPackages = bintrayService.fetchPackages()
        List<Map> plugins = bintrayPackages.collect { Map data ->
            if (data.vcs_url) {
                def matcher = data.vcs_url =~ /.*github\.com\/([^\/]+\/[^\/\.]+).*/
                if (matcher.matches()) {
                    String ownerAndRepo = matcher[0][1]
                    Map githubData = githubService.fetchRepo(ownerAndRepo)
                    if (githubData) {
                        data.githubRepo = githubData
                    }
                }
            }
            data
        }

        Compare compare = Plugins.get() ? new Compare(Plugins.get(), plugins) : null
        Plugins.set plugins

        compare
    }

    void refreshReadmeHTML() {
        for (Map data : Plugins.get() ) {
            log.info 'Fetching README of {}', data.name
            String readmeHTML = renderPluginReadme(data)
            if ( readmeHTML != null ) {
                Plugins.setReadmeHTML(data.name as String, readmeHTML)
            }

        }
    }


    @CompileDynamic
    List<Map> filterPluginsByQuery(String query, List<Map> plugins) {
        if ( query ) {
            String ownerPreffix = 'owner:'
            if ( query.startsWith(ownerPreffix) ) {
                String owner = query.substring(ownerPreffix.length())
                return plugins.findAll { Map plugin -> plugin.owner == owner}

            }
            String labelPreffix = 'label:'
            if ( query.startsWith(labelPreffix) ) {
                String label = query.substring(labelPreffix.length())
                return plugins.findAll { Map plugin -> plugin.labels.contains(label) }
            }

            return plugins.findAll { Map plugin ->
                (plugin.name && plugin.name.replaceAll('-', ' ').toLowerCase().contains(query.toLowerCase())) ||
                (plugin.name && plugin.name.toLowerCase().contains(query.toLowerCase())) ||
                (plugin.desc && plugin.desc.toLowerCase().contains(query.toLowerCase())) ||
                (plugin.labels.any { String label -> label.toLowerCase().contains(query.toLowerCase()) })
            }
        }
        plugins
    }

    List<Map> findAll(String query = null) {

        List<Map> plugins = Plugins.get()
        if ( query ) {
            plugins = filterPluginsByQuery(query, plugins)
        }

        log.debug "Plugins: #{}", plugins.size()

        plugins
    }

    Map findByOwnerNameAndPluginName(String ownerName, String pluginName) {
        Plugins.get().find { Map plugin -> plugin.name == pluginName && plugin.owner == ownerName }
    }

    Map findByPluginName(String pluginName) {

        Plugins.get().find { Map plugin -> plugin.name == pluginName}
    }

    @CompileDynamic
    String renderPluginReadme(Map plugin) {
        if ( plugin.readHTML ) {
            return plugin.readHTML
        }
        String html = null
        if ( plugin.githubRepo?.full_name ) {

            RESTClient restClient = new RESTClient('https://raw.githubusercontent.com/')
            String githubSlug = plugin.githubRepo?.full_name
            try {
                HttpResponseDecorator resp = restClient.get(path: "${githubSlug}/master/README.md")

                String text = resp.getData().text
                if (text) {
                    html = renderMarkdown(text)
                }

            } catch (HttpResponseException e) {
            }
            try {
                HttpResponseDecorator resp = restClient.get(path: "${githubSlug}/master/README.adoc")

                String text = resp.getData().text
                if (text) {
                    html = renderAsciidoc(text)
                }

            } catch (HttpResponseException e ) {
            }
        }
        html
    }

    int count() {
        Plugins.get().size()
    }

    String renderAsciidoc(String asciidoc) {
        Asciidoctor asciidoctor = create()
        asciidoctor.convert(asciidoc, new HashMap<String, Object>())
    }
    String renderMarkdown(String markdown) {
        MutableDataSet options = new MutableDataSet()
        Parser parser = Parser.builder(options).build()
        HtmlRenderer renderer = HtmlRenderer.builder(options).build()
        Node document = parser.parse(markdown)
        renderer.render(document)
    }

    @CompileDynamic
    List<Map> topRatedPlugins() {
        findAll().findAll { Map plugin -> plugin.githubRepo?.stargazers_count }.sort { Map a, Map b ->
            Integer aStarCount = a.githubRepo?.stargazers_count as Integer
            Integer bStarCount = b.githubRepo?.stargazers_count as Integer
            bStarCount <=> aStarCount
        }.take(numberOfTopRatedGuides)
    }

    List<Map> latestPlugins() {
        findAll().findAll { it.latest_version_updated }.sort { Map a, Map b ->
            Date aDate = PluginService.UTC.parse(a.latest_version_updated as String)
            Date bDate = PluginService.UTC.parse(b.latest_version_updated as String)
            bDate <=> aDate
        }.take(numberOfLatestGuides)
    }
}
