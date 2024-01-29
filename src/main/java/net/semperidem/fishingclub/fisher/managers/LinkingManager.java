package net.semperidem.fishingclub.fisher.managers;

import net.minecraft.nbt.NbtCompound;
import net.semperidem.fishingclub.fisher.FishingCard;

public class LinkingManager extends DataManager {
    public LinkingManager(FishingCard trackedFor) {
        super(trackedFor);
    }

    @Override
    public void readNbt(NbtCompound nbtCompound) {

    }

    @Override
    public void writeNbt(NbtCompound nbtCompound) {

    }
}
