package org.mc.pokomonia.controllers;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import jakarta.inject.Inject;
import org.mc.pokomonia.services.PokeApiRestClient;

import static org.mc.pokomonia.services.PokeApiRestClient.Pokemon;


@Controller("/pokemon")
public class PokemonController {

    @Inject
    PokeApiRestClient pokeApiRestClient;

    @Get(value = "{name}")
    public Pokemon get(String name) {
        return pokeApiRestClient.getByName(name);
    }

}
