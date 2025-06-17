package com.example.examplemod.RamenMod;

import com.example.examplemod.ExampleMod;
import com.ibm.icu.impl.locale.ParseStatus;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
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

import java.util.Random;
import java.util.stream.Stream;

public class BlockCookingPot extends Block {

    public static final VoxelShape SHAPE = Stream.of(
            Block.box(7, 14, 0, 9, 15, 1),
            Block.box(2, 0, 2, 14, 1, 14),
            Block.box(3, 1, 2, 14, 15, 3),
            Block.box(2, 1, 13, 13, 15, 14),
            Block.box(2, 1, 2, 3, 15, 13),
            Block.box(13, 1, 3, 14, 15, 14),
            Block.box(6, 14, 14, 7, 15, 16),
            Block.box(9, 14, 14, 10, 15, 16),
            Block.box(7, 14, 15, 9, 15, 16),
            Block.box(6, 14, 0, 7, 15, 2),
            Block.box(9, 14, 0, 10, 15, 2)
    ).reduce(Shapes.empty(), Shapes::or);


    public static final int TEMPS = 6;
    public static final int SOUP_DONE_TICKS = 1000;

    public static final IntegerProperty TEMP = IntegerProperty.create("temp", 0, TEMPS - 1);
    public static final EnumProperty<HotPotMode> MODE = EnumProperty.create("mode", HotPotMode.class);

    public BlockCookingPot() {
        super(BlockBehaviour.Properties.of(Material.STONE).strength(10f));
        this.registerDefaultState(this.stateDefinition.any().setValue(TEMP, 0));
        this.registerDefaultState(this.stateDefinition.any().setValue(MODE, HotPotMode.EMPTY));
    }

    @Override
    public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(TEMP);
        builder.add(MODE);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, Random random) {
        HotPotMode mode = state.getValue(MODE);

        if (mode == HotPotMode.BOIL || mode == HotPotMode.SOUP_BEGINNING || mode == HotPotMode.SOUP_DONE) {
//            level.addParticle(ParticleTypes.SNOWFLAKE,
//                    (double) pos.getX() + 0.5D, (double) pos.getY() + 1.1D, (double) pos.getZ() + 0.5D,
//                    0.0D, 0.03D, 0.0D);
            level.addParticle(ParticleTypes.CLOUD,
                    pos.getX() + 0.5,
                    pos.getY() + 1.0,
                    pos.getZ() + 0.5,
                    0.0, 0.05, 0.0);
        }
    }



    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        HotPotMode mode = pState.getValue(MODE);

        ItemStack itemStack = pPlayer.getItemInHand(pHand);
        Item item = itemStack.getItem();

        // 水を入れる処理（下に火があれば沸騰）
        if (item == Items.WATER_BUCKET && mode == HotPotMode.EMPTY) {
            if (!pPlayer.isCreative()) {
                ItemStack newStack = new ItemStack(Items.BUCKET);
                pPlayer.setItemInHand(pHand, newStack);
            }
            if (isFireBelow(pState, pLevel, pPos)) {
                pLevel.setBlock(pPos, pState.setValue(MODE, HotPotMode.BOIL), 3);
            } else {
                pLevel.setBlock(pPos, pState.setValue(MODE, HotPotMode.WATER), 3);
            }
            return InteractionResult.SUCCESS;
        }

        // スープにする処理
        if (item == Items.BONE && mode == HotPotMode.BOIL) {
            if (itemStack.getCount() == 1) {
                pPlayer.setItemInHand(pHand, ItemStack.EMPTY);
            } else {
                itemStack.setCount(itemStack.getCount() - 1);
            }
            pLevel.setBlock(pPos, pState.setValue(MODE, HotPotMode.SOUP_BEGINNING), 3);
            pLevel.scheduleTick(pPos, this, SOUP_DONE_TICKS);
            return InteractionResult.SUCCESS;
        }

        // 麺を入れる処理
        if (item == ExampleMod.ITEM_NOODLE && mode == HotPotMode.BOIL) {
            if (itemStack.getCount() == 1) {
                pPlayer.setItemInHand(pHand, ItemStack.EMPTY);
            } else {
                itemStack.setCount(itemStack.getCount() - 1);
            }
            pLevel.setBlock(pPos, pState.setValue(MODE, HotPotMode.NOODLE), 3);
            return InteractionResult.SUCCESS;
        }

        // スープを取る処理
        if (item == ExampleMod.ITEM_LADLE && mode == HotPotMode.SOUP_DONE) {
            ItemStack newStack = new ItemStack(ExampleMod.ITEM_LADLE_SOUP);
            pPlayer.setItemInHand(pHand, newStack);

            pLevel.setBlock(pPos, pState.setValue(MODE, HotPotMode.EMPTY), 3);

            return InteractionResult.SUCCESS;
        }

        // 麺を取る処理
        if (item == ExampleMod.ITEM_COLANDER && mode == HotPotMode.NOODLE) {
            ItemStack newStack = new ItemStack(ExampleMod.ITEM_COLANDER_NOODLE);
            pPlayer.setItemInHand(pHand, newStack);

            pLevel.setBlock(pPos, pState.setValue(MODE, HotPotMode.BOIL), 3);

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, Random pRandom) {
        HotPotMode mode = pState.getValue(MODE);

        if (mode == HotPotMode.WATER) {
            pLevel.setBlock(pPos, pState.setValue(MODE, HotPotMode.BOIL), 3);
        } else if (mode == HotPotMode.SOUP_BEGINNING) {
            pLevel.setBlock(pPos, pState.setValue(MODE, HotPotMode.SOUP_DONE), 3);
        }
    }

    @Override
    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {
        HotPotMode mode = pState.getValue(MODE);

        if (isFireBelow(pState, pLevel, pPos) && (mode == HotPotMode.WATER)) {
            pLevel.setBlock(pPos, pState.setValue(MODE, HotPotMode.BOIL), 3);
        } else if (!isFireBelow(pState, pLevel, pPos) && (mode == HotPotMode.BOIL)) {
            pLevel.setBlock(pPos, pState.setValue(MODE, HotPotMode.WATER), 3);
        }
    }

    protected Boolean isFireBelow(BlockState pState, Level pLevel, BlockPos pPos) {
        BlockPos below = pPos.below();
        BlockState belowState = pLevel.getBlockState(below);

        return belowState.is(Blocks.FIRE);
    }
}