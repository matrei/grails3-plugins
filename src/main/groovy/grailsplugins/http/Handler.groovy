package grailsplugins.http

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import io.micronaut.http.client.exceptions.HttpClientResponseException
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription

@Slf4j
@CompileStatic
abstract class Handler<T> implements Subscriber<T> {

    abstract void handle(T t)
    abstract String createHttpClientResponseExceptionMessage(HttpClientResponseException e)

    @Override void onSubscribe(Subscription s) { s.request 1 }
    @Override void onNext(T t) { handle t }
    @Override void onComplete() {}
    @Override void onError(Throwable t) {
        if(t instanceof HttpClientResponseException) { log.warn '{}', createHttpClientResponseExceptionMessage(t as HttpClientResponseException) }
        else { log.error 'Error {}', t.message }
    }
}
