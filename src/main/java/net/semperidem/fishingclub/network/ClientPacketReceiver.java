package net.semperidem.fishingclub.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.*;
import net.semperidem.fishingclub.client.FishingClubClient;
import net.semperidem.fishingclub.fisher.FishingCardSerializer;
import net.semperidem.fishingclub.fisher.perks.FishingPerk;
import net.semperidem.fishingclub.fisher.perks.FishingPerks;
import net.semperidem.fishingclub.fisher.perks.spells.SpellInstance;

import java.util.HashMap;

public class ClientPacketReceiver {
    public static void registerClientPacketHandlers() {
        ClientPlayNetworking.registerGlobalReceiver(PacketIdentifiers.S2C_SUMMON_REQUEST, (client, handler, buf, responseSender) -> {
            String target =  buf.readString();
            client.execute( () -> {
               client.player.sendMessage(Text.of("Your friend:" + target + " send you summon request, You have 30s to accept"), false);
               client.player.sendMessage(MutableText.of(new LiteralTextContent("Accept?")).setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/fishing-club summon_accept"))), false);
            });
        });


        ClientPlayNetworking.registerGlobalReceiver(PacketIdentifiers.S2C_PERKS_LIST, (client, handler, buf, responseSender) -> {
            NbtCompound nbtCompound =  buf.readNbt();
            NbtList perkListTag = nbtCompound.getList(FishingCardSerializer.PERKS_TAG, NbtElement.STRING_TYPE);
            client.execute( () -> {
                HashMap<String, FishingPerk> fishingPerks = new HashMap<>();
                perkListTag.forEach(
                        nbtElement -> FishingPerks.getPerkFromName(nbtElement.asString()).ifPresent(
                                fishingPerk -> fishingPerks.put(fishingPerk.getName(), fishingPerk)));
                FishingClubClient.updatePerks(fishingPerks);
            });
        });
        ClientPlayNetworking.registerGlobalReceiver(PacketIdentifiers.S2C_SPELL_INSTANCES_LIST, (client, handler, buf, responseSender) -> {
            NbtCompound nbtCompound =  buf.readNbt();
            NbtList spellListTag = nbtCompound.getList(FishingCardSerializer.SPELLS_TAG, NbtElement.COMPOUND_TYPE);
            client.execute( () -> {
                HashMap<FishingPerk, SpellInstance> spells = new HashMap<>();
                for(int i = 0; i < spellListTag.size(); i++) {
                    SpellInstance spellInstance = SpellInstance.fromNbt(spellListTag.getCompound(i));
                    spells.put(spellInstance.getPerk(), spellInstance);
                }
                FishingClubClient.updateAvailableSpells(spells);
            });
        });
    }
}
