package net.semperidem.fishingclub.network.payload;

import static net.semperidem.fishingclub.FishingClub.identifier;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.semperidem.fishingclub.fish.specimen.SpecimenData;
import net.semperidem.fishingclub.item.fishing_rod.components.RodConfiguration;

public record FishingGameStartS2CPayload(
	SpecimenData fishComponent, RodConfiguration configurationComponent)
    implements CustomPayload {
  public static final CustomPayload.Id<FishingGameStartS2CPayload> ID =
      new CustomPayload.Id<>(identifier("s2c_fishing_game_open"));
  public static final PacketCodec<RegistryByteBuf, FishingGameStartS2CPayload> CODEC =
      PacketCodec.tuple(
          SpecimenData.PACKET_CODEC,
          FishingGameStartS2CPayload::fishComponent,
          RodConfiguration.PACKET_CODEC,
          FishingGameStartS2CPayload::configurationComponent,
          FishingGameStartS2CPayload::new);

  @Override
  public CustomPayload.Id<? extends CustomPayload> getId() {
    return ID;
  }
}
