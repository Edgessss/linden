package com.edge.linden.alter.procedures;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.IEnergyStorage;

public class EG1UpdateTickProcedure {
    public static void execute(LevelAccessor world, double x, double y, double z) {
        // Текущая позиция блока
        BlockPos currentPos = new BlockPos((int) x, (int) y, (int) z);

        // Количество энергии для передачи
        int amount = 500_000_000;

        // Перебираем все направления
        for (Direction direction : Direction.values()) {
            // Получаем позицию соседнего блока
            BlockPos neighborPos = currentPos.relative(direction);
            BlockEntity blockEntity = world.getBlockEntity(neighborPos);

            if (blockEntity != null) {
                // Проверяем возможность передачи энергии через capability
                blockEntity.getCapability(ForgeCapabilities.ENERGY, direction.getOpposite()).ifPresent(capability -> {
                    if (capability.canReceive()) { // Проверяем, может ли блок принимать энергию
                        capability.receiveEnergy(amount, false); // Передаем энергию
                    }
                });
            }
        }
    }
}