package org.mc.pokomonia.controllers;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import jakarta.inject.Inject;
import org.mc.pokomonia.model.Pokemon;
import org.mc.pokomonia.services.FunTranslationsClient;
import org.mc.pokomonia.services.PokeApiRestClient;


@Controller("/pokemon")
public class PokemonController {

    @Inject
    PokeApiRestClient pokeApiRestClient;

    @Inject
    FunTranslationsClient funTranslationsClient;

    @Get(value = "{name}")
    public Pokemon get(String name) {
        return pokeApiRestClient.getByName(name);
    }

    @Get(value = "translated/{name}")
    public Pokemon translated(String name) {
        Pokemon pokemon = pokeApiRestClient.getByName(name);
        try {
            String translatedDescription = funTranslationsClient.translate(pokemon.description(), pokemon.translator());
            return pokemon.cloneWithDescription(translatedDescription);
        } catch (Exception e) {
            return pokemon;
        }
    }

}
