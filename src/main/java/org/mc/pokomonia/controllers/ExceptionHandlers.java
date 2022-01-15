package org.mc.pokomonia.controllers;

import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;


/**
 * to get the correct http status and further info for any errors
 */
public interface ExceptionHandlers {

    record Error(String message) {
    }

    @Produces
    @Singleton
    @Requires(classes = {HttpClientResponseException.class, ExceptionHandler.class})
    class httpClientResponseExceptionHandler
            implements ExceptionHandler<HttpClientResponseException, HttpResponse<Error>> {

        @Override
        public HttpResponse<Error> handle(HttpRequest request, HttpClientResponseException e) {
            if (e.getStatus() == HttpStatus.NOT_FOUND) {
                // this will be from the PokeApi as for now the translation runs in a try catch
                return HttpResponse.notFound(new Error("pokemon not found"));
            } else {
                return HttpResponse.serverError(new Error(e.getMessage()));
            }
        }
    }
}
