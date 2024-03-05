package net.semperidem.fishingclub;

import net.minecraft.item.ItemStack;
import net.semperidem.fishingclub.entity.FishermanEntity;

import java.util.UUID;

public interface FishingServerWorld {

    FishermanEntity getDerek(ItemStack summonedUsing, UUID summonedBy);

    FishermanEntity getDerek();

    void setDerek(FishermanEntity fishermanEntity);
}
