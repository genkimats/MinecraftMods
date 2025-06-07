package com.example.examplemod.RamenMod;

import com.example.examplemod.ExampleMod;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Random;
import java.util.stream.Stream;

public class BlockRamenBowl extends Block {

    public static final int PHASE_LENGTH = 8;

    public record RamenEffects(MobEffect effect, String effectMessage, String effectName) { }

    public static final RamenEffects[] MAIN_EFFECTS = new RamenEffects[] {
            new RamenEffects(MobEffects.MOVEMENT_SPEED, "「一口目のうまさ」", "移動速度上昇"),
            new RamenEffects(MobEffects.REGENERATION, "「食後の幸福感」", "再生")
    };

    public static final RamenEffects[] LAST_EFFECTS = new RamenEffects[] {
            new RamenEffects(MobEffects.MOVEMENT_SLOWDOWN, "「後悔」", "移動速度低下"),
            new RamenEffects(MobEffects.SATURATION, "「満足」", "満腹度回復"),
    };


    public static final IntegerProperty PHASE = IntegerProperty.create("phase", 0, PHASE_LENGTH - 1);

    public static final VoxelShape SHAPE = Stream.of(
            Block.box(6, 0, 6, 10, 1, 10),
            Block.box(5, 1, 6, 6, 2, 11),
            Block.box(6, 1, 10, 11, 2, 11),
            Block.box(10, 1, 5, 11, 2, 10),
            Block.box(5, 1, 5, 10, 2, 6),
            Block.box(4, 2, 5, 5, 3, 12),
            Block.box(4, 2, 4, 11, 3, 5),
            Block.box(11, 3, 4, 12, 4, 11),
            Block.box(5, 3, 11, 12, 4, 12),
            Block.box(4, 3, 5, 5, 4, 12),
            Block.box(4, 3, 4, 11, 4, 5),
            Block.box(5, 2, 11, 12, 3, 12),
            Block.box(11, 2, 4, 12, 3, 11),
            Block.box(3, 4, 4, 4, 5, 13),
            Block.box(12, 4, 3, 13, 5, 12),
            Block.box(3, 4, 3, 12, 5, 4),
            Block.box(4, 4, 12, 13, 5, 13),
            Block.box(2, 5, 3, 3, 6, 14),
            Block.box(3, 5, 13, 14, 6, 14),
            Block.box(13, 5, 2, 14, 6, 13),
            Block.box(2, 5, 2, 13, 6, 3),
            Block.box(2, 6, 3, 3, 7, 14),
            Block.box(13, 6, 2, 14, 7, 13),
            Block.box(2, 6, 2, 13, 7, 3),
            Block.box(3, 6, 13, 14, 7, 14)
    ).reduce(Shapes.empty(), Shapes::or);

    public BlockRamenBowl() {
        super(BlockBehaviour.Properties.of(Material.STONE).strength(10f)
                .noOcclusion());
        this.registerDefaultState(this.stateDefinition.any().setValue(PHASE, 0));
    }

    @Override
    public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(PHASE);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public void attack(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer) {
        int temp = pState.getValue(PHASE);

        temp++;
        if (temp >= PHASE_LENGTH) {
            temp = 0;
        }

        pLevel.setBlockAndUpdate(pPos, pState.setValue(PHASE, temp));
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        int phase = pState.getValue(PHASE);

        ItemStack itemStack = pPlayer.getItemInHand(pHand);
        Item item = itemStack.getItem();
        if (item == ExampleMod.ITEM_CHOPSTICK && phase > 2) {
            return eat(pLevel, pPos, pState, pPlayer);
        } else if (item == ExampleMod.ITEM_LADLE_SOUP && phase < 1) {
            // スープ入れる処理
            return insertSoup(pHand, pLevel, pPos, pState, pPlayer);

        } else if (item == ExampleMod.ITEM_COLANDER_NOODLE && phase == 1) {
            // 麺入れる処理
            return insertNoodle(pHand, pLevel, pPos, pState, pPlayer);
        } else if (item == ExampleMod.ITEM_RAMEN_INGREDIENT_SET && phase == 2) {
            // 具を入れる処理
            return insertIngredients(pHand, pLevel, pPos, pState, pPlayer);
        }

        return InteractionResult.PASS;
    }

