package org.mc.pokomonia;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileReader;
import java.io.StringReader;

public class TestSupport {

    public static JsonNode jsonNodeFromFile(String fileName) throws Exception {
        try (FileReader reader = new FileReader("data/" + fileName)) {
            return new ObjectMapper().readTree(reader);
        }
    }

    public static JsonNode jsonNodeFromString(String jsonString) throws Exception {
        try (StringReader reader = new StringReader(jsonString)) {
            return new ObjectMapper().readTree(reader);
        }
    }

}
