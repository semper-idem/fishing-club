package net.semperidem.fishingclub.fish.specimen;

import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.entity.passive.TropicalFishEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.fish.Species;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;

public class SpecimenComponent extends AbstractSpecimenComponent implements AutoSyncedComponent, EntityComponentInitializer{
    private WaterCreatureEntity owner;

    public static SpecimenComponent of(WaterCreatureEntity fishEntity) {
        return SPECIMEN.get(fishEntity);
    }

    public SpecimenComponent(WaterCreatureEntity entity) {
        this.owner = entity;
    }

    @Override
    public void set(SpecimenData data) {
        super.set(data);

        if (owner == null) {
            return;
        }

        SPECIMEN.sync(owner);
    }

    private static final ComponentKey<SpecimenComponent> SPECIMEN = ComponentRegistry.getOrCreate(FishingClub.identifier("specimen_component"), SpecimenComponent.class);

    @Override
    public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        this.initIfValid();
        super.writeToNbt(tag, registryLookup);
    }

    private void initIfValid() {
        if (owner == null) {
            return;
        }
        if (data != null) {
            return;
        }
        if (owner instanceof TropicalFishEntity tropicalFishEntity) {
            NbtCompound nbt = new NbtCompound();
            tropicalFishEntity.writeCustomDataToNbt(nbt);
            int variantId = nbt.getInt("Variant", 0);
            TropicalFishEntity.Variant variant = new TropicalFishEntity.Variant(variantId);
            if (!TropicalFishEntity.COMMON_VARIANTS.contains(variant)) {
                System.out.println("Found variant that doesn't match common variants");
                return;
            }
            this.set(SpecimenData.init(Species.Library.TROPICAL_FISH, TropicalFishEntity.COMMON_VARIANTS.indexOf(variant)));
            return;
        }
        this.set(SpecimenData.init(Species.Library.fromName(owner.getType().getUntranslatedName())));
    }


    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerFor(WaterCreatureEntity.class, SPECIMEN, SpecimenComponent::new);
    }

    public SpecimenComponent() {}
}
