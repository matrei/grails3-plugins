package grailsplugins.util

import groovy.transform.CompileStatic

import java.nio.charset.StandardCharsets

@CompileStatic
class EncodingUtil {

    static String encodeURIComponent(String s) {
        URLEncoder.encode(s, StandardCharsets.UTF_8)
            .replace("+", "%20")
            .replace("%21", "!")
            .replace("%27", "'")
            .replace("%28", "(")
            .replace("%29", ")")
            .replace("%7E", "~")
    }
}