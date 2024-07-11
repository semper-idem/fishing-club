package net.semperidem.fishing_club.fisher.managers;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;

public interface NbtData {
    void readNbt(NbtCompound nbtCompound, RegistryWrapper.WrapperLookup wrapperLookup);
    void writeNbt(NbtCompound nbtCompound, RegistryWrapper.WrapperLookup wrapperLookup);

}
