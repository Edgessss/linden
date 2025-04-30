package com.edge.linden.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import com.edge.linden.registry.ModItems;

public class LindenModeTabs {

 public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, "linden");


 public static final RegistryObject<CreativeModeTab> PANELS = REGISTRY.register("panels", () ->
         CreativeModeTab.builder()
                 .title(Component.translatable("item_group.linden.panels"))
                 .icon(() -> new ItemStack(ModItems.EG_1.get()))
                 .displayItems((parameters, output) -> {
                  output.accept(ModItems.EG_1.get());
                  output.accept(ModItems.EG_2.get());
                  output.accept(ModItems.ORE_GENERATOR_ITEM.get());
                 }).build()
 );
}

