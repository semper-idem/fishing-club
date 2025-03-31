package net.semperidem.fishingclub.fish.specimen;

import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.entity.FishDisplayBlockEntity;
import net.semperidem.fishingclub.registry.Components;
import org.ladysnake.cca.api.v3.block.BlockComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.block.BlockComponentInitializer;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

public class SpecimenDisplayComponent extends AbstractSpecimenComponent implements AutoSyncedComponent{
	private final FishDisplayBlockEntity parent;

	public static SpecimenDisplayComponent of(FishDisplayBlockEntity parent) {
		return Components.SPECIMEN_DISPLAY.get(parent);
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

		Components.SPECIMEN_DISPLAY.sync(parent);
	}

}
