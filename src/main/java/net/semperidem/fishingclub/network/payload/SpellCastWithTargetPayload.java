package net.semperidem.fishingclub.network.payload;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Uuids;
import net.semperidem.fishingclub.fisher.FishingCard;

import java.util.UUID;

import static net.semperidem.fishingclub.FishingClub.identifier;

public record SpellCastWithTargetPayload(String spellName, UUID targetUUID) implements CustomPayload {
    public static final CustomPayload.Id<SpellCastWithTargetPayload> ID = new CustomPayload.Id<>(identifier("c2s_spell_cast_target"));
    public static final PacketCodec<RegistryByteBuf, SpellCastWithTargetPayload> CODEC =
            PacketCodec.tuple(
                    PacketCodecs.STRING,
                    SpellCastWithTargetPayload::spellName,
                    Uuids.PACKET_CODEC,
                    SpellCastWithTargetPayload::targetUUID,
                    SpellCastWithTargetPayload::new);

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void consumePayload(SpellCastWithTargetPayload payload, ServerPlayNetworking.Context context) {
        try (MinecraftServer server = context.server()) {
            FishingCard.of(context.player()).useSpell(payload.spellName, server.getPlayerManager().getPlayer(payload.targetUUID));
        }
    }
}
