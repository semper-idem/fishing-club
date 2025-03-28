package net.semperidem.fishingclub.network.payload;


import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.semperidem.fishingclub.fisher.Card;


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
        Card.of(context.player()).useTradeSecret(payload.spellName, null);
    }
}
