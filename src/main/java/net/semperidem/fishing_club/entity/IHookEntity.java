package net.semperidem.fishing_club.entity;

import net.semperidem.fishing_club.fisher.FishingCard;
import net.semperidem.fishing_club.item.fishing_rod.components.RodConfiguration;

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
