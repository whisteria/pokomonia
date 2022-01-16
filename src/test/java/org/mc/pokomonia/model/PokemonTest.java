package org.mc.pokomonia.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PokemonTest {

    @Test
    void cloneWithDescription() {
        Pokemon pokemon = new Pokemon("mewtwo", "todo", "rare", true);
        assertEquals(new Pokemon("mewtwo", "translated", "rare", true), pokemon.cloneWithDescription("translated"));
    }

    @Test
    void translatorYoda() {
        assertEquals("yoda", new Pokemon("mewtwo", "todo", "cave", false).translator());
        assertEquals("yoda", new Pokemon("mewtwo", "todo", "rare", true).translator());
    }

    @Test
    void translatorShakespeare() {
        assertEquals("shakespeare", new Pokemon("mewtwo", "todo", "rare", false).translator());
    }
}