package com.edge.linden.alter.Items;

import com.edge.linden.alter.OreGeneratorBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class UpgradeItem extends Item {
    public UpgradeItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockEntity blockEntity = level.getBlockEntity(pos);

        if (!level.isClientSide && blockEntity instanceof OreGeneratorBlockEntity generator) {
            generator.incrementUpgradeLevel();
            int speed = generator.getEffectiveTicksPerOre();

            // Уведомление игрока с переводом
            if (context.getPlayer() instanceof ServerPlayer player) {
                player.displayClientMessage(
                        Component.translatable(
                                "message.linden.upgrade_applied",
                                generator.getUpgradeLevel(),
                                speed
                        ).withStyle(ChatFormatting.GREEN),
                        true
                );
            }

            // Потратить предмет, если не в креативе
            if (!context.getPlayer().getAbilities().instabuild) {
                context.getItemInHand().shrink(1);
            }

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }
}
