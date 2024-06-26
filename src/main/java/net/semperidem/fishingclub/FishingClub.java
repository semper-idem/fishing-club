package net.semperidem.fishingclub;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.semperidem.fishingclub.registry.FishingClubRegistry;
import net.semperidem.fishingclub.registry.ItemRegistry;
import net.semperidem.fishingclub.util.Commands;

import java.util.function.Function;

public class FishingClub implements ModInitializer {
    public static final String MOD_ID = "fishing-club";

    @Override
    public void onInitialize() {
        Commands.register();
        FishingClubRegistry.register();
        addGoldenFishToLootTable();
    }

    private void addGoldenFishToLootTable() {
        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
            if (!LootTables.FISHING_TREASURE_GAMEPLAY.equals(id)) {
               return;
            }
            tableBuilder.modifyPools((pool) -> pool.with(ItemEntry.builder(ItemRegistry.GOLD_FISH)));
        });
    }

    public static Identifier getIdentifier(String resource){
        return Identifier.of(MOD_ID, resource);
    }
}
