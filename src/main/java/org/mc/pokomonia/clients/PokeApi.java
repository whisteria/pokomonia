package org.mc.pokomonia.clients;

import com.fasterxml.jackson.databind.JsonNode;
import io.micronaut.context.annotation.Primary;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.uri.UriBuilder;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.mc.pokomonia.model.Pokemon;

import java.util.Map;


@Primary
@Singleton
public class PokeApi {

    @Client("https://pokeapi.co/api/v2")
    @Inject
    HttpClient httpClient;

    public Pokemon getByName(String name) {
        String uri = UriBuilder.of("/pokemon-species/{name}")
                .expand(Map.of("name", name))
                .toString();

        HttpResponse<JsonNode> response = httpClient.toBlocking().exchange(uri, JsonNode.class);
        return toPokemon(name, response.body());
    }

    public static Pokemon toPokemon(String name, JsonNode jsonNode) {
        // todo description
        String description = "todo";
        String habitat = jsonNode.get("habitat").get("name").asText();
        boolean isLegendary = jsonNode.get("is_legendary").asBoolean();
        return new Pokemon(name, description, habitat, isLegendary);
    }

}
