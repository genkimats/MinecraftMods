package com.example.examplemod.RamenMod;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

public class ItemLadle extends Item {

    public ItemLadle() {
        super(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS)
                .stacksTo(1));
    }

}
