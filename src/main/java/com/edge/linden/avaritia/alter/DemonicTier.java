package com.edge.linden.avaritia.alter;

import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

public class DemonicTier implements Tier {

    @Override
    public int getUses() {
        return Integer.MAX_VALUE; // Не ломается
    }

    @Override
    public float getSpeed() {
        return 100.0F; // Быстрая атака
    }

    @Override
    public float getAttackDamageBonus() {
        return Float.MAX_VALUE; // Отображаемый урон в инвентаре
    }

    @Override
    public int getLevel() {
        return 5; // Уровень для совместимости с блоками
    }

    @Override
    public int getEnchantmentValue() {
        return 100; // Высокий уровень зачарований
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.EMPTY; // Не ремонтируется
    }
}