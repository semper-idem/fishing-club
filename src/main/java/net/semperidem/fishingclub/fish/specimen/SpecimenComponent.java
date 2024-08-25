package net.semperidem.fishingclub.fish.specimen;

import net.minecraft.entity.passive.FishEntity;
import net.semperidem.fishingclub.FishingClub;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;

public class SpecimenComponent extends AbstractSpecimenComponent implements AutoSyncedComponent, EntityComponentInitializer{
    private FishEntity parent;

    public static SpecimenComponent of(FishEntity fishEntity) {
        return SPECIMEN.get(fishEntity);
    }

    public SpecimenComponent(FishEntity entity) {
        this.parent = entity;
    }

    @Override
    public void set(SpecimenData data) {
        super.set(data);

        if (parent == null) {
            return;
        }

        SPECIMEN.sync(parent);
    }

    private static final ComponentKey<SpecimenComponent> SPECIMEN = ComponentRegistry.getOrCreate(FishingClub.identifier("specimen_component"), SpecimenComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerFor(FishEntity.class, SPECIMEN, SpecimenComponent::new);
    }

    public SpecimenComponent() {}
}
