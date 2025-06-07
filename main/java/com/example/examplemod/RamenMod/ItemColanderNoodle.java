package com.example.examplemod.RamenMod;

import com.example.examplemod.ExampleMod;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class ItemColanderNoodle extends Item {

    public ItemColanderNoodle() {
        super(new Properties().tab(CreativeModeTab.TAB_TOOLS));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);

        if (!pLevel.isClientSide) {
            // Create a new item to replace the current one
            ItemStack newStack = new ItemStack(ExampleMod.ITEM_COLANDER);

            // Replace the item in the player's hand
            pPlayer.setItemInHand(pUsedHand, newStack);
        }

        return InteractionResultHolder.sidedSuccess(stack, pLevel.isClientSide());
    }
}
