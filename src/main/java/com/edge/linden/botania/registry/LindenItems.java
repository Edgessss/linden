package com.edge.linden.botania.registry;


import com.edge.linden.Linden;
import net.minecraft.world.item.BlockItem;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;

import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


public class LindenItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Linden.MOD_ID);

    public static final RegistryObject<Item> ASGARDANDELION_TIER1_ITEM =
            ITEMS.register("asgardandelion_tier1", () -> new BlockItem(LindenBlocks.ASGARDANDELION_TIER1.get(), new Properties()));
    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }

}
