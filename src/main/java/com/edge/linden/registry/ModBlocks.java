package com.edge.linden.registry;

import com.edge.linden.alter.EG1Block;
import com.edge.linden.alter.EG2Block;

import com.edge.linden.alter.OreGeneratorBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {

 public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, "linden");


 public static final RegistryObject<Block> EG_1 = REGISTRY.register("eg_1", () -> new EG1Block(
         Block.Properties.copy(Blocks.COBBLESTONE)
 ));

 public static final RegistryObject<Block> EG_2 = REGISTRY.register("eg_2", () -> new EG2Block(
         Block.Properties.copy(Blocks.COBBLESTONE)
 ));
 public static final RegistryObject<OreGeneratorBlock> ORE_GENERATOR_BLOCK = REGISTRY.register("ore_generator_block",
         () -> new OreGeneratorBlock());
}