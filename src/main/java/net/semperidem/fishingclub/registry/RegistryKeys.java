package net.semperidem.fishingclub.registry;

import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.fish.species.butterfish.ButterfishVariant;

public class RegistryKeys {
    public static final RegistryKey<Registry<ButterfishVariant>> BUTTERFISH_VARIANT = of("butterfish_variant");

    private static <T> RegistryKey<Registry<T>> of(String id) {
        return RegistryKey.ofRegistry(FishingClub.identifier(id));
    }


}
