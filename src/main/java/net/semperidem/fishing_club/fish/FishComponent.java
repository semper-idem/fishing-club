package net.semperidem.fishing_club.fish;

import com.mojang.serialization.Dynamic;
import net.minecraft.entity.passive.FishEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import net.semperidem.fishing_club.FishingClub;
import net.semperidem.fishing_club.entity.FCFishEntity;
import net.semperidem.fishing_club.entity.FishDisplayBlockEntity;
import net.semperidem.fishing_club.entity.renderer.model.FCFishEntityModel;
import net.semperidem.fishing_club.registry.FCModels;
import org.ladysnake.cca.api.v3.block.BlockComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.block.BlockComponentInitializer;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;

public class FishComponent implements BlockComponentInitializer, EntityComponentInitializer, AutoSyncedComponent {
    FishDisplayBlockEntity displayBlockEntity;
    FCFishEntity fishEntity;
    FishRecord fishRecord;

    float weightScale = 1;
    float lengthScale = 1;
    boolean isAlbino = false;
    FCFishEntityModel<FCFishEntity> model;
    Identifier texture;

    public static final ComponentKey<FishComponent> FISH_COMPONENT = ComponentRegistry.getOrCreate(
      FishingClub.getIdentifier("fish_component"), FishComponent.class);

    public FishComponent(FishDisplayBlockEntity displayBlockEntity) {
        this.displayBlockEntity = displayBlockEntity;
    }

    public float weightScale() {
        return this.weightScale;
    }

    public float lengthScale() {
        return this.lengthScale;
    }

    public FCFishEntityModel<FCFishEntity> model() {
        return this.model;
    }

    public Identifier texture() {
        return this.texture;
    }

    public void calculateModelVariant(FishRecord fishRecord) {
        if (!this.fishEntity.getWorld().isClient()) {
            return;
        }
        var species = SpeciesLibrary.ALL_FISH_TYPES.get(fishRecord.speciesName());
        this.lengthScale = FishRecord.getLengthScale(species, fishRecord.length());
        this.weightScale = FishRecord.getWeightScale(species, fishRecord.weight());
        this.isAlbino = fishRecord.isAlbino();
        this.model = FCModels.getModel(fishRecord);
        this.texture = FCModels.getTexture(fishRecord);
    }


    public static FishComponent of(FishEntity fishEntity) {
        return FISH_COMPONENT.get(fishEntity);
    }

    public static FishComponent of(FishDisplayBlockEntity displayBlock) {
        return FISH_COMPONENT.get(displayBlock);
    }

    @Override
    public void applySyncPacket(RegistryByteBuf buf) {
        AutoSyncedComponent.super.applySyncPacket(buf);
    }

    public FishComponent() {}

    public FishComponent(FCFishEntity entity) {
        this.fishEntity = entity;
    }

    public void set(FishRecord fishRecord) {
        this.fishRecord = fishRecord;
        if (this.fishEntity != null) {
            FISH_COMPONENT.sync(this.fishEntity);
            this.calculateModelVariant(fishRecord);
        }
        if (this.displayBlockEntity != null) {
            FISH_COMPONENT.sync(this.displayBlockEntity);
        }
    }

    public FishRecord record() {
        return this.fishRecord;
    }

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerFor(FCFishEntity.class, FISH_COMPONENT, FishComponent::new);
    }

    @Override
    public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        if (!tag.contains("fish")) {
            return;
        }
        FishRecord.CODEC.parse(new Dynamic<>(NbtOps.INSTANCE, tag.get("fish"))).resultOrPartial().ifPresent(
          fishRecord -> this.fishRecord = fishRecord
        );
    }

    @Override
    public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        if (fishRecord == null) {
            return;
        }
        FishRecord.CODEC.encodeStart(NbtOps.INSTANCE, this.fishRecord).resultOrPartial().ifPresent(fishTag -> {
            tag.put("fish", fishTag);
        });
    }

    @Override
    public void registerBlockComponentFactories(BlockComponentFactoryRegistry registry) {
        registry.registerFor(FishDisplayBlockEntity.class, FISH_COMPONENT, FishComponent::new);

    }

}
