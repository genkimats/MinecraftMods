package com.example.examplemod.RamenMod;

import net.minecraft.util.StringRepresentable;

public enum CuttingBoardMode implements StringRepresentable {
    NONE("none"),
    NOODLE("noodle"),
    GREEN_ONION("green_onion");

    private final String modeName;

    CuttingBoardMode(String modeName) {
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