    protected static InteractionResult insertSoup(InteractionHand pHand, LevelAccessor pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
        int phase = pState.getValue(PHASE);
        ItemStack stack = pPlayer.getItemInHand(pHand);

        if (!(stack.getItem() == ExampleMod.ITEM_LADLE_SOUP && phase == 0)) {
            return InteractionResult.PASS;
        }

        ItemStack newStack = new ItemStack(ExampleMod.ITEM_LADLE);
        pPlayer.setItemInHand(pHand, newStack);

        pLevel.setBlock(pPos, pState.setValue(PHASE, 1), 3);
        return InteractionResult.sidedSuccess(pLevel.isClientSide());
    }

    protected static InteractionResult insertNoodle(InteractionHand pHand, LevelAccessor pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
        int phase = pState.getValue(PHASE);
        ItemStack stack = pPlayer.getItemInHand(pHand);

        if (!(stack.getItem() == ExampleMod.ITEM_COLANDER_NOODLE && phase == 1)) {
            return InteractionResult.PASS;
        }

        ItemStack newStack = new ItemStack(ExampleMod.ITEM_COLANDER);
        pPlayer.setItemInHand(pHand, newStack);

        pLevel.setBlock(pPos, pState.setValue(PHASE, 2), 3);
        return InteractionResult.sidedSuccess(pLevel.isClientSide());
    }

    protected static InteractionResult insertIngredients(InteractionHand pHand, LevelAccessor pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
        int phase = pState.getValue(PHASE);
        ItemStack stack = pPlayer.getItemInHand(pHand);

        if (!(stack.getItem() == ExampleMod.ITEM_RAMEN_INGREDIENT_SET && phase == 2)) {
            return InteractionResult.PASS;
        }

        if (stack.getCount() == 1) {
            pPlayer.setItemInHand(pHand, ItemStack.EMPTY);
        } else {
            stack.setCount(stack.getCount() - 1);
        }

        pLevel.setBlock(pPos, pState.setValue(PHASE, 3), 3);
        return InteractionResult.sidedSuccess(pLevel.isClientSide());
    }

    protected static InteractionResult eat(LevelAccessor pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
        int phase = pState.getValue(PHASE);

        // 空腹ではない場合、どんぶりがからもしくは未完成の場合、パス
        if ((!pPlayer.canEat(false)) || (phase < 3)) {
            return InteractionResult.PASS;
        }
        if ((phase + 1) > (PHASE_LENGTH)) {
            return InteractionResult.PASS;
        }

        // 満腹度回復
        pPlayer.getFoodData().eat(4, 0.5f);
        pLevel.gameEvent(pPlayer, GameEvent.EAT, pPos);

        // エフェクト付与
        Random random = new Random();
        MobEffect selectedEffect;
        String selectedEffectMessage;
        String selectedEffectName;
        TextColor textColor;

        if (phase < 7) {
            int randomNumber = random.nextInt(MAIN_EFFECTS.length);
            selectedEffect = MAIN_EFFECTS[randomNumber].effect();
            selectedEffectMessage = MAIN_EFFECTS[randomNumber].effectMessage();
            selectedEffectName = MAIN_EFFECTS[randomNumber].effectName();
            textColor = TextColor.fromRgb(0x00FF00);
        } else {
            int randomNumber = random.nextInt(LAST_EFFECTS.length);
            selectedEffect = LAST_EFFECTS[randomNumber].effect();
            selectedEffectMessage = LAST_EFFECTS[randomNumber].effectMessage();
            selectedEffectName = LAST_EFFECTS[randomNumber].effectName();
            textColor = randomNumber == 0 ? TextColor.fromRgb(0xFF0000) : TextColor.fromRgb(0x00FF00);
        }

        int duration = 1800;
        int potionLevel = 2;

        pPlayer.addEffect(new MobEffectInstance(selectedEffect, duration, potionLevel));
        if (pLevel.isClientSide()) {
            TextComponent effectName = new TextComponent(selectedEffectName);
            Style mainStyle = Style.EMPTY
                    .withColor(textColor)
                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, effectName));

            TextComponent effectMessage = new TextComponent(selectedEffectMessage);
            effectMessage.setStyle(mainStyle);

            pPlayer.sendMessage(effectMessage, pPlayer.getUUID());

        }


        // テクスチャーの変化（state)
        if ((phase + 1) < PHASE_LENGTH) {
            phase++;
        } else {
            phase = 0;
        }
        pLevel.setBlock(pPos, pState.setValue(PHASE, phase), 3);


        return InteractionResult.SUCCESS;
    }


}
