package com.example.examplemod.RamenMod;

import net.minecraft.util.StringRepresentable;

public enum HotPotMode implements StringRepresentable {
    EMPTY("empty"),
    WATER("water"),
    BOIL("boil"),
    NOODLE("noodle"),
    SOUP_BEGINNING("soup_beginning"),
    SOUP_DONE("soup_done");

    private final String modeName;

    HotPotMode(String modeName) {
        this.modeName = modeName;
    }

    @Override
    public String getSerializedName() {
        return modeName;
    }

    @Override
    public String toString() {
        return modeName;
    }
}
