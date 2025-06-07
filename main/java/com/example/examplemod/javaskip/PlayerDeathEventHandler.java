package com.example.examplemod.javaskip;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PlayerDeathEventHandler {

    // 所持中の間は不死になるアイテムを定義
    private static final Item NOT_DIE_ITEM = Items.BONE;

    @SubscribeEvent
    // プレイヤーがダメージを受けた時のイベントの中身を編集して不死になる
    public void onPlayerHurt(LivingHurtEvent event) {
        // もしダメージが発生した世界がクライアント（Render）だったら無視
        if (event.getEntityLiving().level.isClientSide) {
            return;
        }
        // ダメージを受けたエンティティがプレイヤーじゃなければ無視
        if (!(event.getEntityLiving() instanceof Player)) {
            return;
        }
        // 死なない程度のダメージならそのダメージ量を受ける
        if (!(event.getEntityLiving().getHealth() - event.getAmount() <= 0)) {
            return;
        }

        // プレイヤーのインベントリー情報を取得する
        Inventory inventory = ((Player) event.getEntityLiving()).getInventory();

        //　インベントリーのアイテムを一個一個確認
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            // インベントリー内のアイテム情報を取得
            ItemStack stack = inventory.getItem(i);

            //　何もなかったら無視
            if (stack == null) {
                continue;
            }

            //　不死指定アイテムじゃなかったら無視
            if (stack.getItem() != NOT_DIE_ITEM) {
                continue;
            }

            //　一つ消費する（不死指定アイテム）
            if (stack.getCount() == 1) {
                inventory.removeItemNoUpdate(i);
            } else {
                stack.setCount(stack.getCount() - 1);
            }
            event.setCanceled(true);
            return;
        }
    }
}