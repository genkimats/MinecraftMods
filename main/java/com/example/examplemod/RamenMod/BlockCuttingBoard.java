package com.example.examplemod.RamenMod;

import com.example.examplemod.ExampleMod;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockCuttingBoard extends Block {

    public static final VoxelShape SHAPE = Shapes.or(
            Block.box(3, 0, 1, 13, 1, 13),
            Block.box(10, 0, 13, 13, 1, 15),
            Block.box(3, 0, 13, 6, 1, 15),
            Block.box(6, 0, 14, 10, 1, 15)
    );


    public static final int MAX_CUTS = 6;

    public static final IntegerProperty CUTS = IntegerProperty.create("cuts", 0, MAX_CUTS);
    public static final EnumProperty<CuttingBoardMode> MODE = EnumProperty.create("mode", CuttingBoardMode.class);


    public BlockCuttingBoard() {
        super(BlockBehaviour.Properties.of(Material.STONE).strength(10f));
        this.registerDefaultState(this.stateDefinition.any().setValue(CUTS, 0));
        this.registerDefaultState(this.stateDefinition.any().setValue(MODE, CuttingBoardMode.NONE));
    }

    @Override
    public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(CUTS);
        builder.add(MODE);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        int cuts = pState.getValue(CUTS);
        CuttingBoardMode mode = pState.getValue(MODE);

        ItemStack itemStack = pPlayer.getItemInHand(pHand);
        Item item = itemStack.getItem();

        if (item == ExampleMod.ITEM_KNIFE && mode != CuttingBoardMode.NONE && cuts < MAX_CUTS) {
            return cut(pState, pLevel, pPos);
        }
        if (item == ExampleMod.ITEM_NOODLE_DOUGH && mode == CuttingBoardMode.NONE) {
            return putNoodleDough(pState, pLevel, pPos, pPlayer, pHand);
        }
        if (mode != CuttingBoardMode.NONE && cuts == MAX_CUTS) {
            return takeNoodle(pState, pLevel, pPos, pPlayer, pHand);
        }

        return InteractionResult.PASS;
    }

    protected InteractionResult cut(BlockState pState, Level pLevel, BlockPos pPos) {
        int cuts = pState.getValue(CUTS);
        CuttingBoardMode mode = pState.getValue(MODE);

        if (cuts >= MAX_CUTS || mode == CuttingBoardMode.NONE) {
            return InteractionResult.PASS;
        }

        cuts++;

        pLevel.setBlock(pPos, pState.setValue(CUTS, cuts), 3);
        return InteractionResult.SUCCESS;
    }

    protected InteractionResult putNoodleDough(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand) {
        CuttingBoardMode mode = pState.getValue(MODE);
        ItemStack itemStack = pPlayer.getItemInHand(pHand);
        Item item = itemStack.getItem();

        if (mode != CuttingBoardMode.NONE || item != ExampleMod.ITEM_NOODLE_DOUGH) {
            return InteractionResult.PASS;
        }

        if (itemStack.getCount() == 1) {
            pPlayer.setItemInHand(pHand, ItemStack.EMPTY);
        } else {
            itemStack.setCount(itemStack.getCount() - 1);
        }

        pLevel.setBlock(pPos, pState.setValue(MODE, CuttingBoardMode.NOODLE), 3);

        return InteractionResult.SUCCESS;
    }

    protected InteractionResult takeNoodle(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand) {
        CuttingBoardMode mode = pState.getValue(MODE);
        int cuts = pState.getValue(CUTS);

        if (mode != CuttingBoardMode.NOODLE || cuts != MAX_CUTS) {
            return InteractionResult.PASS;
        }

        pLevel.setBlock(pPos, pState.setValue(MODE, CuttingBoardMode.NONE).setValue(CUTS, 0), 3);

        if (!pLevel.isClientSide) {
            ItemStack noodle = new ItemStack(ExampleMod.ITEM_NOODLE);
            ItemEntity dropped = new ItemEntity(pLevel,
                    pPos.getX() + 0.5, pPos.getY() + 0.5, pPos.getZ() + 0.5, noodle);
            pLevel.addFreshEntity(dropped);
        }
        return InteractionResult.SUCCESS;
    }
}
