package net.semperidem.fishingclub.registry;

import com.mojang.serialization.Codec;
import net.minecraft.util.Uuids;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.fish.specimen.SpecimenComponent;
import net.semperidem.fishingclub.fish.specimen.SpecimenData;
import net.semperidem.fishingclub.item.FishingNetContentComponent;
import net.semperidem.fishingclub.item.fishing_rod.components.RodConfiguration;
import net.minecraft.component.ComponentType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.dynamic.Codecs;

import java.util.UUID;

public class Components {
    public static ComponentType<RodConfiguration> ROD_CONFIGURATION;
    public static ComponentType<Integer> LINE_LENGTH;
    public static ComponentType<Integer> EXPIRATION_TIME;
    public static ComponentType<Float> CAST_POWER;
    public static ComponentType<Integer> TIER;
    public static ComponentType<SpecimenData> SPECIMEN;
    public static ComponentType<Long> CREATION_TICK;
    public static ComponentType<Integer> COIN;
    public static ComponentType<UUID> CAUGHT_BY;
    public static ComponentType<FishingNetContentComponent> FISHING_NET_CONTENT;

    public static void registerRodConfigurationEarly() {
                ROD_CONFIGURATION =
                Registry.register(
                        Registries.DATA_COMPONENT_TYPE,
                        FishingClub.identifier("rod_configuration"),
                        ComponentType.<RodConfiguration>builder()
                                .codec(RodConfiguration.CODEC)
                                .packetCodec(RodConfiguration.PACKET_CODEC)
                                .cache()
                                .build());
    }

    public static void register() {
        EXPIRATION_TIME =
                Registry.register(
                        Registries.DATA_COMPONENT_TYPE,
                        FishingClub.identifier("expiration_time"),
                        ComponentType.<Integer>builder()
                                .codec(Codecs.NON_NEGATIVE_INT)
                                .packetCodec(PacketCodecs.VAR_INT)
                                .cache()
                                .build());

        LINE_LENGTH =
                Registry.register(
                        Registries.DATA_COMPONENT_TYPE,
                        FishingClub.identifier("line_length"),
                        ComponentType.<Integer>builder()
                                .codec(Codecs.NON_NEGATIVE_INT)
                                .packetCodec(PacketCodecs.VAR_INT)
                                .cache()
                                .build());

        CAST_POWER =
                Registry.register(
                        Registries.DATA_COMPONENT_TYPE,
                        FishingClub.identifier("cast_power"),
                        ComponentType.<Float>builder()
                                .codec(Codecs.POSITIVE_FLOAT)
                                .packetCodec(PacketCodecs.FLOAT)
                                .build());

        TIER =
                Registry.register(
                        Registries.DATA_COMPONENT_TYPE,
                        FishingClub.identifier("tier"),
                        ComponentType.<Integer>builder()
                                .codec(Codecs.NON_NEGATIVE_INT)
                                .packetCodec(PacketCodecs.VAR_INT)
                                .build());
        SPECIMEN =
                Registry.register(
                        Registries.DATA_COMPONENT_TYPE,
                        FishingClub.identifier(SpecimenComponent.KEY),
                        ComponentType.<SpecimenData>builder()
                                .codec(SpecimenData.CODEC)
                                .packetCodec(SpecimenData.PACKET_CODEC)
                                .build());
        CREATION_TICK =
                Registry.register(
                        Registries.DATA_COMPONENT_TYPE,
                        FishingClub.identifier("creation_tick"),
                        ComponentType.<Long>builder()
                                .codec(Codec.LONG)
                                .packetCodec(PacketCodecs.VAR_LONG)
                                .build());


        COIN =
                Registry.register(
                        Registries.DATA_COMPONENT_TYPE,
                        FishingClub.identifier("coin"),
                        ComponentType.<Integer>builder()
                                .codec(Codecs.NON_NEGATIVE_INT)
                                .packetCodec(PacketCodecs.VAR_INT)
                                .build());
        CAUGHT_BY =
          Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            FishingClub.identifier("caught_by"),
            ComponentType.<UUID>builder()
              .codec(Uuids.CODEC)
              .packetCodec(Uuids.PACKET_CODEC)
              .build());

        FISHING_NET_CONTENT =
          Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            FishingClub.identifier("fishing_net_content"),
            ComponentType.<FishingNetContentComponent>builder()
              .codec(FishingNetContentComponent.CODEC)
              .packetCodec(FishingNetContentComponent.PACKET_CODEC)
              .build());
    }
}
