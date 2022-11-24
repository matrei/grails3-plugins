package grailsplugins.http

import grailsplugins.config.GithubConfig
import io.micronaut.http.HttpResponse
import io.micronaut.http.MutableHttpRequest
import io.micronaut.http.annotation.Filter
import io.micronaut.http.filter.ClientFilterChain
import io.micronaut.http.filter.HttpClientFilter
import org.reactivestreams.Publisher

@Filter('/repos/**')
class GithubApiHttpFilter implements HttpClientFilter {

    private final String username
    private final String token

    GithubApiHttpFilter(GithubConfig config) {
        this.username = config.username
        this.token = config.token
    }

    @Override
    Publisher<? extends HttpResponse<?>> doFilter(MutableHttpRequest<?> request, ClientFilterChain chain) {
        chain.proceed(request.basicAuth(username, token))
    }
}
