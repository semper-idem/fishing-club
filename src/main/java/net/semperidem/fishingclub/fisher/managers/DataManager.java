package net.semperidem.fishingclub.fisher.managers;

import net.semperidem.fishingclub.fisher.FishingCard;

import static net.semperidem.fishingclub.fisher.FishingCard.FISHING_CARD;

public abstract class DataManager implements NbtData{
    FishingCard trackedFor;
    public DataManager(FishingCard trackedFor) {
        this.trackedFor = trackedFor;
    }

    public void sync() {
        if (trackedFor.isClient()) {
            return;
        }
        FISHING_CARD.sync(trackedFor.getHolder(), ((buf, recipient) -> trackedFor.writeSyncPacket(buf, recipient, this)));
    }
}
