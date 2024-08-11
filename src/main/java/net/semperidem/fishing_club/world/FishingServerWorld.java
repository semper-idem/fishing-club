package net.semperidem.fishing_club.world;

import net.minecraft.item.ItemStack;
import net.semperidem.fishing_club.entity.FishermanEntity;

import java.util.UUID;

public interface FishingServerWorld {

    FishermanEntity getDerek(ItemStack summonedUsing, UUID summonedBy);

    FishermanEntity getDerek();

    void setDerek(FishermanEntity fishermanEntity);
}
