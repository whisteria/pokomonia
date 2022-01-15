package org.mc.pokomonia;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileReader;

public class TestSupport {

    public static JsonNode getDataJsonNode(String fileName) throws Exception {
        try (FileReader fileReader = new FileReader("data/" + fileName)) {
            return new ObjectMapper().readTree(fileReader);
        }
    }

}
