package com.example.examplemod.mc_05_mysword;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Blocks;
import org.lwjgl.system.CallbackI;

public class ItemMySword extends SwordItem {
    public ItemMySword() {
        super(Tiers.IRON,
                10,
                -2.4F,
                (new Item.Properties()).tab(CreativeModeTab.TAB_COMBAT));
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker instanceof Player) {
            target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 1200, 0));
            target.setRemainingFireTicks(1200);
            target.setPos(target.position().x,
                    target.position().y + 10,
                                target.position().z);
            BlockPos pos = new BlockPos(target.getX(), target.getY(), target.getZ());
            target.level.setBlockAndUpdate(pos.offset(1, 1, 0), Blocks.GLASS.defaultBlockState());
            target.level.setBlockAndUpdate(pos.offset(0, 1, 1), Blocks.GLASS.defaultBlockState());
            target.level.setBlockAndUpdate(pos.offset(-1, 1, 0), Blocks.GLASS.defaultBlockState());
            target.level.setBlockAndUpdate(pos.offset(0, 1, -1), Blocks.GLASS.defaultBlockState());
        }
        return super.hurtEnemy(stack, target, attacker);
    }

}
