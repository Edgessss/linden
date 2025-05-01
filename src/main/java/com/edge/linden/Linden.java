package com.edge.linden;


import com.edge.linden.registry.ModBlocks;
import com.edge.linden.registry.ModItems;
import com.edge.linden.registry.LindenModeTabs;
import com.edge.linden.registry.ModBlockEntities;


import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import net.minecraftforge.fml.common.Mod;

import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.eventbus.api.IEventBus;

@Mod(Linden.MOD_ID)
public class Linden {
    public static final String MOD_ID = "linden";

    public Linden() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModBlocks.REGISTRY.register(modEventBus);

        ModItems.REGISTRY.register(modEventBus);

        LindenModeTabs.REGISTRY.register(modEventBus);

        ModBlockEntities.REGISTRY.register(modEventBus);

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::addCreative);


    }

    private void commonSetup(final FMLCommonSetupEvent event) {

    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == LindenModeTabs.PANELS.getKey()) {
            event.accept(ModItems.EG_1);
            event.accept(ModItems.EG_2);
            event.accept(ModItems.ORE_GENERATOR_ITEM);
        }
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {

        }
    }
}