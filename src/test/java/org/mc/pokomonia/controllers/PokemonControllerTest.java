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
import org.mc.pokomonia.model.Pokemon;
import org.mc.pokomonia.clients.FunTranslator;
import org.mc.pokomonia.clients.PokeApi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@MicronautTest
class PokemonControllerTest {

    @Inject
    @Client("/")
    HttpClient client;

    private final PokeApi pokeApiMock = mock(PokeApi.class);
    private final FunTranslator funTranslatorMock = mock(FunTranslator.class);

    @Test
    public void get() {
        Pokemon expected = new Pokemon("mewtwo", "todo", "rare", true);
        when(pokeApiMock.getByName(any())).thenReturn(expected);
        HttpRequest<String> request = HttpRequest.GET("/pokemon/mewtwo");
        final HttpResponse<Pokemon> response = client.toBlocking().exchange(request, Pokemon.class);
        assertEquals(expected, response.body());
        assertEquals(HttpStatus.OK, response.status());
    }

    @Test
    public void getWithThrow() {
        when(pokeApiMock.getByName(any())).thenThrow(new IllegalStateException("error"));
        HttpRequest<String> request = HttpRequest.GET("/pokemon/mewtwo");
        HttpClientResponseException e = assertThrows(
                HttpClientResponseException.class,
                () -> client.toBlocking().exchange(request)
        );
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, e.getStatus());
    }


    // translated
    @Test
    public void translated() {
        Pokemon pokemon = new Pokemon("mewtwo", "todo", "rare", true);
        when(pokeApiMock.getByName(any())).thenReturn(pokemon);
        when(funTranslatorMock.translate(any(), any())).thenReturn("my translation");
        HttpRequest<String> request = HttpRequest.GET("/pokemon/translated/mewtwo");
        final HttpResponse<Pokemon> response = client.toBlocking().exchange(request, Pokemon.class);
        assertEquals(new Pokemon("mewtwo", "my translation", "rare", true), response.body());
        assertEquals(HttpStatus.OK, response.status());
    }

    @Test
    public void translationFails() {
        Pokemon pokemon = new Pokemon("mewtwo", "todo", "rare", true);
        when(pokeApiMock.getByName(any())).thenReturn(pokemon);
        when(funTranslatorMock.translate(any(), any())).thenThrow(new RuntimeException("no translation"));
        HttpRequest<String> request = HttpRequest.GET("/pokemon/translated/mewtwo");
        final HttpResponse<Pokemon> response = client.toBlocking().exchange(request, Pokemon.class);
        assertEquals(new Pokemon("mewtwo", "todo", "rare", true), response.body());
        assertEquals(HttpStatus.OK, response.status());
    }

    @MockBean(PokeApi.class)
    public PokeApi pokeApi() {
        return pokeApiMock;
    }

    @MockBean(FunTranslator.class)
    public FunTranslator funTranslator() {
        return funTranslatorMock;
    }

}