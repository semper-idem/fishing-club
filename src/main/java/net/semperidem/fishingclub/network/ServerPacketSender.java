package net.semperidem.fishingclub.network;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.InventoryS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.*;
import net.minecraft.util.Identifier;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.fisher.FishingCardManager;
import net.semperidem.fishingclub.fisher.FishingCardSerializer;

import java.util.UUID;

import static net.semperidem.fishingclub.network.PacketIdentifiers.*;

public class ServerPacketSender {

    private static void sendPacket(ServerPlayerEntity player, Identifier identifier, PacketByteBuf buf){
        if (player != null && player.networkHandler != null) {
            ServerPlayNetworking.send( player, identifier, buf);
        }
    }

    public static void sendShopScreenInventorySyncPacket(ServerPlayerEntity player){
        player.networkHandler.sendPacket(
                new InventoryS2CPacket(
                        player.playerScreenHandler.syncId,
                        player.playerScreenHandler.getRevision(),
                        player.playerScreenHandler.getStacks(),
                        player.playerScreenHandler.getCursorStack()
                )
        );
    }

    public static void sendPerksUpdate(FishingCard fishingCard){
        PacketByteBuf packet = PacketByteBufs.create();
        NbtCompound nbtCompound = new NbtCompound();
        nbtCompound.put(FishingCardSerializer.PERKS_TAG, FishingCardSerializer.getPerkListTag(fishingCard));
        packet.writeNbt(nbtCompound);
        sendPacket((ServerPlayerEntity) fishingCard.getHolder(), S2C_PERKS_LIST, packet);
    }

    public static void sendSpellsUpdate(FishingCard fishingCard){
        PacketByteBuf packet = PacketByteBufs.create();
        NbtCompound nbtCompound = new NbtCompound();
        nbtCompound.put(FishingCardSerializer.SPELLS_TAG, FishingCardSerializer.getSpellListTag(fishingCard));
        packet.writeNbt(nbtCompound);
        sendPacket((ServerPlayerEntity) fishingCard.getHolder(), S2C_SPELL_INSTANCES_LIST, packet);
    }
}
