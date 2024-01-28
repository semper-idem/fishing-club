package net.semperidem.fishingclub.fisher.util;

import net.semperidem.fishingclub.fisher.FishingCard;

public abstract class DataManager implements NbtData{
    FishingCard trackedFor;
    public DataManager(FishingCard trackedFor) {
        this.trackedFor = trackedFor;
    }
}
