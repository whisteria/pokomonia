package org.mc.pokomonia.controllers;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import jakarta.inject.Inject;
import org.mc.pokomonia.services.PokemonService;

import static org.mc.pokomonia.services.PokemonService.*;

@Controller("/pokemon")
public class PokemonController {

    @Inject
    PokemonService pokemonService;

    @Get(value = "{name}")
    public Pokemon get(String name) {
        return pokemonService.getByName(name);
    }

}
