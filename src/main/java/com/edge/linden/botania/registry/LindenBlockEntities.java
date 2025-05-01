package com.edge.linden.botania.registry;

import com.edge.linden.botania.entity.AsgardandelionBlockEntity;
import com.edge.linden.Linden;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class LindenBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Linden.MOD_ID);

    public static final RegistryObject<BlockEntityType<AsgardandelionBlockEntity>> ASGARDANDELION_TIER1 =
            BLOCK_ENTITIES.register("asgardandelion_tier1",
                    () -> BlockEntityType.Builder.of(AsgardandelionBlockEntity::new,
                            LindenBlocks.ASGARDANDELION_TIER1.get()).build(null));
}
