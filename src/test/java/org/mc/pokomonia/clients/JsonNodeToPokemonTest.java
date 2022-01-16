package org.mc.pokomonia.clients;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;
import org.mc.pokomonia.model.Pokemon;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mc.pokomonia.TestSupport.jsonNodeFromFile;
import static org.mc.pokomonia.TestSupport.jsonNodeFromString;
import static org.mc.pokomonia.clients.JsonNodeToPokemon.PokemonParseException;


class JsonNodeToPokemonTest {

    @Test
    void toPokemon() throws Exception {
        JsonNode jsonNode = jsonNodeFromFile("pokemon-species.json");
        Pokemon expected = new Pokemon(
                "mewtwo",
                "It was created by a scientist after years of horrific gene splicing and DNA engineering experiments.",
                "rare",
                true
        );
        assertEquals(expected, JsonNodeToPokemon.toPokemon("mewtwo", jsonNode));
    }

    @Test
    void firstFlavorText() throws Exception {
        JsonNode jsonNode = jsonNodeFromFile("flavor_text_entries.json");
        assertEquals("correct result for the test", JsonNodeToPokemon.firstFlavorText(jsonNode, "en"));
        assertEquals("sample text for not-en", JsonNodeToPokemon.firstFlavorText(jsonNode, "not-en"));
    }

    @Test
    void normalize() {
        assertEquals("this should be really ok", JsonNodeToPokemon.normalize("this\tshould\n be \nreally\fok  "));
        assertEquals("this should not change", JsonNodeToPokemon.normalize("this should not change"));
    }

    @Test
    void habitatMissing() throws Exception {
        // other fields can be tested in same way
        String jsonString = """
                {
                  "name": "mewtwo",
                  "is_legendary": true,
                  "flavor_text_entries": [
                    {
                      "flavor_text": "here the description",
                      "language": {
                        "name": "en"
                      }
                    }
                  ]
                }
                """;
        JsonNode jsonNode = jsonNodeFromString(jsonString);
        PokemonParseException e = assertThrows(
                PokemonParseException.class,
                () -> JsonNodeToPokemon.toPokemon("mewtwo", jsonNode)
        );
        assertEquals("field not found from path /habitat/name", e.getMessage());
    }

    @Test
    void descriptionMissing() throws Exception {
        String jsonString = """
                {
                  "name": "mewtwo",
                  "habitat": {
                    "name": "rare"
                  },
                  "is_legendary": true,
                  "flavor_text_entries": [
                    {
                      "flavor_text": "here the description",
                      "language": {
                        "name": "not-en"
                      }
                    }
                  ]
                }
                                """;
        JsonNode jsonNode = jsonNodeFromString(jsonString);
        PokemonParseException e = assertThrows(
                PokemonParseException.class,
                () -> JsonNodeToPokemon.toPokemon("mewtwo", jsonNode)
        );
        assertEquals("no flavor text found for en for the description", e.getMessage());
    }

}