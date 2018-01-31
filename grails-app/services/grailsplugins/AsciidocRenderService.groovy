package grailsplugins

import groovy.transform.CompileStatic
import org.asciidoctor.Asciidoctor
import static org.asciidoctor.Asciidoctor.Factory.create

@CompileStatic
class AsciidocRenderService {
    String renderAsciidoc(String asciidoc) {
        Asciidoctor asciidoctor = create()
        asciidoctor.convert(asciidoc, new HashMap<String, Object>())
    }
}