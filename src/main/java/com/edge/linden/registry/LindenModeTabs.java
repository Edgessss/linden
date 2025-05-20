package com.edge.linden.registry;

import com.edge.linden.botania.registry.LindenItems;
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
                    .icon(() -> new ItemStack(ModItems.EG_1.item.get()))
                    .displayItems((parameters, output) -> {
                        for (var itemReg : ModItems.getItemsForCreativeTab()) {
                            output.accept(itemReg.get());
                            for (var item : LindenItems.getItemsForCreativeTab()) {
                                output.accept(item.get());
                            }
                        }
                    })
                    .build()
    );
}

