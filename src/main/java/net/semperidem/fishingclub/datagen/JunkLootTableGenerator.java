package net.semperidem.fishingclub.datagen;

import net.minecraft.data.server.loottable.LootTableGenerator;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LootTableEntry;
import net.minecraft.loot.function.EnchantRandomlyLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.function.SetDamageLootFunction;
import net.minecraft.loot.provider.number.BinomialLootNumberProvider;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.semperidem.fishingclub.registry.FCItems;
import net.semperidem.fishingclub.registry.FCLootTables;
import java.util.function.BiConsumer;

public record JunkLootTableGenerator(RegistryWrapper.WrapperLookup registries) implements LootTableGenerator {
    @Override
    public void accept(BiConsumer<RegistryKey<LootTable>, LootTable.Builder> lootTableBiConsumer) {
        lootTableBiConsumer.accept(
                FCLootTables.JUNK,
                LootTable.builder()
                        .pool(
                                LootPool.builder()
                                        .rolls(ConstantLootNumberProvider.create(1))
                                        .bonusRolls(BinomialLootNumberProvider.create(3, 0.1f))
                                        .with(LootTableEntry.builder(FCLootTables.JUNK_OTHER).weight(30).quality(-1))
                                        .with(LootTableEntry.builder(FCLootTables.JUNK_TOOLS).weight(10).quality(1))
                                        .with(LootTableEntry.builder(FCLootTables.JUNK_ROD).weight(15).quality(4))
                                        .with(LootTableEntry.builder(FCLootTables.JUNK_ARMOR).weight(5).quality(1))
                                        .with(LootTableEntry.builder(FCLootTables.JUNK_PLANT).weight(40).quality(-2))
                        )
        );
        lootTableBiConsumer.accept(
                FCLootTables.JUNK_ROD,
                LootTable.builder()
                        .apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(0.1f, 0.95f)))
                        .pool(LootPool.builder()
                                .with(ItemEntry.builder(FCItems.CORE_BAMBOO))
                                .with(ItemEntry.builder(FCItems.CORE_WOOD))
                                .with(ItemEntry.builder(FCItems.CORE_BONE))
                                .with(ItemEntry.builder(FCItems.REEL_BONE))
                                .with(ItemEntry.builder(FCItems.REEL_WOOD))
                                .with(ItemEntry.builder(FCItems.BOBBER_PLANT_SLIME))
                                .with(ItemEntry.builder(FCItems.BOBBER_LEATHER_SLIME))
                                .with(ItemEntry.builder(FCItems.LINE_PLANT))
                                .with(ItemEntry.builder(FCItems.LINE_WOOL))
                                .with(ItemEntry.builder(FCItems.LINE_SPIDER))
                                .with(ItemEntry.builder(FCItems.HOOK_BONE))
                                .with(ItemEntry.builder(FCItems.HOOK_FLINT))
                        ));
        lootTableBiConsumer.accept(
                FCLootTables.JUNK_ARMOR,
                LootTable.builder()
                        .apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(0.1f, 0.95f)))
                        .apply(EnchantRandomlyLootFunction.builder(registries).allowIncompatible())
                        .pool(LootPool.builder()
                                .with(ItemEntry.builder(Items.IRON_CHESTPLATE))
                                .with(ItemEntry.builder(Items.IRON_HELMET))
                                .with(ItemEntry.builder(Items.IRON_BOOTS))
                                .with(ItemEntry.builder(Items.IRON_LEGGINGS))
                                .with(ItemEntry.builder(Items.GOLDEN_HELMET))
                                .with(ItemEntry.builder(Items.GOLDEN_CHESTPLATE))
                                .with(ItemEntry.builder(Items.GOLDEN_LEGGINGS))
                                .with(ItemEntry.builder(Items.GOLDEN_BOOTS))
                                .with(ItemEntry.builder(Items.CHAINMAIL_HELMET))
                                .with(ItemEntry.builder(Items.CHAINMAIL_BOOTS))
                                .with(ItemEntry.builder(Items.CHAINMAIL_CHESTPLATE))
                                .with(ItemEntry.builder(Items.CHAINMAIL_LEGGINGS))
                                .with(ItemEntry.builder(Items.LEATHER_BOOTS).weight(25))
                                .with(ItemEntry.builder(Items.LEATHER_CHESTPLATE).weight(5))
                                .with(ItemEntry.builder(Items.LEATHER_HELMET).weight(5))
                                .with(ItemEntry.builder(Items.LEATHER_LEGGINGS).weight(5))
                        )
        );
        lootTableBiConsumer.accept(
                FCLootTables.JUNK_TOOLS,
                LootTable.builder()
                        .apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(0.1f, 0.95f)))
                        .apply(EnchantRandomlyLootFunction.builder(registries).allowIncompatible())
                        .pool(LootPool.builder()
                                .with(ItemEntry.builder(Items.BOW).weight(5))
                                .with(ItemEntry.builder(Items.CROSSBOW).weight(2))
                                .with(ItemEntry.builder(Items.SHEARS))
                                .with(ItemEntry.builder(Items.IRON_PICKAXE))
                                .with(ItemEntry.builder(Items.IRON_AXE))
                                .with(ItemEntry.builder(Items.IRON_HOE))
                                .with(ItemEntry.builder(Items.IRON_SHOVEL))
                                .with(ItemEntry.builder(Items.IRON_SWORD))
                                .with(ItemEntry.builder(Items.GOLDEN_PICKAXE))
                                .with(ItemEntry.builder(Items.GOLDEN_AXE))
                                .with(ItemEntry.builder(Items.GOLDEN_HOE))
                                .with(ItemEntry.builder(Items.GOLDEN_SHOVEL))
                                .with(ItemEntry.builder(Items.GOLDEN_SWORD))
                        )
        );
        lootTableBiConsumer.accept(
                FCLootTables.JUNK_OTHER,
                LootTable.builder()
                        .apply(SetCountLootFunction.builder(BinomialLootNumberProvider.create(3, 0.05f)))
                        .pool(LootPool.builder()
                                .with(ItemEntry.builder(Items.SADDLE))
                                .with(ItemEntry.builder(Items.LEAD).weight(2))
                                .with(ItemEntry.builder(Items.ROTTEN_FLESH).weight(10))
                                .with(ItemEntry.builder(Items.WATER_BUCKET))
                                .with(ItemEntry.builder(Items.LEATHER).weight(5))
                                .with(ItemEntry.builder(Items.FEATHER).weight(5))
                                .with(ItemEntry.builder(Items.RABBIT_HIDE).weight(5))
                                .with(ItemEntry.builder(Items.PHANTOM_MEMBRANE).weight(3))
                                .with(ItemEntry.builder(Items.TURTLE_SCUTE).weight(3))
                                .with(ItemEntry.builder(Items.RABBIT_FOOT).weight(3))
                                .with(ItemEntry.builder(Items.ARMADILLO_SCUTE).weight(2))
                                .with(ItemEntry.builder(Items.INK_SAC).weight(10))
                                .with(ItemEntry.builder(Items.GLOW_INK_SAC).weight(5))
                                .with(ItemEntry.builder(Items.BONE).weight(5))
                                .with(ItemEntry.builder(Items.SPIDER_EYE).weight(5))
                                .with(ItemEntry.builder(Items.GOLD_NUGGET).weight(8))
                                .with(ItemEntry.builder(Items.IRON_NUGGET).weight(8))
                                .with(ItemEntry.builder(Items.FLINT).weight(2))
                                .with(ItemEntry.builder(Items.LAPIS_LAZULI))
                                .with(ItemEntry.builder(Items.STRING).weight(10))
                                .with(ItemEntry.builder(Items.SLIME_BALL).weight(10))
                                .with(ItemEntry.builder(Items.PRISMARINE_SHARD).weight(3))
                                .with(ItemEntry.builder(Items.PRISMARINE_CRYSTALS).weight(2))
                                .with(ItemEntry.builder(Items.STICK).weight(10))
                                .with(ItemEntry.builder(Items.EXPERIENCE_BOTTLE).weight(2))
                                .with(ItemEntry.builder(Items.POTION).weight(10))


                        )
        );

        lootTableBiConsumer.accept(
                FCLootTables.JUNK_PLANT,
                LootTable.builder()
                        .apply(SetCountLootFunction.builder(BinomialLootNumberProvider.create(8, 0.25f)))
                        .pool(LootPool.builder()
                                .with(ItemEntry.builder(Items.WHEAT_SEEDS).weight(2))
                                .with(ItemEntry.builder(Items.LILY_PAD).weight(3))
                                .with(ItemEntry.builder(Items.PUMPKIN_SEEDS).weight(2))
                                .with(ItemEntry.builder(Items.MELON_SEEDS).weight(2))
                                .with(ItemEntry.builder(Items.BAMBOO).weight(3))
                                .with(ItemEntry.builder(Items.SUGAR_CANE).weight(2))
                                .with(ItemEntry.builder(Items.KELP).weight(4))
                                .with(ItemEntry.builder(FCItems.NUTRITIOUS_KELP).weight(3))
                                .with(ItemEntry.builder(FCItems.REED).weight(3))
                                .with(ItemEntry.builder(FCItems.ENERGY_DENSE_KELP).weight(3))
                                .with(ItemEntry.builder(Items.SEAGRASS).weight(4))
                                .with(ItemEntry.builder(Items.SEA_PICKLE).weight(2))
                                .with(ItemEntry.builder(Items.POTATO).weight(2))
                                .with(ItemEntry.builder(Items.POISONOUS_POTATO).weight(2))
                                .with(ItemEntry.builder(Items.CARROT).weight(2))
                                .with(ItemEntry.builder(Items.BEETROOT_SEEDS).weight(2))
                                .with(ItemEntry.builder(Items.COCOA_BEANS))
                                .with(ItemEntry.builder(Items.GLOW_BERRIES))
                                .with(ItemEntry.builder(Items.SWEET_BERRIES))
                        )
        );
    }
}
