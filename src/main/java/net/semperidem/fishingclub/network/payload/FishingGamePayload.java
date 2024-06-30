package net.semperidem.fishingclub.network.payload;

import static net.semperidem.fishingclub.FishingClub.getIdentifier;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.semperidem.fishingclub.fish.FishComponent;
import net.semperidem.fishingclub.item.fishing_rod.components.RodConfigurationComponent;

public record FishingGamePayload(
    FishComponent fishComponent, RodConfigurationComponent configurationComponent)
    implements CustomPayload {
  public static final CustomPayload.Id<FishingGamePayload> ID =
      new CustomPayload.Id<>(getIdentifier("s2c_fishing_game_open"));
  public static final PacketCodec<RegistryByteBuf, FishingGamePayload> CODEC =
      PacketCodec.tuple(
          FishComponent.PACKET_CODEC,
          FishingGamePayload::fishComponent,
          RodConfigurationComponent.PACKET_CODEC,
          FishingGamePayload::configurationComponent,
          FishingGamePayload::new);

  @Override
  public CustomPayload.Id<? extends CustomPayload> getId() {
    return ID;
  }
}
