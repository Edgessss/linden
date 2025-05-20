package com.edge.linden.avaritia.items;

import com.edge.linden.avaritia.alter.DemonicDamageSource;
import com.edge.linden.avaritia.alter.DemonicTier;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class DemonicSword extends SwordItem {


    public DemonicSword(Properties properties) {
        super(new DemonicTier(), 3, -2.4F, properties);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        return slot == EquipmentSlot.MAINHAND
                ? ImmutableMultimap.of()
                : super.getDefaultAttributeModifiers(slot);
    }


    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("§cУрон: Бесконечный"));
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker instanceof Player player) {
            DamageSource damageSource = DemonicDamageSource.create(player);
            target.hurt(damageSource, Float.MAX_VALUE);
            return true;
        }
        return false;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isFireResistant() {
        return true;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        if (!stack.isEnchanted() && entity instanceof Player) {
            stack.enchant(Enchantments.MOB_LOOTING, 20);
        }
        super.inventoryTick(stack, level, entity, slot, selected);
    }
}
