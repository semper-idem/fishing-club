package net.semperidem.fishing_club;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.semperidem.fishing_club.entity.renderer.model.FishModelGenerator;

public class FishingClubDataGenerator implements DataGeneratorEntrypoint {

	@Override
	public void onInitializeDataGenerator(FabricDataGenerator generator) {
		FabricDataGenerator.Pack pack = generator.createPack();
		pack.addProvider(FishModelGenerator::new);

		// Adding a provider example:
		//
		// pack.addProvider(AdvancementsProvider::new);
	}

}