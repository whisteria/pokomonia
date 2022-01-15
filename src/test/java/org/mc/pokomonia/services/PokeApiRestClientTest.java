package org.mc.pokomonia.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mc.pokomonia.model.Pokemon;

import java.io.FileReader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mc.pokomonia.services.PokeApiRestClient.*;


class PokeApiRestClientTest {

    @Test
    void getPokemon() throws Exception {
        try (FileReader fileReader = new FileReader("data/pokemon-species.json")) {
            JsonNode jsonNode = new ObjectMapper().readTree(fileReader);
            // todo todo
            Pokemon expected = new Pokemon("mewtwo", "todo", "rare", true);
            assertEquals(expected, toPokemon("mewtwo", jsonNode));
        }
    }
}