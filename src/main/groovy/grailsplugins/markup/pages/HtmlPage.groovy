package grailsplugins.markup.pages

import groovy.transform.CompileStatic

@CompileStatic
interface HtmlPage {
    String html()
    String getSlug()

    void setTimestamp(String timestamp)
}
