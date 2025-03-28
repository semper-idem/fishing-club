package net.semperidem.fishingclub.datagen;

import net.minecraft.data.server.loottable.LootTableGenerator;
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
import net.semperidem.fishingclub.registry.Items;
import net.semperidem.fishingclub.registry.LootTables;
import java.util.function.BiConsumer;

public record JunkLootTableGenerator(RegistryWrapper.WrapperLookup registries) implements LootTableGenerator {
    @Override
    public void accept(BiConsumer<RegistryKey<LootTable>, LootTable.Builder> lootTableBiConsumer) {
        lootTableBiConsumer.accept(
                LootTables.JUNK,
                LootTable.builder()
                        .pool(
                                LootPool.builder()
                                        .rolls(ConstantLootNumberProvider.create(1))
                                        .bonusRolls(BinomialLootNumberProvider.create(3, 0.1f))
                                        .with(LootTableEntry.builder(LootTables.JUNK_OTHER).weight(30).quality(-1))
                                        .with(LootTableEntry.builder(LootTables.JUNK_TOOLS).weight(10).quality(1))
                                        .with(LootTableEntry.builder(LootTables.JUNK_ROD).weight(15).quality(4))
                                        .with(LootTableEntry.builder(LootTables.JUNK_ARMOR).weight(5).quality(1))
                                        .with(LootTableEntry.builder(LootTables.JUNK_PLANT).weight(40).quality(-2))
                        )
        );
        lootTableBiConsumer.accept(
                LootTables.JUNK_ROD,
                LootTable.builder()
                        .apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(0.1f, 0.95f)))
                        .pool(LootPool.builder()
                                .with(ItemEntry.builder(Items.CORE_BAMBOO))
                                .with(ItemEntry.builder(Items.CORE_WOOD))
                                .with(ItemEntry.builder(Items.CORE_BONE))
                                .with(ItemEntry.builder(Items.REEL_BONE))
                                .with(ItemEntry.builder(Items.REEL_WOOD))
                                .with(ItemEntry.builder(Items.BOBBER_PLANT_SLIME))
                                .with(ItemEntry.builder(Items.BOBBER_LEATHER_SLIME))
                                .with(ItemEntry.builder(Items.LINE_PLANT))
                                .with(ItemEntry.builder(Items.LINE_WOOL))
                                .with(ItemEntry.builder(Items.LINE_SPIDER))
                                .with(ItemEntry.builder(Items.HOOK_BONE))
                                .with(ItemEntry.builder(Items.HOOK_FLINT))
                        ));
        lootTableBiConsumer.accept(
                LootTables.JUNK_ARMOR,
                LootTable.builder()
                        .apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(0.1f, 0.95f)))
                        .apply(EnchantRandomlyLootFunction.builder(registries).allowIncompatible())
                        .pool(LootPool.builder()
                                .with(ItemEntry.builder(net.minecraft.item.Items.IRON_CHESTPLATE))
                                .with(ItemEntry.builder(net.minecraft.item.Items.IRON_HELMET))
                                .with(ItemEntry.builder(net.minecraft.item.Items.IRON_BOOTS))
                                .with(ItemEntry.builder(net.minecraft.item.Items.IRON_LEGGINGS))
                                .with(ItemEntry.builder(net.minecraft.item.Items.GOLDEN_HELMET))
                                .with(ItemEntry.builder(net.minecraft.item.Items.GOLDEN_CHESTPLATE))
                                .with(ItemEntry.builder(net.minecraft.item.Items.GOLDEN_LEGGINGS))
                                .with(ItemEntry.builder(net.minecraft.item.Items.GOLDEN_BOOTS))
                                .with(ItemEntry.builder(net.minecraft.item.Items.CHAINMAIL_HELMET))
                                .with(ItemEntry.builder(net.minecraft.item.Items.CHAINMAIL_BOOTS))
                                .with(ItemEntry.builder(net.minecraft.item.Items.CHAINMAIL_CHESTPLATE))
                                .with(ItemEntry.builder(net.minecraft.item.Items.CHAINMAIL_LEGGINGS))
                                .with(ItemEntry.builder(net.minecraft.item.Items.LEATHER_BOOTS).weight(25))
                                .with(ItemEntry.builder(net.minecraft.item.Items.LEATHER_CHESTPLATE).weight(5))
                                .with(ItemEntry.builder(net.minecraft.item.Items.LEATHER_HELMET).weight(5))
                                .with(ItemEntry.builder(net.minecraft.item.Items.LEATHER_LEGGINGS).weight(5))
                        )
        );
        lootTableBiConsumer.accept(
                LootTables.JUNK_TOOLS,
                LootTable.builder()
                        .apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(0.1f, 0.95f)))
                        .apply(EnchantRandomlyLootFunction.builder(registries).allowIncompatible())
                        .pool(LootPool.builder()
                                .with(ItemEntry.builder(net.minecraft.item.Items.BOW).weight(5))
                                .with(ItemEntry.builder(net.minecraft.item.Items.CROSSBOW).weight(2))
                                .with(ItemEntry.builder(net.minecraft.item.Items.SHEARS))
                                .with(ItemEntry.builder(net.minecraft.item.Items.IRON_PICKAXE))
                                .with(ItemEntry.builder(net.minecraft.item.Items.IRON_AXE))
                                .with(ItemEntry.builder(net.minecraft.item.Items.IRON_HOE))
                                .with(ItemEntry.builder(net.minecraft.item.Items.IRON_SHOVEL))
                                .with(ItemEntry.builder(net.minecraft.item.Items.IRON_SWORD))
                                .with(ItemEntry.builder(net.minecraft.item.Items.GOLDEN_PICKAXE))
                                .with(ItemEntry.builder(net.minecraft.item.Items.GOLDEN_AXE))
                                .with(ItemEntry.builder(net.minecraft.item.Items.GOLDEN_HOE))
                                .with(ItemEntry.builder(net.minecraft.item.Items.GOLDEN_SHOVEL))
                                .with(ItemEntry.builder(net.minecraft.item.Items.GOLDEN_SWORD))
                        )
        );
        lootTableBiConsumer.accept(
                LootTables.JUNK_OTHER,
                LootTable.builder()
                        .apply(SetCountLootFunction.builder(BinomialLootNumberProvider.create(3, 0.05f)))
                        .pool(LootPool.builder()
                                .with(ItemEntry.builder(net.minecraft.item.Items.SADDLE))
                                .with(ItemEntry.builder(net.minecraft.item.Items.LEAD).weight(2))
                                .with(ItemEntry.builder(net.minecraft.item.Items.ROTTEN_FLESH).weight(10))
                                .with(ItemEntry.builder(net.minecraft.item.Items.WATER_BUCKET))
                                .with(ItemEntry.builder(net.minecraft.item.Items.LEATHER).weight(5))
                                .with(ItemEntry.builder(net.minecraft.item.Items.FEATHER).weight(5))
                                .with(ItemEntry.builder(net.minecraft.item.Items.RABBIT_HIDE).weight(5))
                                .with(ItemEntry.builder(net.minecraft.item.Items.PHANTOM_MEMBRANE).weight(3))
                                .with(ItemEntry.builder(net.minecraft.item.Items.TURTLE_SCUTE).weight(3))
                                .with(ItemEntry.builder(net.minecraft.item.Items.RABBIT_FOOT).weight(3))
                                .with(ItemEntry.builder(net.minecraft.item.Items.ARMADILLO_SCUTE).weight(2))
                                .with(ItemEntry.builder(net.minecraft.item.Items.INK_SAC).weight(10))
                                .with(ItemEntry.builder(net.minecraft.item.Items.GLOW_INK_SAC).weight(5))
                                .with(ItemEntry.builder(net.minecraft.item.Items.BONE).weight(5))
                                .with(ItemEntry.builder(net.minecraft.item.Items.SPIDER_EYE).weight(5))
                                .with(ItemEntry.builder(net.minecraft.item.Items.GOLD_NUGGET).weight(8))
                                .with(ItemEntry.builder(net.minecraft.item.Items.IRON_NUGGET).weight(8))
                                .with(ItemEntry.builder(net.minecraft.item.Items.FLINT).weight(2))
                                .with(ItemEntry.builder(net.minecraft.item.Items.LAPIS_LAZULI))
                                .with(ItemEntry.builder(net.minecraft.item.Items.STRING).weight(10))
                                .with(ItemEntry.builder(net.minecraft.item.Items.SLIME_BALL).weight(10))
                                .with(ItemEntry.builder(net.minecraft.item.Items.PRISMARINE_SHARD).weight(3))
                                .with(ItemEntry.builder(net.minecraft.item.Items.PRISMARINE_CRYSTALS).weight(2))
                                .with(ItemEntry.builder(net.minecraft.item.Items.STICK).weight(10))
                                .with(ItemEntry.builder(net.minecraft.item.Items.EXPERIENCE_BOTTLE).weight(2))
                                .with(ItemEntry.builder(net.minecraft.item.Items.POTION).weight(10))


                        )
        );

        lootTableBiConsumer.accept(
                LootTables.JUNK_PLANT,
                LootTable.builder()
                        .apply(SetCountLootFunction.builder(BinomialLootNumberProvider.create(8, 0.25f)))
                        .pool(LootPool.builder()
                                .with(ItemEntry.builder(net.minecraft.item.Items.WHEAT_SEEDS).weight(2))
                                .with(ItemEntry.builder(net.minecraft.item.Items.LILY_PAD).weight(3))
                                .with(ItemEntry.builder(net.minecraft.item.Items.PUMPKIN_SEEDS).weight(2))
                                .with(ItemEntry.builder(net.minecraft.item.Items.MELON_SEEDS).weight(2))
                                .with(ItemEntry.builder(net.minecraft.item.Items.BAMBOO).weight(3))
                                .with(ItemEntry.builder(net.minecraft.item.Items.SUGAR_CANE).weight(2))
                                .with(ItemEntry.builder(net.minecraft.item.Items.KELP).weight(4))
                                .with(ItemEntry.builder(Items.NUTRITIOUS_KELP).weight(3))
                                .with(ItemEntry.builder(Items.REED).weight(3))
                                .with(ItemEntry.builder(Items.ENERGY_DENSE_KELP).weight(3))
                                .with(ItemEntry.builder(net.minecraft.item.Items.SEAGRASS).weight(4))
                                .with(ItemEntry.builder(net.minecraft.item.Items.SEA_PICKLE).weight(2))
                                .with(ItemEntry.builder(net.minecraft.item.Items.POTATO).weight(2))
                                .with(ItemEntry.builder(net.minecraft.item.Items.POISONOUS_POTATO).weight(2))
                                .with(ItemEntry.builder(net.minecraft.item.Items.CARROT).weight(2))
                                .with(ItemEntry.builder(net.minecraft.item.Items.BEETROOT_SEEDS).weight(2))
                                .with(ItemEntry.builder(net.minecraft.item.Items.COCOA_BEANS))
                                .with(ItemEntry.builder(net.minecraft.item.Items.GLOW_BERRIES))
                                .with(ItemEntry.builder(net.minecraft.item.Items.SWEET_BERRIES))
                        )
        );
    }
}
