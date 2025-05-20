package com.edge.linden.registry;

import com.edge.linden.Linden;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod.EventBusSubscriber(modid = Linden.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class LindenDataGen {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        var generator = event.getGenerator();
        var existingFileHelper = event.getExistingFileHelper();
        var packOutput = event.getGenerator().getPackOutput();

        var lookupProvider = event.getLookupProvider();


    }
}
