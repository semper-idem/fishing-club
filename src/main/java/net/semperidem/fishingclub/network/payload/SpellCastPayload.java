package net.semperidem.fishingclub.network.payload;


import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Uuids;
import net.semperidem.fishingclub.fisher.FishingCard;


import static net.semperidem.fishingclub.FishingClub.identifier;

public record SpellCastPayload(String spellName) implements CustomPayload {
    public static final CustomPayload.Id<SpellCastPayload> ID = new CustomPayload.Id<>(identifier("c2s_spell_cast"));
    public static final PacketCodec<RegistryByteBuf, SpellCastPayload> CODEC =
            PacketCodec.tuple(
                    PacketCodecs.STRING,
                    SpellCastPayload::spellName,
                    SpellCastPayload::new);

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void consumePayload(SpellCastPayload payload, ServerPlayNetworking.Context context) {
        FishingCard.of(context.player()).useTradeSecret(payload.spellName, null);
    }
}
