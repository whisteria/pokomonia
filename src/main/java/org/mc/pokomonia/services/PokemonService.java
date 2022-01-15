package org.mc.pokomonia.services;

public interface PokemonService {

    Pokemon getByName(String name);

    record Pokemon(String name, String description, String habitat, boolean isLegendary) {
    }
}
