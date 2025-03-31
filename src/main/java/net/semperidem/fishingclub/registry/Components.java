package net.semperidem.fishingclub.registry;

import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Uuids;
import net.minecraft.util.dynamic.Codecs;
import net.semperidem.fishingclub.entity.FishDisplayBlockEntity;
import net.semperidem.fishingclub.fish.specimen.SpecimenComponent;
import net.semperidem.fishingclub.fish.specimen.SpecimenData;
import net.semperidem.fishingclub.fish.specimen.SpecimenDisplayComponent;
import net.semperidem.fishingclub.fisher.Card;
import net.semperidem.fishingclub.fisher.FishingKing;
import net.semperidem.fishingclub.item.NetContentComponent;
import net.semperidem.fishingclub.item.fishing_rod.components.RodConfiguration;
import net.semperidem.fishingclub.leaderboard.LeaderboardTracker;
import net.semperidem.fishingclub.world.ChunkQuality;
import org.jetbrains.annotations.Nullable;
import org.ladysnake.cca.api.v3.block.BlockComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.block.BlockComponentInitializer;
import org.ladysnake.cca.api.v3.chunk.ChunkComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.chunk.ChunkComponentInitializer;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;
import org.ladysnake.cca.api.v3.scoreboard.ScoreboardComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.scoreboard.ScoreboardComponentInitializer;

import java.util.UUID;

import static net.semperidem.fishingclub.FishingClub.identifier;

public class Components implements
        EntityComponentInitializer,
        BlockComponentInitializer,
        ScoreboardComponentInitializer,
        ChunkComponentInitializer {

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
    public static final ComponentKey<FishingKing> FISHING_KING = ComponentRegistry
            .getOrCreate(
                    identifier(FishingKing.TAG_KEY),
                    FishingKing.class
            );
    public static final ComponentKey<ChunkQuality> CHUNK_QUALITY = ComponentRegistry
            .getOrCreate(
                    identifier(ChunkQuality.TAG_KEY),
                    ChunkQuality.class
            );
    public static final ComponentKey<LeaderboardTracker> LEADERBOARD_TRACKER = ComponentRegistry
            .getOrCreate(
                    identifier(LeaderboardTracker.TAG_KEY),
                    LeaderboardTracker.class
            );

    public static ComponentType<RodConfiguration> ROD_CONFIGURATION;
    public static ComponentType<Integer> LINE_LENGTH;
    public static ComponentType<Integer> EXPIRATION_TIME;
    public static ComponentType<Float> CAST_POWER;
    public static ComponentType<Integer> TIER;
    public static ComponentType<SpecimenData> SPECIMEN_DATA;
    public static ComponentType<Long> CREATION_TICK;
    public static ComponentType<Integer> COIN;
    public static ComponentType<UUID> CAUGHT_BY;
    public static ComponentType<NetContentComponent> NET_CONTENT;

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

        NET_CONTENT =
                Registry.register(
                        Registries.DATA_COMPONENT_TYPE,
                        identifier("net_content"),
                        ComponentType.<NetContentComponent>builder()
                                .codec(NetContentComponent.CODEC)
                                .packetCodec(NetContentComponent.PACKET_CODEC)
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

    @Override
    public void registerScoreboardComponentFactories(ScoreboardComponentFactoryRegistry registry) {
        registry.registerScoreboardComponent(FISHING_KING, FishingKing::new);
        registry.registerScoreboardComponent(LEADERBOARD_TRACKER, (Scoreboard scoreboard, @Nullable MinecraftServer server) -> new LeaderboardTracker(scoreboard));
    }

    @Override
    public void registerChunkComponentFactories(ChunkComponentFactoryRegistry registry) {
        registry.register(CHUNK_QUALITY, ChunkQuality::new);
    }
}
