package org.mc.pokomonia.controllers;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mc.pokomonia.controllers.PokemonController.*;

@MicronautTest
class PokemonControllerTest {

    @Inject
    @Client("/")
    HttpClient client;

    @Test
    public void helloPokemon() {
        HttpRequest<String> request = HttpRequest.GET("/pokemon/hello");
        Pokemon expected = new Pokemon("hello", "description", "habitat", true);
        Pokemon actual = client.toBlocking().retrieve(request, Pokemon.class);
        assertEquals(expected, actual);
    }

}