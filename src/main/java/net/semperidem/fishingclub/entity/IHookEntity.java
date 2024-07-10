package net.semperidem.fishingclub.entity;

import net.minecraft.util.math.ChunkPos;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.item.fishing_rod.components.RodConfiguration;

public interface IHookEntity {
    FishingCard getFishingCard();

    RodConfiguration getCaughtUsing();

    ChunkPos getFishedInChunk();

    float getFishMethodDebuff();

    default int getWaitTime() {
        return 0;
    }
}
