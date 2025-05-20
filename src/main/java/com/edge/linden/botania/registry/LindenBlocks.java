package com.edge.linden.botania.registry;

import com.edge.linden.botania.AsgardandelionBlock;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static vazkii.botania.common.block.BotaniaBlocks.dreamwood;

public class LindenBlocks {
    private static final BlockBehaviour.StateArgumentPredicate<EntityType<?>> NO_SPAWN = (state, world, pos, et) -> false;
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, "linden");

    public static final RegistryObject<Block> ASGARDANDELION_TIER1 =
            BLOCKS.register("asgardandelion_tier1", AsgardandelionBlock::new);

}
