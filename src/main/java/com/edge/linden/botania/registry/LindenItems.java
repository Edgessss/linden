package com.edge.linden.botania.registry;

import com.edge.linden.Linden;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class LindenItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Linden.MOD_ID);

    public static class RegisteredItem {
        public final RegistryObject<Item> item;
        public final boolean addToCreativeTab;

        public RegisteredItem(RegistryObject<Item> item, boolean addToCreativeTab) {
            this.item = item;
            this.addToCreativeTab = addToCreativeTab;
        }
    }

    private static final List<RegisteredItem> REGISTERED_ITEMS = new ArrayList<>();

    private static RegisteredItem register(String name, Supplier<Item> supplier, boolean addToCreativeTab) {
        RegistryObject<Item> reg = ITEMS.register(name, supplier);
        RegisteredItem rItem = new RegisteredItem(reg, addToCreativeTab);
        REGISTERED_ITEMS.add(rItem);
        return rItem;
    }

    public static final RegisteredItem ASGARDANDELION_TIER1_ITEM = register("asgardandelion_tier1",
            () -> new BlockItem(LindenBlocks.ASGARDANDELION_TIER1.get(), new Properties()), true);

    public static List<RegistryObject<Item>> getItemsForCreativeTab() {
        List<RegistryObject<Item>> result = new ArrayList<>();
        for (RegisteredItem r : REGISTERED_ITEMS) {
            if (r.addToCreativeTab) {
                result.add(r.item);
            }
        }
        return result;
    }

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}
