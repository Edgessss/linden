package com.edge.linden.registry;

import com.edge.linden.alter.Items.UpgradeItem;
import com.edge.linden.alter.procedures.ModRarities;
import com.edge.linden.avaritia.items.DemonicSword;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ModItems {

    public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, "linden");

    // Вспомогательный класс для хранения RegistryObject и флага попадания в креатив таб
    public static class RegisteredItem {
        public final RegistryObject<Item> item;
        public final boolean addToCreativeTab;

        public RegisteredItem(RegistryObject<Item> item, boolean addToCreativeTab) {
            this.item = item;
            this.addToCreativeTab = addToCreativeTab;
        }
    }

    // Список всех зарегистрированных предметов с флагом
    private static final List<RegisteredItem> REGISTERED_ITEMS = new ArrayList<>();

    // Вспомогательный метод регистрации
    private static RegisteredItem register(String name, Supplier<Item> supplier, boolean addToCreativeTab) {
        RegistryObject<Item> reg = REGISTRY.register(name, supplier);
        RegisteredItem rItem = new RegisteredItem(reg, addToCreativeTab);
        REGISTERED_ITEMS.add(rItem);
        return rItem;
    }

    // Предметы с указанием, попадать ли в креатив таб
    public static final RegisteredItem EG_1 = register("eg_1",
            () -> new BlockItem(ModBlocks.EG_1.get(), new Properties().rarity(ModRarities.UNDEGRAUND)), true);

    public static final RegisteredItem EG_2 = register("eg_2",
            () -> new BlockItem(ModBlocks.EG_2.get(), new Properties().rarity(ModRarities.COSMIC)), true);

    public static final RegisteredItem ORE_GENERATOR_ITEM = register("ore_generator_block",
            () -> new BlockItem(ModBlocks.ORE_GENERATOR_BLOCK.get(), new Properties().rarity(ModRarities.EPIC)), true);

    public static final RegisteredItem UPGRADE_ITEM = register("upgrade_item",
            () -> new UpgradeItem(new Properties().stacksTo(16)), true);

    public static final RegisteredItem DEMONIC_SWORD = register("demonic_sword",
            () -> new DemonicSword(new Properties().rarity(ModRarities.UNDEGRAUND)), true);

    // Метод для получения всех предметов, которые нужно показывать в креатив табе
    public static List<RegistryObject<Item>> getItemsForCreativeTab() {
        List<RegistryObject<Item>> result = new ArrayList<>();
        for (RegisteredItem r : REGISTERED_ITEMS) {
            if (r.addToCreativeTab) {
                result.add(r.item);
            }
        }
        return result;
    }
}
