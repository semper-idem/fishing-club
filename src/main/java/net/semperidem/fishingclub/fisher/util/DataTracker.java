package net.semperidem.fishingclub.fisher.util;

import net.minecraft.nbt.NbtCompound;

public interface DataTracker {
    void readNbt(NbtCompound nbtCompound);
    void writeNbt(NbtCompound nbtCompound);

}
