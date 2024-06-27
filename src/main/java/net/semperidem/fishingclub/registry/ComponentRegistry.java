package net.semperidem.fishingclub.registry;

import com.mojang.serialization.Codec;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.item.fishing_rod.components.RodConfigurationComponent;
import net.semperidem.fishingclub.item.fishing_rod.components.RodPartComponent;
import net.minecraft.component.ComponentType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.dynamic.Codecs;

public class ComponentRegistry {
    public static ComponentType<RodConfigurationComponent> ROD_CONFIGURATION ;
    public static ComponentType<RodPartComponent> ROD_PART;
    public static ComponentType<Integer> LINE_LENGTH;
    public static ComponentType<Boolean> BROKEN;
    public static ComponentType<Integer> PART_QUALITY;

    public static void register() {
        ROD_CONFIGURATION = Registry.register(Registries.DATA_COMPONENT_TYPE, FishingClub.getIdentifier("rod_configuration"),
                ComponentType.<RodConfigurationComponent>builder().codec(RodConfigurationComponent.CODEC).packetCodec(RodConfigurationComponent.PACKET_CODEC).build()
        );

        ROD_PART = Registry.register(Registries.DATA_COMPONENT_TYPE, FishingClub.getIdentifier("rod_part"),
                ComponentType.<RodPartComponent>builder().codec(RodPartComponent.CODEC).packetCodec(RodPartComponent.PACKET_CODEC).build()
        );

        LINE_LENGTH = Registry.register(Registries.DATA_COMPONENT_TYPE, FishingClub.getIdentifier("line_length"),
                ComponentType.<Integer>builder().codec(Codecs.NONNEGATIVE_INT).packetCodec(PacketCodecs.VAR_INT).build()
        );

        BROKEN = Registry.register(Registries.DATA_COMPONENT_TYPE, FishingClub.getIdentifier("part_broken"),
                ComponentType.<Boolean>builder().codec(Codec.BOOL).packetCodec(PacketCodecs.BOOL).build()
        );

        PART_QUALITY = Registry.register(Registries.DATA_COMPONENT_TYPE, FishingClub.getIdentifier("part_quality"),
                ComponentType.<Integer>builder().codec(Codecs.NONNEGATIVE_INT).packetCodec(PacketCodecs.VAR_INT).build()
        );
    }

}
