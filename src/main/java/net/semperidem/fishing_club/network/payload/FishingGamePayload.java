package net.semperidem.fishing_club.network.payload;

import static net.semperidem.fishing_club.FishingClub.getIdentifier;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.semperidem.fishing_club.fish.specimen.SpecimenData;
import net.semperidem.fishing_club.item.fishing_rod.components.RodConfiguration;

public record FishingGamePayload(
	SpecimenData fishComponent, RodConfiguration configurationComponent)
    implements CustomPayload {
  public static final CustomPayload.Id<FishingGamePayload> ID =
      new CustomPayload.Id<>(getIdentifier("s2c_fishing_game_open"));
  public static final PacketCodec<RegistryByteBuf, FishingGamePayload> CODEC =
      PacketCodec.tuple(
          SpecimenData.PACKET_CODEC,
          FishingGamePayload::fishComponent,
          RodConfiguration.PACKET_CODEC,
          FishingGamePayload::configurationComponent,
          FishingGamePayload::new);

  @Override
  public CustomPayload.Id<? extends CustomPayload> getId() {
    return ID;
  }
}
