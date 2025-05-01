package com.edge.linden.alter;

import com.edge.linden.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Containers;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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

public class OreGeneratorBlockEntity extends BlockEntity implements WorldlyContainer {

    private final ItemStackHandler itemHandler = new ItemStackHandler(1);
    private final ModEnergyStorage energyStorage = new ModEnergyStorage(100_000_000, 1_000_000);
    private LazyOptional<IItemHandler> itemHandlerOptional = LazyOptional.of(() -> itemHandler);
    private LazyOptional<ModEnergyStorage> energyStorageOptional = LazyOptional.of(() -> energyStorage);

    private final List<ItemStack> ores = new ArrayList<>();
    private int tickCounter = 0;


    private final int ticksPerOre = 5;
    private final int oresPerTick = 32;
    private final int energyPerOre = 50_000;

    public OreGeneratorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ORE_GENERATOR_BLOCK_ENTITY.get(), pos, state);
        reloadOres();
    }

    public void reloadOres() {
        ores.clear();
        ForgeRegistries.ITEMS.tags()
                .getTag(net.minecraft.tags.ItemTags.create(new ResourceLocation("forge", "ores")))
                .forEach(item -> ores.add(new ItemStack(item)));
    }

    public void tickServer() {
        if (level == null || level.isClientSide) return;

        tickCounter++;
        if (ores.isEmpty()) return;

        if (tickCounter >= ticksPerOre) {
            tickCounter = 0;

            for (int i = 0; i < oresPerTick; i++) {
                if (energyStorage.getEnergyStored() < energyPerOre) break;
                if (!itemHandler.getStackInSlot(0).isEmpty()) break;

                energyStorage.extractEnergy(energyPerOre, false);
                ItemStack ore = ores.get(level.random.nextInt(ores.size())).copy();
                itemHandler.insertItem(0, ore, false);
            }
        }

        tryExportItem();
    }

    private void tryExportItem() {
        if (level == null) return;

        ItemStack toExport = itemHandler.extractItem(0, 64, true);
        if (toExport.isEmpty()) return;

        for (Direction direction : Direction.values()) {
            if (toExport.isEmpty()) break;

            BlockPos neighborPos = worldPosition.relative(direction);
            BlockEntity neighbor = level.getBlockEntity(neighborPos);
            if (neighbor == null) continue;

            LazyOptional<IItemHandler> cap = neighbor.getCapability(ForgeCapabilities.ITEM_HANDLER, direction.getOpposite());
            if (!cap.isPresent()) continue;

            IItemHandler neighborInv = cap.orElse(null);
            for (int slot = 0; slot < neighborInv.getSlots(); slot++) {
                ItemStack remaining = neighborInv.insertItem(slot, toExport, false);

                if (remaining.getCount() < toExport.getCount()) {
                    int inserted = toExport.getCount() - remaining.getCount();
                    itemHandler.extractItem(0, inserted, false);
                    toExport = remaining.copy();
                    if (toExport.isEmpty()) break;
                }
            }
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
        nbt.putInt("tickCounter", tickCounter);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        itemHandler.deserializeNBT(nbt.getCompound("inventory"));
        energyStorage.setEnergy(nbt.getInt("energy"));
        tickCounter = nbt.getInt("tickCounter");
    }

    public void setEnergy(int energy) {
        energyStorage.setEnergy(energy);
    }

    // --- WorldlyContainer ---
    @Override public int[] getSlotsForFace(Direction side) { return new int[]{0}; }
    @Override public boolean canPlaceItemThroughFace(int index, ItemStack stack, Direction direction) { return false; }
    @Override public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) { return true; }
    @Override public int getContainerSize() { return 1; }
    @Override public boolean isEmpty() { return itemHandler.getStackInSlot(0).isEmpty(); }
    @Override public ItemStack getItem(int slot) { return itemHandler.getStackInSlot(slot); }
    @Override public ItemStack removeItem(int slot, int count) { return itemHandler.extractItem(slot, count, false); }
    @Override public ItemStack removeItemNoUpdate(int slot) {
        ItemStack stack = getItem(slot);
        itemHandler.setStackInSlot(slot, ItemStack.EMPTY);
        return stack;
    }
    @Override public void setItem(int slot, ItemStack stack) { itemHandler.setStackInSlot(slot, stack); }
    @Override public boolean stillValid(Player player) { return true; }
    @Override public void clearContent() { itemHandler.setStackInSlot(0, ItemStack.EMPTY); }

    public static class ModEnergyStorage extends net.minecraftforge.energy.EnergyStorage {
        public ModEnergyStorage(int capacity, int maxTransfer) {
            super(capacity, maxTransfer);
        }
        public void setEnergy(int energy) {
            this.energy = Math.min(energy, getMaxEnergyStored());
        }
    }
}
