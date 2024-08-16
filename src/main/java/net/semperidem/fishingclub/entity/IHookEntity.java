package net.semperidem.fishingclub.entity;

import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.item.fishing_rod.components.RodConfiguration;

public interface IHookEntity {

    default FishingCard getFishingCard() {
        return FishingCard.DEFAULT;
    }

    default RodConfiguration getCaughtUsing() {
        return RodConfiguration.EMPTY;
    }

    default float getCircumstanceQuality() {
        return 0.5f;
    }

    default int getWaitTime() {
        return 0;
    }
}
