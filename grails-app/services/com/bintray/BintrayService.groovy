package com.bintray

import grails.config.Config
import grails.core.support.GrailsConfigurationAware
import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import io.micronaut.core.type.Argument
import io.micronaut.core.type.Headers
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.client.BlockingHttpClient
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.http.uri.UriBuilder

/**
 * @deprecated We are no longer using Bintray API. Instead read Grails plugins metadata from https://grails.github.io/grails-plugins-metadata/grails-plugins.json
 */
@Deprecated
@CompileStatic
@Slf4j
class BintrayService implements GrailsConfigurationAware {

    private static final String BINTRAY_API_URL = "https://api.bintray.com";

    String organization
    String repository
    String token
    String username
    BlockingHttpClient client = HttpClient.create(new URL(BINTRAY_API_URL)).toBlocking()

    /**
     * @deprecated We are no longer using Bintray API.
     */
    @Deprecated
    @Override
    void setConfiguration(Config co) {
        organization = co.getProperty('bintray.organization', String, 'grails')
        repository = co.getProperty('bintray.repository', String, 'plugins')
        this.username = co.getProperty('bintray.username', String)
        this.token = co.getProperty('bintray.token', String)
    }

    /**
     * @deprecated We are no longer using Bintray API.
     */
    @Deprecated
    BintrayPackage fetchBintrayPackage(String name, String organization = this.organization, String repository = this.repository) throws IOException {
        final String url = "/packages/${organization}/${repository}/${name}".toString()
        try {
            log.trace("sending request to {}", url)
            HttpResponse<BintrayPackage> response = client
                    .exchange(HttpRequest.GET(url).basicAuth(username, token), BintrayPackage)
            log.trace("fetched bintray packages {}", url)
            return response?.body()

        } catch(HttpClientResponseException e) {
            log.warn 'Response {}. Could not fetch bintray package at {}', e.status.code, name
        }
        null
    }

    /**
     * @deprecated We are no longer using Bintray API.
     */
    @Deprecated
    @CompileDynamic
    BintrayPackageResponse fetchBintrayPackagesByStartPosition(Integer startPos) throws NumberFormatException, IOException {
        log.trace 'Fetching bintray packaging at position: {}', startPos
        String url = UriBuilder.of("/repos/grails/plugins/packages").queryParam("start_pos", startPos).build()
        log.debug("fetching {}", url)

        try {
            def response = client.exchange(HttpRequest.GET(url).basicAuth(username, token), Argument.of(List, BintrayPackageSimple))

            log.trace("fetched {}", url)
            List<BintrayPackageSimple> bintrayPackageList = response?.body()

            return new BintrayPackageResponse(start: startPosition(response.headers),
                    end: endPositionHeader(response.headers),
                    total: totalHeader(response.headers),
                    bintrayPackageList: bintrayPackageList)
        } catch(HttpClientResponseException e) {
            log.warn 'Response {}. Could not fetch bintray packages at {}', e.status.code, startPos
        }
    }

    /**
     * @deprecated We are no longer using Bintray API.
     */
    @Deprecated
    List<Integer> expectedExtraStarPositions(Integer total, Integer startPosition, Integer endPosition) {
        if ( total == null || startPosition == null || endPosition == null) {
            return []
        }

        int pageSize = (endPosition - startPosition) + 1
        Integer end = startPosition
        List<Integer> result = []
        for(;;) {
            if ( total <=  (pageSize + end) ) {
                break
            }
            end = end + pageSize
            result << end
        }
        result
    }


    private Integer totalHeader(Headers headers) {
        Optional<Integer> optionalInteger = headers.get("X-RangeLimit-Total", Integer)
        optionalInteger.isPresent() ? optionalInteger.get() : null
    }

    private Integer startPosition(Headers headers) {
        Optional<Integer> optionalInteger = headers.get("X-RangeLimit-StartPos", Integer)
        optionalInteger.isPresent() ? optionalInteger.get() : null
    }

    private Integer endPositionHeader(Headers headers) {
        Optional<Integer> optionalInteger = headers.get("X-RangeLimit-EndPos", Integer)
        optionalInteger.isPresent() ? optionalInteger.get() : null
    }
}
