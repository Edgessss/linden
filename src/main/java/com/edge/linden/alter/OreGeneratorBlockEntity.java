package com.edge.linden.alter;

import com.edge.linden.config.OreGeneratorConfig;
import com.edge.linden.registry.ModBlockEntities;
import com.edge.linden.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Containers;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OreGeneratorBlockEntity extends BlockEntity implements WorldlyContainer {



    private final ItemStackHandler itemHandler = new ItemStackHandler(1); // 1 слот
    private final ModEnergyStorage energyStorage = new ModEnergyStorage(
            OreGeneratorConfig.ENERGY_CAPACITY.get(),
            OreGeneratorConfig.ENERGY_PER_TICK.get()
    );

    private LazyOptional<IItemHandler> itemHandlerOptional = LazyOptional.of(() -> itemHandler);
    private LazyOptional<ModEnergyStorage> energyStorageOptional = LazyOptional.of(() -> energyStorage);

    private final List<ItemStack> ores = new ArrayList<>();

    public OreGeneratorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ORE_GENERATOR_BLOCK_ENTITY.get(), pos, state);
        reloadOres();
    }

    public void reloadOres() {
        ores.clear();
        ForgeRegistries.ITEMS.tags().getTag(net.minecraft.tags.ItemTags.create(new ResourceLocation("forge", "ores")))
                .forEach(item -> ores.add(new ItemStack(item)));

        OreGeneratorConfig.CUSTOM_ORES.get().forEach(registryName -> {
            Optional.ofNullable(ForgeRegistries.ITEMS.getValue(new ResourceLocation(registryName)))
                    .ifPresent(item -> ores.add(new ItemStack(item)));
        });
    }

    public void tickServer() {
        if (level == null || level.isClientSide) return;

        int oresPerTick = OreGeneratorConfig.ORES_PER_TICK.get();
        int energyPerOre = OreGeneratorConfig.ENERGY_PER_TICK.get();

        if (ores.isEmpty()) {
            return;
        }

        for (int i = 0; i < oresPerTick; i++) {
            if (energyStorage.getEnergyStored() < energyPerOre) break;
            if (!itemHandler.getStackInSlot(0).isEmpty()) break;

            energyStorage.extractEnergy(energyPerOre, false);

            ItemStack ore = ores.get(level.random.nextInt(ores.size())).copy();
            itemHandler.insertItem(0, ore, false);
        }
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (cap == ForgeCapabilities.ENERGY) return energyStorageOptional.cast();
        if (cap == ForgeCapabilities.ITEM_HANDLER) return itemHandlerOptional.cast();
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        itemHandlerOptional = LazyOptional.of(() -> itemHandler);
        energyStorageOptional = LazyOptional.of(() -> energyStorage);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        itemHandlerOptional.invalidate();
        energyStorageOptional.invalidate();
    }

    public void dropItems() {
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            ItemStack stack = itemHandler.getStackInSlot(i);
            if (!stack.isEmpty()) {
                Containers.dropItemStack(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), stack);
            }
        }
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.put("inventory", itemHandler.serializeNBT());
        nbt.putInt("energy", energyStorage.getEnergyStored());
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        itemHandler.deserializeNBT(nbt.getCompound("inventory"));
        energyStorage.setEnergy(nbt.getInt("energy"));
    }

    public void setEnergy(int energy) {
        energyStorage.setEnergy(energy);
    }

    // --- WorldlyContainer (интерфейс взаимодействия с воронками и т.п.) ---
    @Override
    public int[] getSlotsForFace(Direction side) {
        return new int[]{0};
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack stack, Direction direction) {
        return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return true;
    }

    @Override
    public int getContainerSize() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return itemHandler.getStackInSlot(0).isEmpty();
    }

    @Override
    public ItemStack getItem(int slot) {
        return itemHandler.getStackInSlot(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int count) {
        return itemHandler.extractItem(slot, count, false);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        ItemStack stack = getItem(slot);
        itemHandler.setStackInSlot(slot, ItemStack.EMPTY);
        return stack;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        itemHandler.setStackInSlot(slot, stack);
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public void clearContent() {
        itemHandler.setStackInSlot(0, ItemStack.EMPTY);
    }

    // --- Кастомный класс хранилища энергии ---
    public static class ModEnergyStorage extends net.minecraftforge.energy.EnergyStorage {
        public ModEnergyStorage(int capacity, int maxTransfer) {
            super(capacity, maxTransfer);
        }

        public void setEnergy(int energy) {
            this.energy = Math.min(energy, getMaxEnergyStored());
        }
    }
}
