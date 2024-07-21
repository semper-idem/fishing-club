package net.semperidem.fishing_club.registry;

import com.mojang.serialization.Codec;
import net.minecraft.util.Uuids;
import net.semperidem.fishing_club.FishingClub;
import net.semperidem.fishing_club.fish.FishRecord;
import net.semperidem.fishing_club.item.FishingNetContentComponent;
import net.semperidem.fishing_club.item.fishing_rod.components.RodConfiguration;
import net.minecraft.component.ComponentType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.dynamic.Codecs;

import java.util.UUID;

public class FCComponents {
    public static ComponentType<RodConfiguration> ROD_CONFIGURATION;
    public static ComponentType<Integer> LINE_LENGTH;
    public static ComponentType<Float> CAST_POWER;
    public static ComponentType<Boolean> BROKEN;
    public static ComponentType<Integer> PART_QUALITY;
    public static ComponentType<Integer> TIER;
    public static ComponentType<FishRecord> FISH;
    public static ComponentType<Long> CREATION_TICK;
    public static ComponentType<Integer> COIN;
    public static ComponentType<UUID> CAUGHT_BY;
    public static ComponentType<FishingNetContentComponent> FISHING_NET_CONTENT;

    public static void register() {
        ROD_CONFIGURATION =
                Registry.register(
                        Registries.DATA_COMPONENT_TYPE,
                        FishingClub.getIdentifier("rod_configuration"),
                        ComponentType.<RodConfiguration>builder()
                                .codec(RodConfiguration.CODEC)
                                .packetCodec(RodConfiguration.PACKET_CODEC)
                                .cache()
                                .build());

        LINE_LENGTH =
                Registry.register(
                        Registries.DATA_COMPONENT_TYPE,
                        FishingClub.getIdentifier("line_length"),
                        ComponentType.<Integer>builder()
                                .codec(Codecs.NONNEGATIVE_INT)
                                .packetCodec(PacketCodecs.VAR_INT)
                                .cache()
                                .build());

        CAST_POWER =
                Registry.register(
                        Registries.DATA_COMPONENT_TYPE,
                        FishingClub.getIdentifier("cast_power"),
                        ComponentType.<Float>builder()
                                .codec(Codecs.POSITIVE_FLOAT)
                                .packetCodec(PacketCodecs.FLOAT)
                                .build());

        BROKEN =
                Registry.register(
                        Registries.DATA_COMPONENT_TYPE,
                        FishingClub.getIdentifier("part_broken"),
                        ComponentType.<Boolean>builder()
                                .codec(Codec.BOOL)
                                .packetCodec(PacketCodecs.BOOL)
                                .build());

        PART_QUALITY =
                Registry.register(
                        Registries.DATA_COMPONENT_TYPE,
                        FishingClub.getIdentifier("part_quality"),
                        ComponentType.<Integer>builder()
                                .codec(Codecs.NONNEGATIVE_INT)
                                .packetCodec(PacketCodecs.VAR_INT)
                                .build());

        TIER =
                Registry.register(
                        Registries.DATA_COMPONENT_TYPE,
                        FishingClub.getIdentifier("tier"),
                        ComponentType.<Integer>builder()
                                .codec(Codecs.NONNEGATIVE_INT)
                                .packetCodec(PacketCodecs.VAR_INT)
                                .build());
        FISH =
                Registry.register(
                        Registries.DATA_COMPONENT_TYPE,
                        FishingClub.getIdentifier("fish"),
                        ComponentType.<FishRecord>builder()
                                .codec(FishRecord.CODEC)
                                .packetCodec(FishRecord.PACKET_CODEC)
                                .build());
        CREATION_TICK =
                Registry.register(
                        Registries.DATA_COMPONENT_TYPE,
                        FishingClub.getIdentifier("creation_tick"),
                        ComponentType.<Long>builder()
                                .codec(Codec.LONG)
                                .packetCodec(PacketCodecs.VAR_LONG)
                                .build());


        COIN =
                Registry.register(
                        Registries.DATA_COMPONENT_TYPE,
                        FishingClub.getIdentifier("coin"),
                        ComponentType.<Integer>builder()
                                .codec(Codecs.NONNEGATIVE_INT)
                                .packetCodec(PacketCodecs.VAR_INT)
                                .build());
        CAUGHT_BY =
          Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            FishingClub.getIdentifier("caught_by"),
            ComponentType.<UUID>builder()
              .codec(Uuids.CODEC)
              .packetCodec(Uuids.PACKET_CODEC)
              .build());

        FISHING_NET_CONTENT =
          Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            FishingClub.getIdentifier("fishing_net_content"),
            ComponentType.<FishingNetContentComponent>builder()
              .codec(FishingNetContentComponent.CODEC)
              .packetCodec(FishingNetContentComponent.PACKET_CODEC)
              .build());
    }
}
