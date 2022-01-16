package org.mc.pokomonia.controllers;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import jakarta.inject.Inject;
import org.mc.pokomonia.clients.FunTranslator;
import org.mc.pokomonia.clients.PokeApi;
import org.mc.pokomonia.model.Pokemon;


@Controller("/pokemon")
public class PokemonController {

    @Inject
    PokeApi pokeApi;

    @Inject
    FunTranslator funTranslator;

    @Get(value = "{name}")
    public Pokemon get(String name) {
        return pokeApi.getByName(name);
    }

    @Get(value = "translated/{name}")
    public Pokemon translated(String name) {
        Pokemon pokemon = pokeApi.getByName(name);
        try {
            String translatedDescription = funTranslator.translate(pokemon.description(), pokemon.translator());
            return pokemon.cloneWithDescription(translatedDescription);
        } catch (Exception e) {
            return pokemon;
        }
    }

}
