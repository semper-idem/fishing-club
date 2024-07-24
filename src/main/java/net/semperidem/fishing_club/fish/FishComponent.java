package net.semperidem.fishing_club.fish;

import com.mojang.serialization.Dynamic;
import net.minecraft.entity.passive.FishEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.semperidem.fishing_club.FishingClub;
import net.semperidem.fishing_club.entity.FCFishEntity;
import net.semperidem.fishing_club.entity.FishDisplayBlockEntity;
import net.semperidem.fishing_club.entity.renderer.FCFishEntityRenderer;
import net.semperidem.fishing_club.entity.renderer.model.FCFishEntityModel;
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
    public static final float SCALE_RANGE = 0.4f;
    private static final float HALF_SCALE_RANGE = SCALE_RANGE * 0.5f;
    Pair<FCFishEntityModel<FCFishEntity>, Identifier> modelAndTexture;

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

    public Pair<FCFishEntityModel<FCFishEntity>, Identifier> getModelAndTexture() {
        return this.modelAndTexture;
    }

    public void calculateModelVariant(FishRecord fishRecord) {
        if (!this.fishEntity.getWorld().isClient()) {
            return;
        }
        Species species = SpeciesLibrary.ALL_FISH_TYPES.get(fishRecord.speciesName());
        this.lengthScale = getLengthScale(species, fishRecord.length());
        this.weightScale = getWeightScale(species, fishRecord.weight());
        this.isAlbino = fishRecord.isAlbino();
        this.modelAndTexture = FCFishEntityRenderer.getModelAndTexture(this.fishRecord);
    }

    public static float getLengthScale(Species species, float length) {
        return  1 - HALF_SCALE_RANGE + ((length - species.fishMinLength) / species.fishRandomLength * SCALE_RANGE);
    }

    public static float getWeightScale(Species species, float weight) {
        return  1 - HALF_SCALE_RANGE + ((weight - species.fishMinLength) / species.fishRandomLength * SCALE_RANGE);
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
