package com.github

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import grails.config.Config
import grails.core.support.GrailsConfigurationAware
import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

@CompileStatic
@Slf4j
class GithubService implements GrailsConfigurationAware {

    private final Moshi moshi = new Moshi.Builder().build()
    private final OkHttpClient client = new OkHttpClient()
    private final JsonAdapter<GithubRepository> githubRepositoryJsonAdapter = moshi.adapter(GithubRepository.class)

    private static final String GITHUB_API_URL = 'https://api.github.com'
    String username
    String token
    String userAgent

    @Override
    void setConfiguration(Config config) {
        username = config.getProperty('github.username', String)
        token = config.getProperty('github.token', String)
        userAgent = config.getProperty('github.userAgent', String, 'Grails Plugins')
    }

    String authorization() {
        String encoded = "$username:$token".bytes.encodeBase64()
        "Basic $encoded".toString()
    }


    @CompileDynamic
    String ownerAndRepo(String vcsUrl) {
        def matcher = vcsUrl =~ /.*github\.com\/([^\/]+\/[^\/\.]+).*/
        if (matcher.matches()) {
            return matcher[0][1]
        }
        null
    }

    GithubRepository fetchGithubRepository(String vcsUrl) throws IOException {
        String ownerAndRepo = ownerAndRepo(vcsUrl)
        if ( !ownerAndRepo ) {
            return null
        }
        final String url = "${GITHUB_API_URL}/repos/${ownerAndRepo}".toString()
        HttpUrl.Builder httpBuider = HttpUrl.parse(url).newBuilder()
        Request request = new Request.Builder()
                .url(httpBuider.build())
                .header("Authorization", authorization())
                .header("User-Agent", userAgent)
                .build()
        Response response = client.newCall(request).execute()
        GithubRepository githubRepository
        if ( response.isSuccessful() ) {
            githubRepository = githubRepositoryJsonAdapter.fromJson(response.body().source())
        } else {
            log.warn 'Response {}. Could not fetch github repository at {}', response.code(), vcsUrl
        }
        response.close()
        githubRepository
    }
}
