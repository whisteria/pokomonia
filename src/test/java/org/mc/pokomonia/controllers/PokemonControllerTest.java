package org.mc.pokomonia.controllers;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.mc.pokomonia.services.PokemonService;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    public void get() {
        Pokemon expected = new Pokemon("hello", "description", "habitat", true);
        when(pokemonServiceMock.getByName(any())).thenReturn(expected);
        HttpRequest<String> request = HttpRequest.GET("/pokemon/hello");
        final HttpResponse<Pokemon> response = client.toBlocking().exchange(request, Pokemon.class);
        assertEquals(expected, response.body());
        assertEquals(HttpStatus.OK, response.status());
    }

    @Test
    public void getWithThrow() {
        when(pokemonServiceMock.getByName(any())).thenThrow(new IllegalStateException("error"));
        HttpRequest<String> request = HttpRequest.GET("/pokemon/hello");
        HttpClientResponseException e = assertThrows(
                HttpClientResponseException.class,
                () -> client.toBlocking().exchange(request)
        );
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, e.getStatus());
    }


    @MockBean(PokemonService.class)
    public PokemonService pokemonService() {
        return pokemonServiceMock;
    }

}