package com.edge.linden.botania;


import com.edge.linden.botania.entity.AsgardandelionBlockEntity;
import com.edge.linden.botania.registry.LindenBlockEntities;
import net.minecraft.core.BlockPos;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import org.jetbrains.annotations.Nullable;

public class AsgardandelionBlock extends Block implements EntityBlock {

    public AsgardandelionBlock() {
        super(Properties.of()
                .mapColor(MapColor.COLOR_PURPLE)
                .noCollission()
                .instabreak()
                .pushReaction(PushReaction.DESTROY)
                .sound(SoundType.GRASS));
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new AsgardandelionBlockEntity(pos, state);
    }
    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos below = pos.below();
        BlockState stateBelow = level.getBlockState(below);
        return stateBelow.is(Blocks.DIRT) || stateBelow.is(Blocks.GRASS_BLOCK) || stateBelow.is(Blocks.PODZOL);
    }
    @Override
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide) {
            return null;
        }

        return type == LindenBlockEntities.ASGARDANDELION_TIER1.get()
                ? (lvl, pos, st, be) -> ((AsgardandelionBlockEntity) be).tickFlower()
                : null;
    }
}
