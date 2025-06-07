package com.example.examplemod.RamenMod;

import com.example.examplemod.ExampleMod;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.commands.PublishCommand;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import java.util.Random;

public class ItemColanderNoodle extends Item {

    public static final String FINISHED = "Finished";

    public static final String[] FAIL_MESSAGES = {
            "「うーん、もうちょっとなんだよなー」",
            "「まだまだ修行が足りないか…」",
            "「ちょっと油断したかも」",
            "「湯気で手元が…くっ！」",
            "「全然決まらなかった！」",
            "「今日は調子悪いな…」",
            "「疲れてきた…集中しないと」",
            "「え、今のダメなの！？」",
            "「くそっ、今ので決めたかったのに…」",
            "「惜しい…ほんと惜しい…！」",
            "「今の完璧だったと思ったのに…！」",
            "「うわっ、全部いっちゃった…」",
            "「くぅ〜、またやっちゃった…！」",
            "「あとコンマ1秒早ければ…！」",
            "「全然ダメじゃん、オレ…」",
            "「いやいや、これはバグでは！？」",
            "「ちょっと、重心がズレたかも」",
            "「油断したら、こうなるんだよな〜」",
            "「……湯切りって奥が深い」",
            "「この湯、絶対わざと暴れてるだろ」"
    };

    public static final String[] SUCCESS_MESSAGES = {
            "「よし、完璧！」",
            "「決まった！気持ちいい〜」",
            "「これぞ職人技ってやつ！」",
            "「来た！これがオレの湯切り！」"
    };

    public ItemColanderNoodle() {
        super(new Properties().tab(CreativeModeTab.TAB_TOOLS));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {

        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
        CompoundTag tag = stack.getOrCreateTag();

        // Read the current value or default to 0 if not set
        boolean isFinished = tag.getBoolean(FINISHED);

        if (!isFinished) {
            Random random = new Random();
            int randomNumber = random.nextInt(20);
            TextColor textColor;
            String message;

            if (randomNumber == 0) {
                int message_idx = random.nextInt(SUCCESS_MESSAGES.length);
                message = SUCCESS_MESSAGES[message_idx];
                textColor = TextColor.fromRgb(0x00FF00);
                tag.putBoolean(FINISHED, true);
            } else {
                int message_idx = random.nextInt(FAIL_MESSAGES.length);
                message = FAIL_MESSAGES[message_idx];
                textColor = TextColor.fromRgb(0xD3D3D3);
            }


            if (pLevel.isClientSide()) {
                TextComponent effectMessage = new TextComponent(message);
                effectMessage.setStyle(Style.EMPTY.withColor(textColor));

                pPlayer.sendMessage(effectMessage, pPlayer.getUUID());
            }
        }

        return InteractionResultHolder.sidedSuccess(stack, pLevel.isClientSide());
    }

//    @Override
//    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
//        boolean charged = stack.getOrCreateTag().getBoolean("Charged");
//        tooltip.add(Component.literal("Charged: " + charged)
//                .withStyle(charged ? ChatFormatting.GREEN : ChatFormatting.RED));
//    }
}
