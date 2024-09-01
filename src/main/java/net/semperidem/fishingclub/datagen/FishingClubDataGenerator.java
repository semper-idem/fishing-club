package net.semperidem.fishingclub.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.semperidem.fishingclub.entity.renderer.model.FishModelGenerator;

public class FishingClubDataGenerator implements DataGeneratorEntrypoint {

	@Override
	public void onInitializeDataGenerator(FabricDataGenerator generator) {
		FabricDataGenerator.Pack pack = generator.createPack();
		pack.addProvider(FCLootTableProviders::createLootTableProvider);
	}

}