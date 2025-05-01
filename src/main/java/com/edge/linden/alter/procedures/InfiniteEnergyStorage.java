package com.edge.linden.alter.procedures;

import net.minecraftforge.energy.IEnergyStorage;

public class InfiniteEnergyStorage implements IEnergyStorage {

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        // Этот метод можно оставить пустым, так как блок не принимает энергию
        return 0;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        // Всегда возвращаем запрошенное количество энергии (бесконечный источник)
        return maxExtract;
    }

    @Override
    public int getEnergyStored() {
        // Возвращаем максимальное значение Integer для имитации "бесконечности"
        return Integer.MAX_VALUE;
    }

    @Override
    public int getMaxEnergyStored() {
        // Возвращаем максимальное значение Integer для имитации "бесконечности"
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean canExtract() {
        // Разрешаем извлечение энергии
        return true;
    }

    @Override
    public boolean canReceive() {
        // Запрещаем прием энергии (блок только отдает энергию)
        return false;
    }
}