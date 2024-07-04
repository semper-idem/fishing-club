package net.semperidem.fishingclub.network.payload;

import com.mojang.serialization.codecs.PrimitiveCodec;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Uuids;
import net.semperidem.fishingclub.client.screen.fishing_card.FishingCardScreenFactory;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.fisher.perks.spells.Spell;

import java.util.UUID;

import static net.semperidem.fishingclub.FishingClub.getIdentifier;

public record SpellCastPayload(String spellName, UUID targetUUID) implements CustomPayload {
    public static final CustomPayload.Id<SpellCastPayload> ID = new CustomPayload.Id<>(getIdentifier("c2s_spell_cast"));
    public static final PacketCodec<RegistryByteBuf, SpellCastPayload> CODEC =
            PacketCodec.tuple(PacketCodecs.STRING, SpellCastPayload::spellName, Uuids.PACKET_CODEC, SpellCastPayload::targetUUID, SpellCastPayload::new);

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void consumePayload(SpellCastPayload payload, ServerPlayNetworking.Context context) {
        try (MinecraftServer server = context.server()) {
            FishingCard.of(context.player()).useSpell(payload.spellName, server.getPlayerManager().getPlayer(payload.targetUUID));
        }
    }
}
