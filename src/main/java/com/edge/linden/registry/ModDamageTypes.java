package com.edge.linden.registry;

import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;



public class ModDamageTypes {

    public static final ResourceKey<DamageType> DEMONIC =
            ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("linden", "demonic"));

    public static final RegistrySetBuilder DAMAGE_BUILDER = new RegistrySetBuilder()
            .add(Registries.DAMAGE_TYPE, ModDamageTypes::bootstrap);

    public static void bootstrap(BootstapContext<DamageType> context) {
        context.register(DEMONIC, new DamageType(
                "demonic",
                DamageScaling.NEVER,
                0.1F,
                DamageEffects.HURT
        ));
    }
}