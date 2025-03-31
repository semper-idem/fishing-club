package net.semperidem.fishingclub.registry;

import com.mojang.serialization.Codec;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.util.Uuids;
import net.semperidem.fishingclub.entity.FishDisplayBlockEntity;
import net.semperidem.fishingclub.fish.specimen.SpecimenComponent;
import net.semperidem.fishingclub.fish.specimen.SpecimenData;
import net.semperidem.fishingclub.fish.specimen.SpecimenDisplayComponent;
import net.semperidem.fishingclub.fisher.Card;
import net.semperidem.fishingclub.item.FishingNetContentComponent;
import net.semperidem.fishingclub.item.fishing_rod.components.RodConfiguration;
import net.minecraft.component.ComponentType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.dynamic.Codecs;
import org.ladysnake.cca.api.v3.block.BlockComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.block.BlockComponentInitializer;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;

import java.util.UUID;

import static net.semperidem.fishingclub.FishingClub.identifier;

public class Components implements EntityComponentInitializer, BlockComponentInitializer {
    public static ComponentType<RodConfiguration> ROD_CONFIGURATION;
    public static ComponentType<Integer> LINE_LENGTH;
    public static ComponentType<Integer> EXPIRATION_TIME;
    public static ComponentType<Float> CAST_POWER;
    public static ComponentType<Integer> TIER;
    public static ComponentType<SpecimenData> SPECIMEN_DATA;
    public static ComponentType<Long> CREATION_TICK;
    public static ComponentType<Integer> COIN;
    public static ComponentType<UUID> CAUGHT_BY;
    public static ComponentType<FishingNetContentComponent> FISHING_NET_CONTENT;

    public static final ComponentKey<Card> CARD = ComponentRegistry
            .getOrCreate(
                    identifier("card"),
                    Card.class
            );
    public static final ComponentKey<SpecimenComponent> SPECIMEN = ComponentRegistry
            .getOrCreate(
                    identifier(SpecimenComponent.TAG_KEY),
                    SpecimenComponent.class
            );
    public static final ComponentKey<SpecimenDisplayComponent> SPECIMEN_DISPLAY = ComponentRegistry
            .getOrCreate(
                    identifier(SpecimenComponent.TAG_KEY + "_displayed"),
                    SpecimenDisplayComponent.class
            );

    public static void register() {
        ROD_CONFIGURATION =
                Registry.register(
                        Registries.DATA_COMPONENT_TYPE,
                        identifier("rod_configuration"),
                        ComponentType.<RodConfiguration>builder()
                                .codec(RodConfiguration.CODEC)
                                .packetCodec(RodConfiguration.PACKET_CODEC)
                                .cache()
                                .build());
        EXPIRATION_TIME =
                Registry.register(
                        Registries.DATA_COMPONENT_TYPE,
                        identifier("expiration_time"),
                        ComponentType.<Integer>builder()
                                .codec(Codecs.NON_NEGATIVE_INT)
                                .packetCodec(PacketCodecs.VAR_INT)
                                .cache()
                                .build());

        LINE_LENGTH =
                Registry.register(
                        Registries.DATA_COMPONENT_TYPE,
                        identifier("line_length"),
                        ComponentType.<Integer>builder()
                                .codec(Codecs.NON_NEGATIVE_INT)
                                .packetCodec(PacketCodecs.VAR_INT)
                                .cache()
                                .build());

        CAST_POWER =
                Registry.register(
                        Registries.DATA_COMPONENT_TYPE,
                        identifier("cast_power"),
                        ComponentType.<Float>builder()
                                .codec(Codecs.POSITIVE_FLOAT)
                                .packetCodec(PacketCodecs.FLOAT)
                                .build());

        TIER =
                Registry.register(
                        Registries.DATA_COMPONENT_TYPE,
                        identifier("tier"),
                        ComponentType.<Integer>builder()
                                .codec(Codecs.NON_NEGATIVE_INT)
                                .packetCodec(PacketCodecs.VAR_INT)
                                .build());
        SPECIMEN_DATA =
                Registry.register(
                        Registries.DATA_COMPONENT_TYPE,
                        identifier(SpecimenComponent.TAG_KEY),
                        ComponentType.<SpecimenData>builder()
                                .codec(SpecimenData.CODEC)
                                .packetCodec(SpecimenData.PACKET_CODEC)
                                .build());
        CREATION_TICK =
                Registry.register(
                        Registries.DATA_COMPONENT_TYPE,
                        identifier("creation_tick"),
                        ComponentType.<Long>builder()
                                .codec(Codec.LONG)
                                .packetCodec(PacketCodecs.VAR_LONG)
                                .build());


        COIN =
                Registry.register(
                        Registries.DATA_COMPONENT_TYPE,
                        identifier("coin"),
                        ComponentType.<Integer>builder()
                                .codec(Codecs.NON_NEGATIVE_INT)
                                .packetCodec(PacketCodecs.VAR_INT)
                                .build());
        CAUGHT_BY =
          Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            identifier("caught_by"),
            ComponentType.<UUID>builder()
              .codec(Uuids.CODEC)
              .packetCodec(Uuids.PACKET_CODEC)
              .build());

        FISHING_NET_CONTENT =
          Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            identifier("fishing_net_content"),
            ComponentType.<FishingNetContentComponent>builder()
              .codec(FishingNetContentComponent.CODEC)
              .packetCodec(FishingNetContentComponent.PACKET_CODEC)
              .build());
    }

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
            registry.registerForPlayers(Components.CARD, Card::new, RespawnCopyStrategy.ALWAYS_COPY);
            registry.registerFor(WaterCreatureEntity.class, SPECIMEN, SpecimenComponent::new);
    }

    @Override
    public void registerBlockComponentFactories(BlockComponentFactoryRegistry registry) {
        registry.registerFor(FishDisplayBlockEntity.class, SPECIMEN_DISPLAY, SpecimenDisplayComponent::new);
    }
}
