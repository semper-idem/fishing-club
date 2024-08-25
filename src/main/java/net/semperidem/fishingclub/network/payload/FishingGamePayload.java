package net.semperidem.fishingclub.network.payload;

import static net.semperidem.fishingclub.FishingClub.identifier;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.semperidem.fishingclub.fish.specimen.SpecimenData;
import net.semperidem.fishingclub.item.fishing_rod.components.RodConfiguration;

public record FishingGamePayload(
	SpecimenData fishComponent, RodConfiguration configurationComponent)
    implements CustomPayload {
  public static final CustomPayload.Id<FishingGamePayload> ID =
      new CustomPayload.Id<>(identifier("s2c_fishing_game_open"));
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
