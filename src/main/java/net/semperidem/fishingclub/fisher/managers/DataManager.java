package net.semperidem.fishingclub.fisher.managers;

import net.semperidem.fishingclub.fisher.FishingCard;

import static net.semperidem.fishingclub.fisher.FishingCard.FISHING_CARD;

public abstract class DataManager implements NbtData{
    FishingCard trackedFor;
    boolean isDirty;
    public DataManager(FishingCard trackedFor) {
        this.trackedFor = trackedFor;
    }

    void markDirty() {
        if (trackedFor.isClient()) {
            return;
        }
        this.isDirty = true;
        FISHING_CARD.sync(trackedFor.getHolder());
    }
}
