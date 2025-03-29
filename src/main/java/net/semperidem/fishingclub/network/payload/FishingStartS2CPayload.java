package net.semperidem.fishingclub.network.payload;

import static net.semperidem.fishingclub.FishingClub.identifier;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.semperidem.fishingclub.fish.specimen.SpecimenData;
import net.semperidem.fishingclub.item.fishing_rod.components.RodConfiguration;

public record FishingStartS2CPayload(
	SpecimenData fishComponent, RodConfiguration configurationComponent)
    implements CustomPayload {
  public static final CustomPayload.Id<FishingStartS2CPayload> ID =
      new CustomPayload.Id<>(identifier("s2c_fishing_start"));
  public static final PacketCodec<RegistryByteBuf, FishingStartS2CPayload> CODEC =
      PacketCodec.tuple(
          SpecimenData.PACKET_CODEC,
          FishingStartS2CPayload::fishComponent,
          RodConfiguration.PACKET_CODEC,
          FishingStartS2CPayload::configurationComponent,
          FishingStartS2CPayload::new);

  @Override
  public CustomPayload.Id<? extends CustomPayload> getId() {
    return ID;
  }
}
