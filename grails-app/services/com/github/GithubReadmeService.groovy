package com.github

import groovy.transform.CompileStatic
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

@CompileStatic
class GithubReadmeService {
    private final String rawGithubUserContentUrl = 'https://raw.githubusercontent.com'
    private final OkHttpClient client = new OkHttpClient()

    String fetchMarkdown(String githubSlug) {
        fetchUrl("${rawGithubUserContentUrl}/${githubSlug}/master/README.md".toString())
    }

    String fetchAsciidoc(String githubSlug) {
        fetchUrl("${rawGithubUserContentUrl}/${githubSlug}/master/README.adoc".toString())
    }

    String fetchUrl(String url) {
        HttpUrl.Builder httpBuider = HttpUrl.parse(url).newBuilder()
        Request request = new Request.Builder()
                .url(httpBuider.build())
                .build()
        Response response = client.newCall(request).execute()
        String str
        if ( response.isSuccessful() ) {
            str = response.body().string()
        }
        response.close()
        str
    }


}