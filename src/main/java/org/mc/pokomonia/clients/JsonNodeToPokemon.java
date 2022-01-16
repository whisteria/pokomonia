package org.mc.pokomonia.clients;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.mc.pokomonia.model.Pokemon;

import java.util.Iterator;

/**
 * not a client but a stateless helper class to serialize the Pokemon
 */
public class JsonNodeToPokemon {

    public static class PokemonParseException extends RuntimeException {

        public PokemonParseException(String message) {
            super(message);
        }
    }

    private final static JsonPointer habitatPath = JsonPointer.compile("/habitat/name");
    private final static JsonPointer flavorTextEntriesPath = JsonPointer.compile("/flavor_text_entries");
    private final static JsonPointer isLegendaryPath = JsonPointer.compile("/is_legendary");
    private final static JsonPointer languageNamePath = JsonPointer.compile("/language/name");
    private final static JsonPointer flavorTextPath = JsonPointer.compile("/flavor_text");

    static Pokemon toPokemon(String name, JsonNode jsonNode) {
        return new Pokemon(
                name,
                getDescription(jsonNode),
                fromPath(jsonNode, habitatPath).asText(),
                fromPath(jsonNode, isLegendaryPath).asBoolean()
        );
    }

    // visible for testing
    @SuppressWarnings("ConstantConditions")
    static String getDescription(JsonNode jsonNode) {
        String descriptionDirty = firstFlavorText(jsonNode.requiredAt(flavorTextEntriesPath), "en");
        return normalize(descriptionDirty);
    }

    // visible for testing
    static String firstFlavorText(JsonNode flavorTextEntries, String languageName) {
        if (flavorTextEntries instanceof ArrayNode arrayNode) {
            for (Iterator<JsonNode> it = arrayNode.elements(); it.hasNext(); ) {
                JsonNode flavorTextNode = it.next();
                String language = flavorTextNode.requiredAt(languageNamePath).asText();
                if (languageName.equals(language)) {
                    return flavorTextNode.requiredAt(flavorTextPath).asText();
                }
            }
            throw new PokemonParseException("no flavor text found for " + languageName + " for the description");

        } else {
            String simpleName = flavorTextEntries.getClass().getSimpleName();
            String message = "unexpected node class for flavor_text_entries: " + simpleName;
            throw new PokemonParseException(message);
        }
    }

    // possibly a proper json path library is nicer
    static JsonNode fromPath(JsonNode node, JsonPointer pointer) {
        JsonNode at = node.at(pointer);
        if (at.isMissingNode()) {
            throw new PokemonParseException("field not found from path " + pointer);
        }
        return at;
    }

    // visible for testing
    static String normalize(String text) {
        return text.replaceAll("\\s+", " ").trim();
    }
}
