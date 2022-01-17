package org.mc.pokomonia.clients.translate;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mc.pokomonia.TestSupport.jsonNodeFromString;

class FunTranslatorTest {

    @Test
    void getTranslation() throws Exception {
        String jsonString = """
                {
                    "success": {
                        "total": 1
                    },
                    "contents": {
                        "translated": "Created by a scientist after years of horrific gene splicing and dna engineering experiments,  it was.",
                        "text": "It was created by a scientist after years of horrific gene splicing and DNA engineering experiments.",
                        "translation": "yoda"
                    }
                }
                """;
        JsonNode jsonNode = jsonNodeFromString(jsonString);
        assertEquals(
                "Created by a scientist after years of horrific gene splicing and dna engineering experiments,  it was.",
                FunTranslator.getTranslation(jsonNode)
        );
    }
}