package org.mc.pokomonia.controllers;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.mc.pokomonia.services.PokemonService;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mc.pokomonia.services.PokemonService.Pokemon;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@MicronautTest
class PokemonControllerTest {

    @Inject
    @Client("/")
    HttpClient client;

    private final PokemonService pokemonServiceMock  = mock(PokemonService.class);

    @Test
    public void helloPokemon() {
        Pokemon expected = new Pokemon("hello", "description", "habitat", true);
        when(pokemonServiceMock.getByName(any())).thenReturn(expected);
        HttpRequest<String> request = HttpRequest.GET("/pokemon/hello");
        Pokemon actual = client.toBlocking().retrieve(request, Pokemon.class);
        assertEquals(expected, actual);
    }


    @MockBean(PokemonService.class)
    public PokemonService pokemonService() {
        return pokemonServiceMock;
    }

}