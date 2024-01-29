package net.semperidem.fishingclub.fisher.managers;

import net.minecraft.nbt.NbtCompound;

public interface NbtData {
    void readNbt(NbtCompound nbtCompound);
    void writeNbt(NbtCompound nbtCompound);

}
