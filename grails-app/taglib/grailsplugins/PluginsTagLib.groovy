package grailsplugins

import grailsplugins.models.GrailsPlugin
import grailsplugins.util.EncodingUtil
import groovy.util.logging.Slf4j
import org.grails.buffer.GrailsPrintWriter

import java.text.ParseException
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Slf4j
class PluginsTagLib {

    public static final DateTimeFormatter UTC = DateTimeFormatter.ISO_INSTANT.withZone ZoneId.of('UTC')
    public static final DateTimeFormatter DISPLAY = DateTimeFormatter.ofLocalizedDate FormatStyle.MEDIUM

    static namespace = 'grailsplugins'

    static returnObjectForTags = ['packageLink']

    def lastUpdated = { attrs, body ->
        GrailsPlugin plugin = attrs.plugin as GrailsPlugin
        if ( plugin.updated ) {
            try {
                def date = UTC.parse plugin.updated
                out << DISPLAY.format(date)
            } catch ( ParseException ignore ) {
                log.error 'unable to parse updated {} for {}', plugin.updated, plugin.name
            } catch ( NumberFormatException ignore ) {
                log.error 'NumberFormatException {} for {}', plugin.updated, plugin.name
            }
        }
    }

    def packageLink = { attrs, body ->
        def plugin = attrs.plugin as GrailsPlugin
        plugin.groupId ? "https://repo.grails.org/ui/native/core/${plugin.groupId.replaceAll('\\.', '/')}/$plugin.artifactId" : ''
    }

    def labelsTagCloud = { attrs, body ->
        def pluginList = attrs.pluginList as List<GrailsPlugin>
        String header = g.message code: 'plugins.tagcloud.labels', default: 'Plugins by Tag'
        List<Tag> tags = labelsTagCloud pluginList
        renderTagCloud tags, header, 'label', out
    }

    def ownersTagCloud = {attrs, body ->
        def pluginList = attrs.pluginList as List<GrailsPlugin>
        String header = g.message code: 'plugins.tagcloud.owner', default: 'Plugins by Owner'
        def tags = ownerTagCloud pluginList
        renderTagCloud tags, header, 'owner', out
    }

    void renderTagCloud(List<Tag> tags, String header, String prefix, GrailsPrintWriter out) {
        String encodedPrefix = EncodingUtil.encodeURIComponent prefix
        out << "<div class='tagsbytopic'>"
        out << "<h3 class='columnheader'>${header}</h3>"
        out << '<ul>'
        for ( Tag tag : tags) {
            out << "<li class='tag${tag.occurrence}'><a href='q/${encodedPrefix}:${EncodingUtil.encodeURIComponent tag.title}'>${tag.title}</a></li>"
        }
        out << '</ul>'
        out << '</div>'
    }

    List<Tag> ownerTagCloud(List<GrailsPlugin> pluginList) {
        def tagsOccurrence = [:] as Map<String, Integer>
        for (GrailsPlugin plugin : pluginList) {
            def label = plugin.owner
            if ( label ) {
                if (!tagsOccurrence.containsKey(label)) {
                    tagsOccurrence[label] = 1
                } else {
                    tagsOccurrence[label] = tagsOccurrence[label] + 1
                }
            }
        }
        tagListFromTagsOccurrence tagsOccurrence
    }

    List<Tag> labelsTagCloud(List<GrailsPlugin> pluginList) {
        def tagsOccurrence = [:] as Map<String, Integer>
        for (def plugin : pluginList) {
            def labels = plugin.labels.collect { it.toLowerCase() }
            if ( labels ) {
                for (def label : labels) {
                    if (!tagsOccurrence.containsKey(label)) {
                        tagsOccurrence[label] = 1
                    } else {
                        tagsOccurrence[label] = tagsOccurrence[label] + 1
                    }
                }
            }
        }
        tagListFromTagsOccurrence(tagsOccurrence)
    }

    List<Tag> tagListFromTagsOccurrence(Map<String, Integer> tagsOccurrence) {
        tagsOccurrence?.collect { String k, Integer v -> new Tag(title: k, occurrence: v) }?.sort { Tag a, Tag b ->
            a.title <=> b.title
        }
    }
}

class Tag {
    String title
    int occurrence
}