package net.semperidem.fishingclub.datagen;

import net.minecraft.data.DataOutput;
import net.minecraft.data.server.loottable.LootTableProvider;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.registry.RegistryWrapper;
import net.semperidem.fishingclub.registry.FCLootTables;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class FCLootTableProviders {
    public static LootTableProvider createLootTableProvider(DataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookupFuture) {
        return new LootTableProvider(
                output,
                Set.of(FCLootTables.JUNK),
                List.of(
                        new LootTableProvider.LootTypeGenerator(JunkLootTableGenerator::new, LootContextTypes.FISHING)
                ),
                registryLookupFuture
        );
    }
}

