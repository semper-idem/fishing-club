package net.semperidem.fishing_club.entity;

import net.minecraft.util.math.ChunkPos;
import net.semperidem.fishing_club.fisher.FishingCard;
import net.semperidem.fishing_club.item.fishing_rod.components.RodConfiguration;

public interface IHookEntity {
    FishingCard getFishingCard();

    RodConfiguration getCaughtUsing();

    ChunkPos getFishedInChunk();

    float getFishMethodDebuff();

    default int getWaitTime() {
        return 0;
    }
}
