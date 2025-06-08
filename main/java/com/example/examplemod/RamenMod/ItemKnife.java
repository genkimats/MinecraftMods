package com.example.examplemod.RamenMod;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

public class ItemKnife extends Item {

    public ItemKnife() {
        super(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS)
                .stacksTo(1));
    }


}
