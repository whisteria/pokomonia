package org.mc.pokomonia.controllers;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

@Controller("/pokemon")
public class PokemonController {

    record Pokemon(String name, String description, String habitat, boolean isLegendary) {
    }

    @Get(value = "{name}")
    public Pokemon get(String name) {
        return new Pokemon(name, "description", "habitat", true);
    }

}
