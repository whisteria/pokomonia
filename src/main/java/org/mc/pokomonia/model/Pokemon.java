package org.mc.pokomonia.model;

public record Pokemon(String name, String description, String habitat, boolean isLegendary) {
    public String translator() {
        boolean needsYoda = "cave".equals(habitat) || isLegendary;
        return needsYoda ? "yoda" : "shakespeare";
    }

    public Pokemon cloneWithDescription(String translatedDescription) {
        return new Pokemon(name, translatedDescription, habitat, isLegendary);
    }
}
