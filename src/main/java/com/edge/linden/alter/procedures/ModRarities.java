package com.edge.linden.alter.procedures;

import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.Rarity;

public class ModRarities {
    // Стандартные редкости
    public static final Rarity COMMON = Rarity.COMMON;
    public static final Rarity UNCOMMON = Rarity.UNCOMMON;
    public static final Rarity RARE = Rarity.RARE;
    public static final Rarity EPIC = Rarity.EPIC;

    // Пользовательские редкости с HEX-цветами
    public static final Rarity LEGENDARY = createCustomRarity("LEGENDARY", 0xFFC166); // Золотой HEX
    public static final Rarity NEUTRON = createCustomRarity("NEUTRON", 0x8D899A); // Красный HEX
    public static final Rarity FORBIDDEN = createCustomRarity("FORBIDDEN", 0xEA9999); // Красный HEX
    public static final Rarity COSMIC = createCustomRarity("COSMIC", 0xE98EC0); // Фиолетовый HEX
    public static final Rarity UNDEGRAUND = createCustomRarity("UNDEGRAUND", 0xE96339); // Оранжевый HEX


    private static Rarity createCustomRarity(String name, int hexColor) {
        return Rarity.create(name, style -> style.withColor(TextColor.fromRgb(hexColor)));
    }
}