package com.edge.linden.avaritia.alter;

import com.edge.linden.avaritia.items.DemonicSword;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LootingLevelEvent;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "linden")
public class DemonicLootingHandler {

    @SubscribeEvent
    public static void onLootingLevel(LootingLevelEvent event) {
        if (event.getEntity() instanceof Player player) {
            ItemStack held = player.getMainHandItem();
            if (held.getItem() instanceof DemonicSword) {
                event.setLootingLevel(20); // Устанавливаем добычу 20
            }
        }
    }
}
