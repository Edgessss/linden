package com.edge.linden.alter;

import com.edge.linden.registry.ModBlockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class OreGeneratorBlock extends BaseEntityBlock {

    public OreGeneratorBlock() {
        super(Properties.of().strength(3.0f).requiresCorrectToolForDrops());
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new OreGeneratorBlockEntity(pos, state);
    }
    @Override
    public void onRemove(BlockState oldState, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!oldState.is(newState.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof OreGeneratorBlockEntity generator) {
                generator.dropItems();
            }
        }
        super.onRemove(oldState, level, pos, newState, isMoving);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return blockEntityType == ModBlockEntities.ORE_GENERATOR_BLOCK_ENTITY.get()
                ? (tickerLevel, pos, tickerState, be) -> ((OreGeneratorBlockEntity) be).tickServer()
                : null;
    }
}
