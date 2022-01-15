package org.mc.pokomonia.services;

import com.fasterxml.jackson.databind.JsonNode;
import io.micronaut.context.annotation.Primary;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.uri.UriBuilder;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.Map;


@Primary
@Singleton
public class PokeApiRestClient {

    public static class PokemonDeserializationException extends RuntimeException {

        public PokemonDeserializationException(String message, Exception e) {
            super(message, e);
        }
    }

    public record Pokemon(String name, String description, String habitat, boolean isLegendary) {
    }



    @Client("https://pokeapi.co/api/v2")
    @Inject
    HttpClient httpClient;

    public Pokemon getByName(String name) {
        String uri = UriBuilder.of("/pokemon-species/{name}")
                .expand(Map.of("name", name))
                .toString();

        JsonNode jsonNode = httpClient.toBlocking().retrieve(uri, JsonNode.class);
        return toPokemon(name, jsonNode);
    }

    public static Pokemon toPokemon(String name, JsonNode jsonNode) throws PokemonDeserializationException {
        try {
            // todo description
            String description = "todo";
            String habitat = jsonNode.get("habitat").get("name").asText();
            boolean isLegendary = jsonNode.get("is_legendary").asBoolean();
            return new Pokemon(name, description, habitat, isLegendary);
        } catch (Exception e) {
            throw new PokemonDeserializationException("could not deserialize " + name, e);
        }
    }

}
