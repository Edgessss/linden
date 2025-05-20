package com.edge.linden.alter.procedures;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.IEnergyStorage;

public class EG1UpdateTickProcedure {
    public static void execute(LevelAccessor world, double x, double y, double z) {
        BlockPos currentPos = new BlockPos((int) x, (int) y, (int) z);

        // Количество энергии для передачи в long
        long amount = 500_000_000L;

        for (Direction direction : Direction.values()) {
            BlockPos neighborPos = currentPos.relative(direction);
            BlockEntity blockEntity = world.getBlockEntity(neighborPos);

            if (blockEntity != null) {
                blockEntity.getCapability(ForgeCapabilities.ENERGY, direction.getOpposite()).ifPresent(capability -> {
                    if (capability.canReceive()) {
                        long remaining = amount;
                        while (remaining > 0) {
                            int toSend = (int) Math.min(Integer.MAX_VALUE, remaining);
                            int received = capability.receiveEnergy(toSend, false);
                            if (received <= 0) break;
                            remaining -= received;
                        }
                    }
                });
            }
        }
    }
}
