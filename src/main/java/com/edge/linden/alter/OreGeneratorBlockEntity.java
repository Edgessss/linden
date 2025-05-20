package com.edge.linden.alter;

import com.edge.linden.registry.ModBlockEntities;
import com.edge.linden.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Containers;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class OreGeneratorBlockEntity extends BlockEntity implements WorldlyContainer {

    private ItemStack buffer = ItemStack.EMPTY;
    private final ModEnergyStorage energyStorage = new ModEnergyStorage(100_000_000, 1_000_000);
    private LazyOptional<ModEnergyStorage> energyStorageOptional = LazyOptional.of(() -> energyStorage);

    private final List<ItemStack> ores = new ArrayList<>();
    private int tickCounter = 0;

    private final int baseTicksPerOre = 5;
    private final int baseOresPerTick = 32;
    private final int energyPerOre = 50_000;

    private int upgradeLevel = 0;
    private final int maxUpgradeLevel = 5;

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

        if (tickCounter >= getEffectiveTicksPerOre()) {
            tickCounter = 0;

            int toGenerate = getEffectiveOresPerTick();
            for (int i = 0; i < toGenerate; i++) {
                if (energyStorage.getEnergyStored() < energyPerOre) break;

                ItemStack ore = ores.get(level.random.nextInt(ores.size())).copy();
                ore.setCount(1);

                if (buffer.isEmpty()) {
                    buffer = ore.copy();
                    energyStorage.extractEnergy(energyPerOre, false);
                } else if (ItemStack.isSameItemSameTags(buffer, ore) && buffer.getCount() < buffer.getMaxStackSize()) {
                    buffer.grow(1);
                    energyStorage.extractEnergy(energyPerOre, false);
                } else {
                    break;
                }
            }
        }

        tryExportItem();
    }

    private void tryExportItem() {
        if (level == null) return;
        if (buffer.isEmpty()) return;

        int toExportCount = Math.min(64, buffer.getCount());
        ItemStack toExport = buffer.copy();
        toExport.setCount(toExportCount);

        for (Direction direction : Direction.values()) {
            if (toExport.isEmpty()) break;

            BlockPos neighborPos = worldPosition.relative(direction);
            BlockEntity neighbor = level.getBlockEntity(neighborPos);
            if (neighbor == null) continue;

            LazyOptional<IItemHandler> cap = neighbor.getCapability(ForgeCapabilities.ITEM_HANDLER, direction.getOpposite());
            if (!cap.isPresent()) continue;

            IItemHandler neighborInv = cap.orElse(null);
            for (int slot = 0; slot < neighborInv.getSlots(); slot++) {
                if (toExport.isEmpty()) break;

                ItemStack remaining = neighborInv.insertItem(slot, toExport, false);

                int inserted = toExport.getCount() - remaining.getCount();
                if (inserted > 0) {
                    buffer.shrink(inserted);
                    toExport = remaining;
                }
            }
        }
    }

    public void incrementUpgradeLevel() {
        if (upgradeLevel < maxUpgradeLevel) {
            upgradeLevel++;
        }
    }

    public int getUpgradeLevel() {
        return upgradeLevel;
    }

    public void setUpgradeLevel(int level) {
        this.upgradeLevel = Math.max(0, Math.min(level, maxUpgradeLevel));
    }

    public int getMaxUpgradeLevel() {
        return maxUpgradeLevel;
    }

    public int getEffectiveTicksPerOre() {
        return Math.max(1, baseTicksPerOre - upgradeLevel);
    }

    public int getEffectiveOresPerTick() {
        return baseOresPerTick + upgradeLevel;
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (cap == ForgeCapabilities.ENERGY) return energyStorageOptional.cast();
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        energyStorageOptional = LazyOptional.of(() -> energyStorage);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        energyStorageOptional.invalidate();
    }

    public void dropItems() {
        if (level == null) return;

        if (!buffer.isEmpty()) {
            Containers.dropItemStack(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), buffer);
            buffer = ItemStack.EMPTY;
        }

        if (upgradeLevel > 0 && ModItems.UPGRADE_ITEM.item.isPresent()) {
            ItemStack upgradeStack = new ItemStack(ModItems.UPGRADE_ITEM.item.get(), upgradeLevel);
            ItemEntity upgradeDrop = new ItemEntity(
                    level,
                    worldPosition.getX() + 0.5,
                    worldPosition.getY() + 0.5,
                    worldPosition.getZ() + 0.5,
                    upgradeStack
            );
            level.addFreshEntity(upgradeDrop);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        CompoundTag bufferTag = new CompoundTag();
        buffer.save(bufferTag);
        nbt.put("buffer", bufferTag);
        nbt.putInt("energy", energyStorage.getEnergyStored());
        nbt.putInt("tickCounter", tickCounter);
        nbt.putInt("upgradeLevel", upgradeLevel);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        buffer = ItemStack.of(nbt.getCompound("buffer"));
        energyStorage.setEnergy(nbt.getInt("energy"));
        tickCounter = nbt.getInt("tickCounter");
        upgradeLevel = nbt.getInt("upgradeLevel");
    }

    public void setEnergy(int energy) {
        energyStorage.setEnergy(energy);
    }

    @Override public int[] getSlotsForFace(Direction side) { return new int[0]; }
    @Override public boolean canPlaceItemThroughFace(int index, ItemStack stack, Direction direction) { return false; }
    @Override public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) { return false; }
    @Override public int getContainerSize() { return 0; }
    @Override public boolean isEmpty() { return buffer.isEmpty(); }
    @Override public ItemStack getItem(int slot) { return ItemStack.EMPTY; }
    @Override public ItemStack removeItem(int slot, int count) { return ItemStack.EMPTY; }
    @Override public ItemStack removeItemNoUpdate(int slot) { return ItemStack.EMPTY; }
    @Override public void setItem(int slot, ItemStack stack) {}
    @Override public boolean stillValid(Player player) { return true; }
    @Override public void clearContent() { buffer = ItemStack.EMPTY; }

    public static class ModEnergyStorage extends net.minecraftforge.energy.EnergyStorage {
        public ModEnergyStorage(int capacity, int maxTransfer) {
            super(capacity, maxTransfer);
        }

        public void setEnergy(int energy) {
            this.energy = Math.min(energy, getMaxEnergyStored());
        }
    }
}
