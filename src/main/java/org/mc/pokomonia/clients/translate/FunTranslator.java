package org.mc.pokomonia.clients.translate;

import com.fasterxml.jackson.databind.JsonNode;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.uri.UriBuilder;
import jakarta.inject.Inject;

import java.util.Map;

public class FunTranslator {

    record FunTranslationRequest(String text) {
    }

    @Client("https://api.funtranslations.com")
    @Inject
    HttpClient httpClient;

    public String translate(String text, String translator) {
        String uri = UriBuilder.of("/translate/{translator}")
                .expand(Map.of("translator", translator))
                .toString();

        HttpRequest<FunTranslationRequest> post = HttpRequest.POST(uri, new FunTranslationRequest(text));
        return getTranslation(httpClient.toBlocking().retrieve(post, JsonNode.class));
    }

    public static String getTranslation(JsonNode response) {
        return response.get("contents").get("translated").asText();
    }

}
