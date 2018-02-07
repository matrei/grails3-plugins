package com.bintray

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import grails.config.Config
import grails.core.support.GrailsConfigurationAware
import groovy.util.logging.Slf4j
import okhttp3.Headers
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.lang.reflect.Type

@Slf4j
class BintrayService implements GrailsConfigurationAware {

    private final Moshi moshi = new Moshi.Builder().build()
    private final OkHttpClient client = new OkHttpClient()
    private final JsonAdapter<BintrayPackage> bintrayPackageJsonAdapter = moshi.adapter(BintrayPackage.class)


    private static final String BINTRAY_API_URL = "https://api.bintray.com";

    String organization
    String repository
    String username
    String token

    @Override
    void setConfiguration(Config co) {
        organization = co.getProperty('bintray.organization', String, 'grails')
        repository = co.getProperty('bintray.repository', String, 'plugins')
        username = co.getProperty('bintray.username', String)
        token = co.getProperty('bintray.token', String)
    }

    String authorization() {
        String encoded = "$username:$token".bytes.encodeBase64()
        "Basic $encoded".toString()
    }

    BintrayPackage fetchBintrayPackage(String name, String organization = this.organization, String repository = this.repository) throws IOException {
        final String url = "${BINTRAY_API_URL}/packages/${organization}/${repository}/${name}".toString()
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", authorization())
                .build()
        Response response = client.newCall(request).execute()
        BintrayPackage bintrayPackage
        if ( response.isSuccessful() ) {
            bintrayPackage = bintrayPackageJsonAdapter.fromJson(response.body().source())
        } else {
            log.warn 'Response {}. Could not fetch bintray package at {}', response.code(), name
        }
        response.close()
        return bintrayPackage
    }

    BintrayPackageResponse fetchBintrayPackagesByStartPosition(Integer startPos) throws NumberFormatException, IOException {
        log.info 'Fetching bintray packaging at position: {}', startPos
        final String url = "${BINTRAY_API_URL}/repos/grails/plugins/packages"
        final Map<String, String> params = new HashMap<>()
        params.put("start_pos", String.valueOf(startPos))
        HttpUrl.Builder httpBuider = HttpUrl.parse(url).newBuilder()
        if (params != null) {
            for(Map.Entry<String, String> param : params.entrySet()) {
                httpBuider.addQueryParameter(param.getKey(),param.getValue())
            }
        }

        Request request = new Request.Builder()
                .url(httpBuider.build())
                .header("Authorization", authorization())
                .build()
        Response response = client.newCall(request).execute()
        BintrayPackageResponse rsp
        if ( response.isSuccessful() ) {

            String bintrayPackageJsonResponse = response.body().string()
            Type type = Types.newParameterizedType(List.class, BintrayPackageSimple.class)
            JsonAdapter<List<BintrayPackageSimple>> adapter = moshi.adapter(type)
            List<BintrayPackageSimple> bintrayPackageList = adapter.fromJson(bintrayPackageJsonResponse)

            Headers headers = response.headers()
            rsp = new BintrayPackageResponse(start: startPosition(headers),
                    end: endPositionHeader(headers),
                    total: totalHeader(headers),
                    bintrayPackageList: bintrayPackageList)
        } else {
            log.warn 'Response {}. Could not fectch bintray packages at {}', response.code(), startPos
        }
        response.close()
        rsp
    }

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
        Integer total = null
        List<String> rangeLimitTotalValues = headers.values("X-RangeLimit-Total")
        if (rangeLimitTotalValues != null && !rangeLimitTotalValues.isEmpty()) {
            total = Integer.valueOf(rangeLimitTotalValues.get(0))
        }
        total
    }

    private Integer startPosition(Headers headers) {
        Integer startPosition = null
        List<String> rangeLimitStartPositionValues = headers.values("X-RangeLimit-StartPos")
        if (rangeLimitStartPositionValues != null && !rangeLimitStartPositionValues.isEmpty()) {
            startPosition = Integer.valueOf(rangeLimitStartPositionValues.get(0))
        }
        startPosition
    }

    private Integer endPositionHeader(Headers headers) {
        Integer endPosition = null
        List<String> rangeLimitEndPositionValues = headers.values("X-RangeLimit-EndPos")
        if (rangeLimitEndPositionValues != null && !rangeLimitEndPositionValues.isEmpty()) {
            endPosition = Integer.valueOf(rangeLimitEndPositionValues.get(0))
        }
        endPosition
    }
}