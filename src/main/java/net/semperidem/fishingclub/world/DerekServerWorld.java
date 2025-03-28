package net.semperidem.fishingclub.world;

import net.minecraft.item.ItemStack;
import net.semperidem.fishingclub.entity.FishermanEntity;

import java.util.UUID;

public interface DerekServerWorld {

    FishermanEntity getDerek(ItemStack summonedUsing, UUID summonedBy);

    FishermanEntity getDerek();

    void setDerek(FishermanEntity fishermanEntity);
}
