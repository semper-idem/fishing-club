package net.semperidem.fishingclub.entity;

import net.minecraft.item.ItemStack;
import net.semperidem.fishingclub.fisher.FishingCard;

public interface IHookEntity {
    FishingCard getFishingCard();

    ItemStack getCaughtUsing();

    FishingCard.Chunk getFishedInChunk();
}
