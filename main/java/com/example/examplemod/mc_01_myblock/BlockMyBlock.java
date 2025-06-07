package com.example.examplemod.mc_01_myblock;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;


public class BlockMyBlock extends Block {
    public BlockMyBlock(){
        super(Block.Properties.of(Material.DIRT));
    }

    @Override
    public void attack(BlockState pState, Level level, BlockPos pPos, Player pPlayer) {
        super.attack(pState, level, pPos, pPlayer);
        LivingEntity entityMob = new Pig(EntityType.PIG, level);
        entityMob.setPos(pPos.getX(), pPos.getY(), pPos.getZ());
        if (!level.isClientSide) {
            ((ServerLevel) level).tryAddFreshEntityWithPassengers(entityMob);
        }
    }
}
