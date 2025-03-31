package net.semperidem.fishingclub.fish.specimen;

import net.semperidem.fishingclub.entity.FishDisplayBlockEntity;
import net.semperidem.fishingclub.registry.Components;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

public class SpecimenDisplayComponent extends AbstractSpecimenComponent implements AutoSyncedComponent{
	private final FishDisplayBlockEntity displayBlockEntity;

	public static SpecimenDisplayComponent of(FishDisplayBlockEntity parent) {
		return Components.SPECIMEN_DISPLAY.get(parent);
	}

	public SpecimenDisplayComponent(FishDisplayBlockEntity displayBlockEntity) {
		this.displayBlockEntity = displayBlockEntity;
	}

	@Override
	public void set(SpecimenData data) {
		super.set(data);
		Components.SPECIMEN_DISPLAY.sync(displayBlockEntity);
	}

}
