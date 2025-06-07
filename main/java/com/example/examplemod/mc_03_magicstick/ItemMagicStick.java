package com.example.examplemod.mc_03_magicstick;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ItemMagicStick extends Item {
    public ItemMagicStick() {
        super(new Item.Properties().tab(CreativeModeTab.TAB_COMBAT));
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        Level level = pTarget.level;
        BlockPos spawnPos = pTarget.blockPosition();

        LivingEntity entity;
        if (pTarget instanceof Villager) {
            entity = new Zombie(level);
        } else {
            entity = new Pig(EntityType.PIG, level);
        }

        entity.setPos(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());

        if (!pTarget.level.isClientSide) {
            ServerLevel serverLevel = (ServerLevel) pTarget.level;
            serverLevel.tryAddFreshEntityWithPassengers(entity);
            serverLevel.removeEntity(pTarget);
        }
        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }
}
