package net.semperidem.fishingclub.entity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.biome.Biome;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.item.fishing_rod.components.RodConfiguration;

import java.util.Optional;


public interface IHookEntity {

    default Random getRandom() {
        return Random.create();
    }

    default FishingCard getFishingCard() {
        return FishingCard.DEFAULT;
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
