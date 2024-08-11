package net.semperidem.fishing_club.registry;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.semperidem.fishing_club.FishingClub;
import net.semperidem.fishing_club.feature.ReedFeature;

public class FCFeatures {
	public static final Identifier REED_FEATURE_ID = FishingClub.getIdentifier("reed_feature");
	public static final ReedFeature REED_FEATURE = new ReedFeature(DefaultFeatureConfig.CODEC);
	public static final RegistryKey<ConfiguredFeature<?, ?>> CONFIGURED_REED_FEATURE = RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, REED_FEATURE_ID);

	public static void register() {
		Registry.register(Registries.FEATURE, REED_FEATURE_ID, REED_FEATURE);
		BiomeModifications.addFeature(
			BiomeSelectors.foundInOverworld(),
			GenerationStep.Feature.VEGETAL_DECORATION,
			RegistryKey.of(RegistryKeys.PLACED_FEATURE, REED_FEATURE_ID
			)
		);
	}
}
