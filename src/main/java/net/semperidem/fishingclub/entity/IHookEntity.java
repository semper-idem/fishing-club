package net.semperidem.fishingclub.entity;

import net.minecraft.util.math.random.Random;
import net.semperidem.fishingclub.fisher.Card;
import net.semperidem.fishingclub.item.fishing_rod.components.RodConfiguration;


public interface IHookEntity {

    default Random getRandom() {
        return Random.create();
    }

    default Card getFishingCard() {
        return Card.DEFAULT;
    }

    default int maxWeight() {
        return 999999;
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
