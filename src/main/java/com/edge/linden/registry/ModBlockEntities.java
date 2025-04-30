package com.edge.linden.registry;

import com.edge.linden.alter.EG1BlockEntity;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    //  DeferredRegister
    public static final DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, "linden");

    // Регистрация
    public static final RegistryObject<BlockEntityType<EG1BlockEntity>> EG_1 = register("eg_1", ModBlocks.EG_1, EG1BlockEntity::new);
    public static final RegistryObject<BlockEntityType<EG1BlockEntity>> EG_2 = register("eg_2", ModBlocks.EG_2, EG1BlockEntity::new);

    // Универсальный метод регистрации BlockEntity
    private static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> register(
            String registryName, RegistryObject<Block> block, BlockEntityType.BlockEntitySupplier<T> factory) {
        return REGISTRY.register(registryName, () -> BlockEntityType.Builder.of(factory, block.get()).build(null));
    }
}