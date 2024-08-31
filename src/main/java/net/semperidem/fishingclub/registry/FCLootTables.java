package net.semperidem.fishingclub.registry;

import net.minecraft.loot.LootTable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.mixin.common.LootTablesAccessor;

public class FCLootTables {
    public static RegistryKey<LootTable> JUNK;
    public static RegistryKey<LootTable> JUNK_OTHER;
    public static RegistryKey<LootTable> JUNK_PLANT;
    public static RegistryKey<LootTable> JUNK_TOOLS;
    public static RegistryKey<LootTable> JUNK_ARMOR;
    public static RegistryKey<LootTable> JUNK_ROD;

    public static void register() {
        JUNK = LootTablesAccessor.registerLootTable(RegistryKey.of(RegistryKeys.LOOT_TABLE, FishingClub.identifier("junk")));
        JUNK_TOOLS = LootTablesAccessor.registerLootTable(RegistryKey.of(RegistryKeys.LOOT_TABLE, FishingClub.identifier("junk_tools")));
        JUNK_ROD = LootTablesAccessor.registerLootTable(RegistryKey.of(RegistryKeys.LOOT_TABLE, FishingClub.identifier("junk_rod")));
        JUNK_ARMOR = LootTablesAccessor.registerLootTable(RegistryKey.of(RegistryKeys.LOOT_TABLE, FishingClub.identifier("junk_armor")));
        JUNK_OTHER = LootTablesAccessor.registerLootTable(RegistryKey.of(RegistryKeys.LOOT_TABLE, FishingClub.identifier("junk_other")));
        JUNK_PLANT = LootTablesAccessor.registerLootTable(RegistryKey.of(RegistryKeys.LOOT_TABLE, FishingClub.identifier("junk_plant")));

    }
}
