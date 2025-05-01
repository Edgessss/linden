package com.edge.linden.botania.registry;

import com.edge.linden.botania.AsgardandelionBlock;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class LindenBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, "linden");

    public static final RegistryObject<Block> ASGARDANDELION_TIER1 =
            BLOCKS.register("asgardandelion_tier1", AsgardandelionBlock::new);
}
