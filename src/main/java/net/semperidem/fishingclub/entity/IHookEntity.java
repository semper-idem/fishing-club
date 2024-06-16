package net.semperidem.fishingclub.entity;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.ChunkPos;
import net.semperidem.fishingclub.fisher.FishingCard;

public interface IHookEntity {
    FishingCard getFishingCard();

    ItemStack getCaughtUsing();

    ChunkPos getFishedInChunk();

    float getFishMethodDebuff();

    default float getWaitTime() {
        return 0;
    }
}
