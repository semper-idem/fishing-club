package net.semperidem.fishingclub.datagen;

import net.minecraft.data.server.loottable.LootTableGenerator;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.semperidem.fishingclub.registry.FCLootTables;

import java.util.function.BiConsumer;

public record JunkLootTableGenerator(RegistryWrapper.WrapperLookup registries) implements LootTableGenerator {
    @Override
    public void accept(BiConsumer<RegistryKey<LootTable>, LootTable.Builder> lootTableBiConsumer) {
        lootTableBiConsumer.accept(
                FCLootTables.JUNK,
                LootTable.builder()
                        .pool(LootPool.builder()
                                .bonusRolls(ConstantLootNumberProvider.create(1))
                                .with(ItemEntry.builder(Items.LILY_PAD).weight(5))
                        )
        );
    }
}
