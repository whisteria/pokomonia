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
import org.mc.pokomonia.clients.pokeapi.PokeApi;
import org.mc.pokomonia.clients.translate.FunTranslator;
import org.mc.pokomonia.model.Pokemon;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@MicronautTest
class PokemonControllerTest {

    @Inject
    @Client("/")
    HttpClient client;

    private final PokeApi pokeApiMock = mock(PokeApi.class);
    private final FunTranslator funTranslatorMock = mock(FunTranslator.class);

    @Test
    public void pokemon() {
        Pokemon expected = new Pokemon("mewtwo", "todo", "rare", true);
        // mock
        when(pokeApiMock.getByName(any())).thenReturn(expected);

        // execute
        HttpRequest<String> request = HttpRequest.GET("/pokemon/mewtwo");
        HttpResponse<Pokemon> response = client.toBlocking().exchange(request, Pokemon.class);

        // assert
        assertEquals(expected, response.body());
        assertEquals(HttpStatus.OK, response.status());
        verify(pokeApiMock, times(1)).getByName("mewtwo");
        verify(funTranslatorMock, times(0)).translate(any(), any());
    }

    @Test
    public void pokemonFailureReturns500() {
        // mock
        when(pokeApiMock.getByName(any())).thenThrow(new IllegalStateException("error"));

        // execute
        HttpRequest<String> request = HttpRequest.GET("/pokemon/dummy");
        HttpClientResponseException e = assertThrows(
                HttpClientResponseException.class,
                () -> client.toBlocking().exchange(request)
        );

        // assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, e.getStatus());
        verify(pokeApiMock, times(1)).getByName("dummy");
        verify(funTranslatorMock, times(0)).translate(any(), any());
    }

    @Test
    public void pokemonNotFoundReturns400() {
        // mock
        HttpClientResponseException httpClientResponseException = mock(HttpClientResponseException.class);
        when(httpClientResponseException.getStatus()).thenReturn(HttpStatus.NOT_FOUND);
        when(pokeApiMock.getByName(any())).thenThrow(httpClientResponseException);

        // execute
        HttpRequest<String> request = HttpRequest.GET("/pokemon/hello");
        HttpClientResponseException e = assertThrows(
                HttpClientResponseException.class,
                () -> client.toBlocking().exchange(request)
        );

        // assert
        assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
        verify(pokeApiMock, times(1)).getByName("hello");
        verify(funTranslatorMock, times(0)).translate(any(), any());
    }

    // translated
    @Test
    public void translated() {
        Pokemon pokemon = new Pokemon("mewtwo", "todo", "rare", true);
        //mock
        when(pokeApiMock.getByName(any())).thenReturn(pokemon);
        when(funTranslatorMock.translate(any(), any())).thenReturn("my translation");

        // execute
        HttpRequest<String> request = HttpRequest.GET("/pokemon/translated/mewtwo");
        HttpResponse<Pokemon> response = client.toBlocking().exchange(request, Pokemon.class);

        // assert
        assertEquals(new Pokemon("mewtwo", "my translation", "rare", true), response.body());
        assertEquals(HttpStatus.OK, response.status());
        verify(pokeApiMock, times(1)).getByName("mewtwo");
        verify(funTranslatorMock, times(1)).translate("todo", "yoda");
    }

    @Test
    public void translationFailsStill200() {
        Pokemon pokemon = new Pokemon("mewtwo", "todo", "rare", false);
        // mock
        when(pokeApiMock.getByName(any())).thenReturn(pokemon);
        when(funTranslatorMock.translate(any(), any())).thenThrow(new RuntimeException("no translation"));

        // execute
        HttpRequest<String> request = HttpRequest.GET("/pokemon/translated/mewtwo");
        HttpResponse<Pokemon> response = client.toBlocking().exchange(request, Pokemon.class);

        // assert
        assertEquals(new Pokemon("mewtwo", "todo", "rare", false), response.body());
        assertEquals(HttpStatus.OK, response.status());
        verify(pokeApiMock, times(1)).getByName("mewtwo");
        verify(funTranslatorMock, times(1)).translate("todo", "shakespeare");
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