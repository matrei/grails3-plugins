package org.grails.plugins

import com.github.sheehan.PluginService

class PluginsTagLib {

    static namespace = 'grailsplugins'

    static returnObjectForTags = ['bintrayLink']

    def lastUpdated = { attrs, body ->
        if ( attrs.plugin.latest_version_updated ) {
            Date date = PluginService.UTC.parse(attrs.plugin.latest_version_updated)
            out << PluginService.DISPLAY.format(date)
        }
    }

    def bintrayLink = { attrs, body ->
        Map plugin = attrs.plugin
        "https://bintray.com/${plugin.owner}/${plugin.repo}/${plugin.name}"
    }

    def labelsTagCloud = { attrs, body ->
        List<Map> pluginList = attrs.pluginList
        String header = labelsHeader()
        List<Tag> tags = tagCloudByKey(pluginList, 'labels')
        renderTagCloud(tags, header, 'label', out)
    }

    def ownersTagCloud = { attrs, body ->
        List<Map> pluginList = attrs.pluginList
        String header = ownersHeader()
        List<Tag> tags = tagCloudByKey(pluginList, 'owner')
        renderTagCloud(tags, header, 'owner', out)
    }

    void renderTagCloud(List<Tag> tags, String header, String preffix, def out) {
        out << "<div class='tagsbytopic'>"
        out << "<h3 class='columnheader'>${header}</h3>"
        out << '<ul>'
        for ( Tag tag : tags) {
            out << "<li class='tag${tag.ocurrence}'><a href='q/${preffix}:${tag.title}'>${tag.title}</a></li>"
        }
        out << '</ul>'
        out << '</div>'
    }

    String labelsHeader() {
        g.message(code: 'plugins.tagcloud.labels', default: 'Plugins by Tag')
    }

    String ownersHeader() {
        g.message(code: 'plugins.tagcloud.owner', default: 'Plugins by Owner')
    }

    List<Tag> tagCloudByKey(List<Map> pluginList, String key) {
        Map<String, Integer> tagsOcurrence = [:] as Map<String, Integer>
        for (Map plugin : pluginList) {
            if (plugin.get(key) ) {
                Object obj = plugin.get(key)
                if (obj instanceof List) {
                    for (String label : obj as List<String>) {
                        if (!tagsOcurrence.containsKey(label)) {
                            tagsOcurrence[label] = 1
                        } else {
                            tagsOcurrence[label] = tagsOcurrence[label] + 1
                        }
                    }
                } else if (obj instanceof String ) {
                    String label = obj as String
                    if (!tagsOcurrence.containsKey(label)) {
                        tagsOcurrence[label] = 1
                    } else {
                        tagsOcurrence[label] = tagsOcurrence[label] + 1
                    }
                }
            }
        }
        tagsOcurrence.collect { String k, Integer v -> new Tag(title: k, ocurrence: v) }.sort { Tag a, Tag b ->
            a.title <=> b.title
        }
    }
}

class Tag {
    String title
    int ocurrence
}