package com.edge.linden.registry;

import com.edge.linden.alter.procedures.ModRarities;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import com.edge.linden.registry.ModBlocks;

public class ModItems {

    public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, "linden");


    public static final RegistryObject<Item> EG_1 = REGISTRY.register("eg_1",
            () -> new BlockItem(ModBlocks.EG_1.get(), new Item.Properties().rarity(ModRarities.UNDEGRAUND)));

    public static final RegistryObject<Item> EG_2 = REGISTRY.register("eg_2",
            () -> new BlockItem(ModBlocks.EG_2.get(), new Item.Properties().rarity(ModRarities.COSMIC)));
}