package net.semperidem.fishingclub.fisher.util;

import net.semperidem.fishingclub.fisher.FishingCard;

public abstract class DataTrackerInstance implements DataTracker{
    FishingCard trackingFor;
    DataTrackerInstance(FishingCard trackingFor){
        this.trackingFor = trackingFor;
    }
}
