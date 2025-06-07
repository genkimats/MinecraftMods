package com.example.examplemod.RamenMod;

import com.example.examplemod.ExampleMod;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ItemLadleSoup extends Item {

    public ItemLadleSoup() {
        super(new Item.Properties()
                .tab(CreativeModeTab.TAB_TOOLS)
                .stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);

        if (!pLevel.isClientSide) {
            ItemStack newStack = new ItemStack(ExampleMod.ITEM_LADLE);
            pPlayer.setItemInHand(pUsedHand, newStack);
        }

        return InteractionResultHolder.sidedSuccess(stack, pLevel.isClientSide());
    }
}
