package net.semperidem.fishingclub.fisher.managers;

import net.semperidem.fishingclub.fisher.Card;

import static net.semperidem.fishingclub.fisher.Card.CARD;

public abstract class DataManager implements NbtData{
    Card trackedFor;
    public DataManager(Card trackedFor) {
        this.trackedFor = trackedFor;
    }

    public void sync() {
        if (trackedFor.isClient()) {
            return;
        }
        CARD.sync(trackedFor.holder(), ((buf, recipient) -> trackedFor.writeSyncPacket(buf, recipient, this)));
    }
}
