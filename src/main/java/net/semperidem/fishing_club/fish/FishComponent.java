package net.semperidem.fishing_club.fish;

import com.mojang.serialization.Dynamic;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.FishEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.registry.RegistryWrapper;
import net.semperidem.fishing_club.FishingClub;
import net.semperidem.fishing_club.entity.FishDisplayBlockEntity;
import org.ladysnake.cca.api.v3.block.BlockComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.block.BlockComponentInitializer;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;

public class FishComponent implements BlockComponentInitializer, EntityComponentInitializer, AutoSyncedComponent {
    FishDisplayBlockEntity displayBlockEntity;
    FishEntity fishEntity;
    FishRecord fishRecord;
    public static final ComponentKey<FishComponent> FISH_COMPONENT = ComponentRegistry.getOrCreate(
      FishingClub.getIdentifier("fish_component"), FishComponent.class);

    public FishComponent(FishDisplayBlockEntity displayBlockEntity) {
        this.displayBlockEntity = displayBlockEntity;
    }


    public static FishComponent of(FishEntity fishEntity) {
        return FISH_COMPONENT.get(fishEntity);
    }

    @Override
    public void applySyncPacket(RegistryByteBuf buf) {
        AutoSyncedComponent.super.applySyncPacket(buf);
    }

    public FishComponent() {}

    public FishComponent(Entity entity) {
        this.fishEntity = (FishEntity) entity;

    }

    public void set(FishRecord fishRecord) {
        this.fishRecord = fishRecord;
        if (this.fishEntity != null) {
            FISH_COMPONENT.sync(this.fishEntity);
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
        registry.registerFor(FishEntity.class, FISH_COMPONENT, FishComponent::new);
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
