package net.semperidem.fishingclub.fish.specimen;

import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.entity.passive.TropicalFishEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.fish.Species;
import net.semperidem.fishingclub.registry.Components;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;

public class SpecimenComponent extends AbstractSpecimenComponent {
    private final WaterCreatureEntity owner;

    public static SpecimenComponent of(WaterCreatureEntity fishEntity) {
        return Components.SPECIMEN.get(fishEntity);
    }

    public SpecimenComponent(WaterCreatureEntity entity) {
        this.owner = entity;
    }

    @Override
    public void set(SpecimenData data) {
        super.set(data);
        Components.SPECIMEN.sync(owner);
    }

    @Override
    public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        initIfEmpty();
        super.writeToNbt(tag, registryLookup);
    }

    private void initIfEmpty() {
        if (!isEmpty()) {
            return;
        }
        if (owner instanceof TropicalFishEntity tropicalFishEntity) {
            this.set(SpecimenData.init(Species.Library.TROPICAL_FISH, getTropicalFishVariant(tropicalFishEntity)));
            return;
        }
        this.set(SpecimenData.init(Species.Library.fromName(owner.getType().getUntranslatedName())));
    }

    private int getTropicalFishVariant(TropicalFishEntity tropicalFishEntity) {
        NbtCompound nbt = new NbtCompound();
        tropicalFishEntity.writeCustomDataToNbt(nbt);
        int variantId = nbt.getInt("Variant", 0);
        TropicalFishEntity.Variant variant = new TropicalFishEntity.Variant(variantId);
        if (!TropicalFishEntity.COMMON_VARIANTS.contains(variant)) {
            System.out.println("Found variant that doesn't match common variants");
            return 0;
        }
        return TropicalFishEntity.COMMON_VARIANTS.indexOf(variant);
    }

}
