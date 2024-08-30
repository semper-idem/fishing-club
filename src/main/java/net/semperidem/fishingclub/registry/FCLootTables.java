package net.semperidem.fishingclub.registry;

import net.minecraft.loot.LootTable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.mixin.common.LootTablesAccessor;

public class FCLootTables {
    public static RegistryKey<LootTable> JUNK;
    public static void register() {
        JUNK = LootTablesAccessor.registerLootTable(RegistryKey.of(RegistryKeys.LOOT_TABLE, FishingClub.identifier("junk")));

    }
}
