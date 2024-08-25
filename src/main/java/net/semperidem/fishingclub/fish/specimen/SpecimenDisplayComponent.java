package net.semperidem.fishingclub.fish.specimen;

import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.entity.FishDisplayBlockEntity;
import org.ladysnake.cca.api.v3.block.BlockComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.block.BlockComponentInitializer;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

public class SpecimenDisplayComponent extends AbstractSpecimenComponent implements AutoSyncedComponent, BlockComponentInitializer {
	private FishDisplayBlockEntity parent;

	public static SpecimenDisplayComponent of(FishDisplayBlockEntity parent) {
		return SPECIMEN_DISPLAY.get(parent);
	}

	public SpecimenDisplayComponent(FishDisplayBlockEntity parent) {
		this.parent = parent;
	}

	@Override
	public void set(SpecimenData data) {
		super.set(data);

		if (parent == null) {
			return;
		}

		SPECIMEN_DISPLAY.sync(parent);
	}

	private static final ComponentKey<SpecimenDisplayComponent> SPECIMEN_DISPLAY = ComponentRegistry.getOrCreate(FishingClub.identifier("specimen_display_component"), SpecimenDisplayComponent.class);

	@Override
	public void registerBlockComponentFactories(BlockComponentFactoryRegistry registry) {
		registry.registerFor(FishDisplayBlockEntity.class, SPECIMEN_DISPLAY, SpecimenDisplayComponent::new);
	}

	public SpecimenDisplayComponent() {}

}
