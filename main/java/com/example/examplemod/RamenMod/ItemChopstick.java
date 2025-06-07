package com.example.examplemod.RamenMod;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

public class ItemChopstick extends Item {

    public ItemChopstick() {
        super(new Item.Properties()
                .tab(CreativeModeTab.TAB_TOOLS)
                .stacksTo(1));
    }
}
