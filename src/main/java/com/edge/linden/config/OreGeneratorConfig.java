package com.edge.linden.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

import java.util.Arrays;
import java.util.List;

public class OreGeneratorConfig {
    public static final ForgeConfigSpec SPEC;

    public static ForgeConfigSpec.IntValue ENERGY_CAPACITY;
    public static ForgeConfigSpec.IntValue ENERGY_PER_TICK;
    public static ForgeConfigSpec.IntValue TICKS_PER_ORE;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> CUSTOM_ORES;
    public static ForgeConfigSpec.IntValue ORES_PER_TICK;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.comment("Настройки генератора руды").push("ore_generator");

        ENERGY_CAPACITY = builder
                .comment("Максимальная вместимость FE")
                .defineInRange("energy_capacity", 1_000_000, 1000, 100_000_000);

        ENERGY_PER_TICK = builder
                .comment("Сколько FE потребляется за тик за каждую руду")
                .defineInRange("energy_per_tick", 10_000, 1, 1_000_000);

        TICKS_PER_ORE = builder
                .comment("Сколько тиков нужно для генерации одной руды")
                .defineInRange("ticks_per_ore", 100, 1, 10_000);

        ORES_PER_TICK = builder
                .comment("Сколько руд генерируется за один тик")
                .defineInRange("ores_per_tick", 1, 0, 64);

        CUSTOM_ORES = builder
                .comment("Дополнительные руды, которые не имеют тега forge:ores")
                .defineList("custom_ores",
                        Arrays.asList(
                                "minecraft:iron_ore",
                                "minecraft:gold_ore"
                        ),
                        o -> o instanceof String);

        builder.pop();
        SPEC = builder.build();
    }

    public static void register() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, SPEC);
    }
}