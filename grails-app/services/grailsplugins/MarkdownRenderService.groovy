package grailsplugins

import com.vladsch.flexmark.util.ast.Document
import com.vladsch.flexmark.util.data.MutableDataSet
import com.vladsch.flexmark.parser.Parser
import com.vladsch.flexmark.html.HtmlRenderer
import groovy.transform.CompileStatic

@CompileStatic
class MarkdownRenderService {

    String renderMarkdown(String markdown) {
        MutableDataSet options = new MutableDataSet()
        Parser parser = Parser.builder(options).build()
        HtmlRenderer renderer = HtmlRenderer.builder(options).build()
        Document document = parser.parse(markdown)
        renderer.render(document)
    }
}