package grailsplugins

import com.vladsch.flexmark.ast.Node
import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.parser.Parser
import com.vladsch.flexmark.util.options.MutableDataSet
import groovy.transform.CompileStatic

@CompileStatic
class MarkdownRenderService {

    String renderMarkdown(String markdown) {
        MutableDataSet options = new MutableDataSet()
        Parser parser = Parser.builder(options).build()
        HtmlRenderer renderer = HtmlRenderer.builder(options).build()
        Node document = parser.parse(markdown)
        renderer.render(document)
    }
}