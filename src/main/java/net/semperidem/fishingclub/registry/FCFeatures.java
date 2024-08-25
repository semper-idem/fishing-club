package net.semperidem.fishingclub.registry;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnLocationTypes;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.block.DuckweedBlock;
import net.semperidem.fishingclub.block.ReedBlock;
import net.semperidem.fishingclub.feature.DuckweedFeature;
import net.semperidem.fishingclub.feature.ReedFeature;
import net.semperidem.fishingclub.fish.Species;

public class FCFeatures {
	public static final Identifier REED_FEATURE_ID = FishingClub.getIdentifier("reed_feature");
	public static final ReedFeature REED_FEATURE = new ReedFeature(DefaultFeatureConfig.CODEC);
	public static final RegistryKey<ConfiguredFeature<?, ?>> CONFIGURED_REED_FEATURE = RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, REED_FEATURE_ID);
	public static final Identifier DUCKWEED_FEATURE_ID = FishingClub.getIdentifier("duckweed_feature");
	public static final DuckweedFeature DUCKWEED_FEATURE = new DuckweedFeature(DefaultFeatureConfig.CODEC);
	public static final RegistryKey<ConfiguredFeature<?, ?>> CONFIGURED_DUCKWEED_FEATURE = RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, DUCKWEED_FEATURE_ID);

	public static void register() {
		Registry.register(Registries.FEATURE, DUCKWEED_FEATURE_ID, DUCKWEED_FEATURE);
		BiomeModifications.addFeature(
			BiomeSelectors.foundInOverworld().and(DuckweedBlock::growsInBiome),
			GenerationStep.Feature.VEGETAL_DECORATION,
			RegistryKey.of(RegistryKeys.PLACED_FEATURE, DUCKWEED_FEATURE_ID
			)
		);
		Registry.register(Registries.FEATURE, REED_FEATURE_ID, REED_FEATURE);
		BiomeModifications.addFeature(
			BiomeSelectors.foundInOverworld().and(ReedBlock::growsInBiome),
			GenerationStep.Feature.VEGETAL_DECORATION,
			RegistryKey.of(RegistryKeys.PLACED_FEATURE, REED_FEATURE_ID
			)
		);
	}
}
