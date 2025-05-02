package com.edge.linden.botania.entity;

import com.edge.linden.botania.registry.LindenBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.api.BotaniaForgeClientCapabilities;
import vazkii.botania.api.block.WandHUD;
import vazkii.botania.api.block_entity.GeneratingFlowerBlockEntity;
import vazkii.botania.api.block_entity.RadiusDescriptor;

public class AsgardandelionBlockEntity extends GeneratingFlowerBlockEntity {
    private static final int MANA_TRANSFER_PER_TICK = 2_000_000;

    public AsgardandelionBlockEntity(BlockPos pos, BlockState state) {
        super(LindenBlockEntities.ASGARDANDELION_TIER1.get(), pos, state);
    }

    @Override
    public void tickFlower() {
        super.tickFlower();

        if (!level.isClientSide) {
            // Обновляем визуальное значение маны до максимума, чтобы отображалось полным
            if (getMana() < getMaxMana()) {
                super.addMana(getMaxMana() - getMana());
            }

            // Пытаемся передать ману спредеру
            super.addMana(MANA_TRANSFER_PER_TICK); // внутренняя генерация
            emptyManaIntoCollector();              // передача

            // Опять восстанавливаем "визуальную" ману
            if (getMana() < getMaxMana()) {
                super.addMana(getMaxMana() - getMana());
            }
        } else {
            if (level.random.nextInt(6) == 0) {
                emitParticle(ParticleTypes.HAPPY_VILLAGER, 0.5, 0.7, 0.5, 0.0, 0.02, 0.0);
            }
        }
    }

    private final LazyOptional<WandHUD> wandHudCap = LazyOptional.of(() -> new BindableFlowerWandHud<AsgardandelionBlockEntity>(this));

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == BotaniaForgeClientCapabilities.WAND_HUD) {
            return wandHudCap.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        wandHudCap.invalidate();
    }

    @Override
    public int getMaxMana() {
        return Integer.MAX_VALUE;
    }

    @Override
    public int getColor() {
        return 0x8A2BE2;
    }

    @Override
    public RadiusDescriptor getRadius() {
        return null;
    }

    @Override
    public void writeToPacketNBT(CompoundTag tag) {
        super.writeToPacketNBT(tag);
    }

    @Override
    public void readFromPacketNBT(CompoundTag tag) {
        super.readFromPacketNBT(tag);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, AsgardandelionBlockEntity be) {
        be.tickFlower();
    }
}
