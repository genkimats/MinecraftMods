package com.example.examplemod.RamenMod;

import com.example.examplemod.ExampleMod;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ItemColander extends Item {

    public ItemColander() {
        super(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS)
                .stacksTo(1));
    }

}
