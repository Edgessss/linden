package com.edge.linden.registry;



import com.edge.linden.Linden;
import com.edge.linden.registry.ModDamageTypes;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = Linden.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModDataGenerators {

    @net.minecraftforge.eventbus.api.SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<net.minecraft.core.HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(event.includeServer(), new DatapackBuiltinEntriesProvider(
                output,
                lookupProvider,
                new RegistrySetBuilder()
                        .add(Registries.DAMAGE_TYPE, ModDamageTypes::bootstrap),
                Set.of(Linden.MOD_ID)
        ));
    }
}
