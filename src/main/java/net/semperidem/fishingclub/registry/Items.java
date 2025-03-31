package net.semperidem.fishingclub.registry;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.block.jukebox.JukeboxSongs;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.item.*;
import net.minecraft.item.equipment.ArmorMaterials;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Rarity;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.item.*;
import net.semperidem.fishingclub.item.fishing_rod.components.*;

import java.util.ArrayList;
import java.util.function.Function;

import static net.minecraft.block.ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE;
import static net.minecraft.util.Rarity.*;

public class Items {
    public static final int VANILLA_ROD_DURABILITY = 640;

    public static Item FISHING_NET;
    public static Item DOUBLE_FISHING_NET;
    public static Item FISH_COIN_BUNDLE;
    public static Item FISHER_HAT;
    public static Item FISHER_VEST;
    public static Item ILLEGAL_GOODS;
    public static Item GOLD_FISH;
    public static Item DEBUG;
    public static Item TACKLE_BOX;
    public static Item FISH_DISPLAY_BAMBOO;
    public static Item YELLOW_FISH_TUNE;
    public static Item REED;
    public static Item ENERGY_DENSE_KELP;
    public static Item DRIED_ENERGY_DENSE_KELP;
    public static Item DRIED_ENERGY_DENSE_KELP_BLOCK;
    public static Item NUTRITIOUS_KELP;
    public static Item DRIED_NUTRITIOUS_KELP;
    public static Item DRIED_NUTRITIOUS_KELP_BLOCK;
    public static Item DUCKWEED;
    public static Item MESSAGE_IN_BOTTLE_ITEM;


    public static final RegistryKey<net.minecraft.item.ItemGroup> ITEM_GROUP_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(), FishingClub.identifier("fishingclub"));

    public static ItemGroup ITEM_GROUP;

    public static Item CORE_BAMBOO;
    public static Item CORE_WOOD;

    public static Item CORE_BONE;
    public static Item CORE_COPPER;
    public static Item CORE_BLAZE;
    public static Item CORE_BREEZE;

    public static Item CORE_PRISMARINE_BLAZE;
    public static Item CORE_NETHERITE;
    public static Item CORE_NAUTILUS_BREEZE;
    public static Item CORE_NAUTILUS_BLAZE;
    public static Item CORE_NAUTILUS_END;


    public static Item REEL_WOOD;
    public static Item REEL_BONE;
    public static Item REEL_PRISMARINE;
    public static Item REEL_IRON;
    public static Item REEL_IRON_PRISMARINE;
    public static Item REEL_AMETHYST_PRISMARINE;
    public static Item REEL_NAUTILUS_ECHO;
    public static Item REEL_NAUTILUS_AMETHYST;
    public static Item REEL_HEART_AMETHYST;
    public static Item REEL_HEART_ECHO;
    public static Item BOBBER_PLANT_SLIME;
    public static Item BOBBER_LEATHER_SLIME;
    public static Item BOBBER_TURTLE_SLIME;
    public static Item BOBBER_ARMADILLO_SLIME;
    public static Item BOBBER_TURTLE_ENDER;
    public static Item BOBBER_ARMADILLO_MAGMA;
    public static Item BOBBER_AMETHYST_ENDER;
    public static Item BOBBER_NAUTILUS;
    public static Item BOBBER_TURTLE_ECHO;
    public static Item BOBBER_ARMADILLO_ECHO;
    public static Item BOBBER_AMETHYST_ECHO;
    public static Item BOBBER_NAUTILUS_ECHO;
    public static Item BOBBER_HEART;
    public static Item LINE_PLANT;
    public static Item LINE_WOOL;
    public static Item LINE_SPIDER;
    public static Item LINE_STRIDER;
    public static Item LINE_PHANTOM;
    public static Item LINE_EVOKER;
    public static Item LINE_PIGLIN;
    public static Item LINE_HEART;
    public static Item LINE_HEART_PIGLIN;
    public static Item LINE_HEART_EVOKER;
    public static Item HOOK_FLINT;
    public static Item HOOK_FLINT_POISON;
    public static Item HOOK_FLINT_SHARP;
    public static Item HOOK_FLINT_SERRATED;
    public static Item HOOK_BONE;
    public static Item HOOK_BONE_POISON;
    public static Item HOOK_BONE_SHARP;
    public static Item HOOK_BONE_SERRATED;
    public static Item HOOK_COPPER;
    public static Item HOOK_COPPER_POISON;
    public static Item HOOK_COPPER_SHARP;
    public static Item HOOK_COPPER_SERRATED;
    public static Item HOOK_COPPER_STICKY;
    public static Item HOOK_IRON;
    public static Item HOOK_IRON_POISON;
    public static Item HOOK_IRON_SHARP;
    public static Item HOOK_IRON_SERRATED;
    public static Item HOOK_IRON_STICKY;
    public static Item HOOK_GOLD;
    public static Item HOOK_GOLD_POISON;
    public static Item HOOK_GOLD_SHARP;
    public static Item HOOK_GOLD_SERRATED;
    public static Item HOOK_GOLD_STICKY;
    public static Item HOOK_PRISMARINE;
    public static Item HOOK_PRISMARINE_POISON;
    public static Item HOOK_PRISMARINE_SHARP;
    public static Item HOOK_PRISMARINE_SERRATED;
    public static Item HOOK_PRISMARINE_STICKY;
    public static Item HOOK_NAUTILUS;
    public static Item HOOK_NAUTILUS_POISON;
    public static Item HOOK_NAUTILUS_SHARP;
    public static Item HOOK_NAUTILUS_SERRATED;
    public static Item HOOK_NAUTILUS_STICKY;
    public static Item HOOK_NETHERITE;
    public static Item HOOK_NETHERITE_POISON;
    public static Item HOOK_NETHERITE_SHARP;
    public static Item HOOK_NETHERITE_SERRATED;
    public static Item HOOK_NETHERITE_STICKY;
    public static Item BAIT_PLANT;
    public static Item BAIT_ROTTEN_FLESH;
    public static Item BAIT_MEAT;
    public static Item BAIT_ORCHID;
    public static Item BAIT_GLOW_BERRIES;
    public static Item BAIT_BLAZE_POWDER;
    public static Item BAIT_IRON;
    public static Item BAIT_GHAST;
    public static Item BAIT_RABBIT;
    public static Item BAIT_CHORUS;
    public static Item BAIT_BREEZE_BLAZE;
    public static Item BAIT_TORCHFLOWER;
    public static Item BAIT_PITCHER_PLANT;
    public static Item BAIT_SPECIAL;

    public static FoodComponent NUTRITIOUS_KELP_FOOD = new FoodComponent.Builder()
            .nutrition(1)
            .saturationModifier(0.3F)
            .alwaysEdible()
           // .statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 100, 0), 0.05F)
            .build();


    public static FoodComponent DRIED_NUTRITIOUS_KELP_FOOD = new FoodComponent.Builder()
            .nutrition(4)
            .saturationModifier(0.9F)
            .alwaysEdible()
//            .statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 200, 0), 0.1F)
            .build();



    public static RegistryKey<Item> keyOf(String id) {
        return RegistryKey.of(RegistryKeys.ITEM, FishingClub.identifier(id));
    }

    private static Item register(String id, Function<Item.Settings, Item> factory, Item.Settings settings) {
        return register(keyOf(id), factory, settings);
    }




    private static Item register(RegistryKey<Item> key, Function<Item.Settings, Item> factory, Item.Settings settings) {
        Item item = (Item)factory.apply(settings.registryKey(key));
        if (item instanceof BlockItem blockItem) {
            blockItem.appendBlocks(Item.BLOCK_ITEMS, item);
        }

        FISHING_ITEMS.add(item.getDefaultStack());
        return Registry.register(Registries.ITEM, key, item);
    }

    public static void register() {
        registerParts();
        DOUBLE_FISHING_NET = register(
                "double_fishing_net",
                settings -> new FishingNetItem(settings, 4),
                new Item.Settings().maxCount(1).component(Components.NET_CONTENT, NetContentComponent.DEFAULT)
        );
        FISHING_NET = register(
                "fishing_net",
                settings -> new FishingNetItem(settings, 2),
                new Item.Settings().maxCount(1).component(Components.NET_CONTENT, NetContentComponent.DEFAULT)
        );
        FISH_COIN_BUNDLE = register(
                "fish_coin_bundle",
                FishCoinBundleItem::new,
                new Item.Settings().maxCount(1)
        );
        FISHER_HAT = register(
                "fisher_hat",
                Item::new,
                new Item.Settings().armor(ArmorMaterials.LEATHER, EquipmentType.HELMET)
        );
        FISHER_VEST = register(
                "fisher_vest",
                Item::new,
                new Item.Settings().armor(ArmorMaterials.LEATHER, EquipmentType.CHESTPLATE)
        );
        ILLEGAL_GOODS = register(
                "illegal_goods",
                IllegalGoodsItem::new,
                new Item.Settings().rarity(RARE).maxCount(1)
        );
        GOLD_FISH = register(
                "gold_fish",
                Item::new,
                new Item.Settings().maxCount(1)
        );
        TACKLE_BOX = register(
                "tackle_box",
                settings -> new TackleBoxItem(Blocks.TACKLE_BOX_BLOCK, settings),
                new Item.Settings()
        );
        FISH_DISPLAY_BAMBOO = register(
                "fish_display_bamboo",
                settings -> new FishDisplayItem(Blocks.FISH_DISPLAY_BLOCK_BAMBOO, settings),
                new Item.Settings().maxCount(16)
        );
        YELLOW_FISH_TUNE = register(
                "yellow_fish_tune",
                Item::new,
                new Item.Settings().maxCount(1).rarity(Rarity.RARE).jukeboxPlayable(JukeboxSongs.ELEVEN)
        );
        REED = register(
                "reed",
                settings -> new BlockItem(Blocks.REED_BLOCK, settings),
                new Item.Settings()
        );
        ENERGY_DENSE_KELP = register(
                "energy_dense_kelp",
                settings -> new BlockItem(Blocks.ENERGY_DENSE_KELP, settings),
                new Item.Settings()
        );
        DRIED_ENERGY_DENSE_KELP = register(
                "dried_energy_dense_kelp",
                Item::new,
                new Item.Settings()
        );
        DRIED_ENERGY_DENSE_KELP_BLOCK = register(
                "dried_energy_dense_kelp_block",
                settings -> new BlockItem(Blocks.DRIED_ENERGY_DENSE_KELP_BLOCK, settings),
                new Item.Settings()
        );
        NUTRITIOUS_KELP = register(
                "nutritious_kelp",
                settings -> new BlockItem(Blocks.NUTRITIOUS_KELP, settings),
                new Item.Settings().food(NUTRITIOUS_KELP_FOOD));
        DRIED_NUTRITIOUS_KELP = register(
                "dried_nutritious_kelp",
                Item::new,
                new Item.Settings().food(DRIED_NUTRITIOUS_KELP_FOOD)
        );
        DRIED_NUTRITIOUS_KELP_BLOCK = register(
                "dried_nutritious_kelp_block",
                settings -> new BlockItem(Blocks.DRIED_NUTRITIOUS_KELP_BLOCK, settings),
                new Item.Settings()
        );
        DUCKWEED = register(
                "duckweed",
                settings -> new DuckweedItem(Blocks.DUCKWEED_BLOCK, settings),
                new Item.Settings()
        );
        MESSAGE_IN_BOTTLE_ITEM = register(
                "message_in_bottle",
                MessageInBottleItem::new,
                new Item.Settings().rarity(UNCOMMON)
        );


        registerCompostableItem(0.1f, DUCKWEED);
        registerCompostableItem(0.3f, REED);
        registerCompostableItem(0.3f, NUTRITIOUS_KELP);
        registerCompostableItem(0.5f, DRIED_NUTRITIOUS_KELP);
        registerCompostableItem(1, DRIED_ENERGY_DENSE_KELP_BLOCK);
        registerCompostableItem(0.3f, ENERGY_DENSE_KELP);
        registerCompostableItem(0.5f, DRIED_ENERGY_DENSE_KELP);
        registerCompostableItem(1, DRIED_ENERGY_DENSE_KELP_BLOCK);

//        FuelRegistry.INSTANCE.add(DRIED_ENERGY_DENSE_KELP, 200);
//        FuelRegistry.INSTANCE.add(DRIED_ENERGY_DENSE_KELP_BLOCK, 12800);

//        RegistryKey<Item> fishingRodKey = keyOf("fishing_rod");
//            RodConfiguration defaultRod = RodConfiguration.EMPTY.equip(Items.LINE_SPIDER.getDefaultStack());
//            Registry.register(
//                            Registries.ITEM,
//                            Registries.ITEM.getId(net.minecraft.item.Items.FISHING_ROD),
//                            new FishingRodCoreItem(new Item.Settings()
//                                    .registryKey(fishingRodKey)
//                                    .maxDamage(Items.VANILLA_ROD_DURABILITY)
//                                    .component(Components.ROD_CONFIGURATION, defaultRod))
//                    .weightClass(2));
        registerItemGroup();
    }


    private static void registerCompostableItem(float levelIncreaseChance, ItemConvertible item) {
        ITEM_TO_LEVEL_INCREASE_CHANCE.put(item.asItem(), levelIncreaseChance);
    }


    public static void registerParts() {
        registerCore();
        registerReel();
        registerBobber();
        registerLine();
        registerHook();
        registerBait();
    }

    private static void registerItemGroup() {
        ITEM_GROUP = Registry.register(Registries.ITEM_GROUP, ITEM_GROUP_KEY,
                FabricItemGroup.builder()
                        .displayName(Text.literal("Fishing Club"))
                        .icon(net.minecraft.item.Items.COD::getDefaultStack)
                        .entries((displayContext, entries) -> entries.addAll(FISHING_ITEMS
//              .stream().sorted(
//              Comparator.comparing(o -> o.getName().getString(), String.CASE_INSENSITIVE_ORDER)).toList()
                        ))
                        .build());

    }

    public static void registerCore() {
        CORE_BAMBOO = register(
                "core_bamboo",
                settings -> new FishingRodCoreItem(settings)
                .weightClass(1)
                .maxOperatingTemperature(1)
                .castPower(ItemStat.MULTIPLIER_T1),
                new Item.Settings().maxCount(1280).rarity(COMMON)
        );
        CORE_WOOD = register(
                "core_wood",
                settings -> new FishingRodCoreItem(settings)
                .weightClass(2)
                .maxOperatingTemperature(1)
                .castPower(ItemStat.MULTIPLIER_T2),
                new Item.Settings().maxDamage(VANILLA_ROD_DURABILITY).rarity(COMMON)
        );
        CORE_BONE = register(
                "core_bone",
                settings -> new FishingRodCoreItem(settings)
                .weightClass(1)
                .minOperatingTemperature(-1)
                .maxOperatingTemperature(1)
                .castPower(ItemStat.MULTIPLIER_T1)
                .luck(1),
                new Item.Settings().maxDamage(1280).rarity(COMMON)
        );

        CORE_COPPER = register(
                "core_copper",
                settings -> new FishingRodCoreItem(settings)
                .weightClass(3)
                .minOperatingTemperature(-1)
                .castPower(ItemStat.MULTIPLIER_T2),
                new Item.Settings().maxDamage(2560).rarity(UNCOMMON)
        );
        CORE_BLAZE = register(
                "core_blaze",
                settings -> new FishingRodCoreItem(settings)
                .weightClass(2)
                .minOperatingTemperature(1)
                .maxOperatingTemperature(2)
                .castPower(ItemStat.MULTIPLIER_T2),
                new Item.Settings().maxDamage(2560).rarity(UNCOMMON)
        );
        CORE_BREEZE = register(
                "core_breeze",
                settings -> new FishingRodCoreItem(settings)
                .weightClass(2)
                .minOperatingTemperature(-2)
                .castPower(ItemStat.MULTIPLIER_T2),
                new Item.Settings().maxDamage(2560).rarity(UNCOMMON)
        );
        CORE_PRISMARINE_BLAZE = register(
                "core_prismarine_blaze",
                settings -> new FishingRodCoreItem(settings)
                .weightClass(3)
                .maxOperatingTemperature(2)
                .luck(3),
                new Item.Settings().maxDamage(5120).rarity(RARE)
        );
        CORE_NETHERITE = register(
                "core_netherite",
                settings -> new FishingRodCoreItem(settings)
                .weightClass(5)
                .minOperatingTemperature(-2)
                .maxOperatingTemperature(2)
                .castPower(ItemStat.MULTIPLIER_T4)
                .luck(1),
                new Item.Settings().maxDamage(20480).rarity(RARE)
        );
        CORE_NAUTILUS_BREEZE = register(
                "core_nautilus_breeze",
                settings -> new FishingRodCoreItem(settings)
                .weightClass(5)
                .minOperatingTemperature(-2)
                .maxOperatingTemperature(2)
                .castPower(ItemStat.MULTIPLIER_T5)
                .luck(2),
                new Item.Settings().maxDamage(10240).rarity(EPIC)
        );
        CORE_NAUTILUS_BLAZE = register(
                "core_nautilus_blaze",
                settings -> new FishingRodCoreItem(settings)
                .weightClass(5)
                .minOperatingTemperature(-2)
                .maxOperatingTemperature(2)
                .castPower(ItemStat.MULTIPLIER_T4)
                .luck(4),
                new Item.Settings().maxDamage(10240).rarity(EPIC)
        );
        CORE_NAUTILUS_END = register(
                "core_nautilus_end",
                settings -> new FishingRodCoreItem(settings)
                .weightClass(6)
                .minOperatingTemperature(-2)
                .maxOperatingTemperature(1)
                .castPower(ItemStat.MULTIPLIER_T4)
                .luck(1),
                new Item.Settings().maxDamage(20480).rarity(EPIC)
        );
    }

    public static void registerReel() {
        REEL_WOOD = register(
                "reel_wood",
                settings -> new ReelPartItem(settings)
                .weightClass(2)
                .bobberControl(ItemStat.BASE_T1)
                .fishControl(ItemStat.BASE_T1)
                .timeHookedMultiplier(ItemStat.MULTIPLIER_T2),
                new Item.Settings().rarity(COMMON)
        );
        REEL_BONE = register(
                "reel_bone",
                settings -> new ReelPartItem(settings)
                .weightClass(1)
                .minOperatingTemperature(-1)
                .maxOperatingTemperature(1)
                .bobberControl(ItemStat.BASE_T2)
                .fishControl(ItemStat.BASE_T2)
                .timeHookedMultiplier(ItemStat.MULTIPLIER_T1),
                new Item.Settings().rarity(COMMON)
        );
        REEL_PRISMARINE = register(
                "reel_prismarine",
                settings -> new ReelPartItem(settings)
                .weightClass(3)
                .minOperatingTemperature(-1)
                .bobberControl(ItemStat.BASE_T3)
                .fishControl(ItemStat.BASE_T3)
                .timeHookedMultiplier(ItemStat.MULTIPLIER_T2),
                new Item.Settings().rarity(COMMON)
        );
        REEL_IRON = register(
                "reel_iron",
                settings -> new ReelPartItem(settings)
                .weightClass(3)
                .minOperatingTemperature(-1)
                .maxOperatingTemperature(1)
                .bobberControl(ItemStat.BASE_T2)
                .fishControl(ItemStat.BASE_T2)
                .timeHookedMultiplier(ItemStat.MULTIPLIER_T2),
                new Item.Settings().rarity(COMMON)
        );
        REEL_IRON_PRISMARINE = register(
                "reel_iron_prismarine",
                settings -> new ReelPartItem(settings)
                .weightClass(4)
                .minOperatingTemperature(-1)
                .maxOperatingTemperature(1)
                .bobberControl(ItemStat.BASE_T3)
                .fishControl(ItemStat.BASE_T3),
                new Item.Settings().rarity(UNCOMMON)
        );
        REEL_AMETHYST_PRISMARINE = register(
                "reel_amethyst_prismarine",
                settings -> new ReelPartItem(settings)
                .weightClass(3)
                .minOperatingTemperature(-1)
                .maxOperatingTemperature(1)
                .bobberControl(ItemStat.BASE_T2)
                .fishControl(ItemStat.BASE_T3)
                .timeHookedMultiplier(ItemStat.MULTIPLIER_T2)
                .fishQuality(1),
                new Item.Settings().rarity(UNCOMMON)
        );
        REEL_NAUTILUS_ECHO = register(
                "reel_nautilus_echo",
                settings -> new ReelPartItem(settings)
                .weightClass(5)
                .minOperatingTemperature(-2)
                .maxOperatingTemperature(1)
                .bobberControl(ItemStat.BASE_T3)
                .fishControl(ItemStat.BASE_T4)
                .timeHookedMultiplier(ItemStat.MULTIPLIER_T4),
                new Item.Settings().rarity(RARE)
        );
        REEL_NAUTILUS_AMETHYST = register(
                "reel_nautilus_amethyst",
                settings -> new ReelPartItem(settings)
                .weightClass(5)
                .minOperatingTemperature(-1)
                .maxOperatingTemperature(2)
                .bobberControl(ItemStat.BASE_T3)
                .fishControl(ItemStat.BASE_T3)
                .fishQuality(1),
                new Item.Settings().rarity(RARE)
        );
        REEL_HEART_AMETHYST = register(
                "reel_heart_amethyst",
                settings -> new ReelPartItem(settings)
                .weightClass(6)
                .minOperatingTemperature(-2)
                .maxOperatingTemperature(2)
                .bobberControl(ItemStat.BASE_T5)
                .fishControl(ItemStat.BASE_T3)
                .timeHookedMultiplier(ItemStat.MULTIPLIER_T5)
                .fishQuality(3),
                new Item.Settings().rarity(EPIC)
        );
        REEL_HEART_ECHO = register(
                "reel_heart_echo",
                settings -> new ReelPartItem(settings)
                .weightClass(6)
                .minOperatingTemperature(-2)
                .maxOperatingTemperature(2)
                .bobberControl(ItemStat.BASE_T3)
                .fishControl(ItemStat.BASE_T5)
                .timeHookedMultiplier(ItemStat.MULTIPLIER_T5)
                .fishQuality(1),
                new Item.Settings().rarity(EPIC)
        );
    }

    public static void registerBobber() {
        BOBBER_PLANT_SLIME = register(
                "bobber_plant_slime",
                settings -> new BobberPartItem(settings)
                .weightClass(1)
                .timeHookedMultiplier(ItemStat.MULTIPLIER_T4)
                .waitTimeReductionMultiplier(ItemStat.MULTIPLIER_T4),
                new Item.Settings().rarity(COMMON)
        );
        BOBBER_LEATHER_SLIME = register(
                "bobber_leather_slime",
                settings -> new BobberPartItem(settings)
                .weightClass(2)
                .timeHookedMultiplier(ItemStat.MULTIPLIER_T4)
                .waitTimeReductionMultiplier(ItemStat.MULTIPLIER_T4),
                new Item.Settings().rarity(COMMON)
        );
        BOBBER_TURTLE_SLIME = register(
                "bobber_turtle_slime",
                settings -> new BobberPartItem(settings)
                .weightClass(3)
                .bobberControl(ItemStat.BASE_T2),
                new Item.Settings().rarity(COMMON)
        );
        BOBBER_ARMADILLO_SLIME = register(
                "bobber_armadillo_slime",
                settings -> new BobberPartItem(settings)
                .weightClass(3)
                .fishControl(ItemStat.BASE_T2),
                new Item.Settings().rarity(COMMON)
        );
        BOBBER_TURTLE_ENDER = register(
                "bobber_turtle_ender",
                settings -> new BobberPartItem(settings)
                .weightClass(3)
                .minOperatingTemperature(-1)
                .bobberControl(ItemStat.BASE_T3),
                new Item.Settings().rarity(UNCOMMON)
        );
        BOBBER_ARMADILLO_MAGMA = register(
                "bobber_armadillo_magma",
                settings -> new BobberPartItem(settings)
                .weightClass(3)
                .maxOperatingTemperature(2)
                .waitTimeReductionMultiplier(ItemStat.MULTIPLIER_T4)
                .fishControl(ItemStat.BASE_T3),
                new Item.Settings().rarity(UNCOMMON)
        );
        BOBBER_AMETHYST_ENDER = register(
                "bobber_amethyst_ender",
                settings -> new BobberPartItem(settings)
                .weightClass(4)
                .minOperatingTemperature(-2)
                .timeHookedMultiplier(ItemStat.MULTIPLIER_T4)
                .bobberControl(ItemStat.BASE_T2)
                .fishControl(ItemStat.BASE_T2)
                .fishQuality(2),
                new Item.Settings().rarity(UNCOMMON)
        );
        BOBBER_NAUTILUS = register(
                "bobber_nautilus",
                settings -> new BobberPartItem(settings)
                .weightClass(5)
                .minOperatingTemperature(-1)
                .maxOperatingTemperature(1)
                .timeHookedMultiplier(ItemStat.MULTIPLIER_T5)
                .bobberControl(ItemStat.BASE_T3)
                .fishControl(ItemStat.BASE_T3),
                new Item.Settings().rarity(RARE)
        );
        BOBBER_TURTLE_ECHO = register(
                "bobber_turtle_echo",
                settings -> new BobberPartItem(settings)
                .weightClass(5)
                .minOperatingTemperature(-2)
                .maxOperatingTemperature(1)
                .bobberControl(ItemStat.BASE_T4)
                .fishControl(ItemStat.BASE_T2)
                .fishQuality(1),
                new Item.Settings().rarity(RARE)
        );
        BOBBER_ARMADILLO_ECHO = register(
                "bobber_armadillo_echo",
                settings -> new BobberPartItem(settings)
                .weightClass(5)
                .minOperatingTemperature(-1)
                .maxOperatingTemperature(2)
                .bobberControl(ItemStat.BASE_T2)
                .fishControl(ItemStat.BASE_T4)
                .fishQuality(1),
                new Item.Settings().rarity(RARE)
        );
        BOBBER_AMETHYST_ECHO = register(
                "bobber_amethyst_echo",
                settings -> new BobberPartItem(settings)
                .weightClass(5)
                .minOperatingTemperature(-1)
                .maxOperatingTemperature(2)
                .bobberControl(ItemStat.BASE_T5)
                .fishControl(ItemStat.BASE_T3)
                .fishQuality(1),
                new Item.Settings().rarity(RARE)
        );
        BOBBER_NAUTILUS_ECHO = register(
                "bobber_nautilus_echo",
                settings -> new BobberPartItem(settings)
                .weightClass(6)
                .minOperatingTemperature(-2)
                .maxOperatingTemperature(1)
                .timeHookedMultiplier(ItemStat.MULTIPLIER_T4)
                .waitTimeReductionMultiplier(ItemStat.MULTIPLIER_T5)
                .bobberControl(ItemStat.BASE_T6)
                .fishControl(ItemStat.BASE_T5)
                .fishQuality(2),
                new Item.Settings().rarity(EPIC)
        );
        BOBBER_HEART = register(
                "bobber_heart",
                settings -> new BobberPartItem(settings)
                .weightClass(6)
                .minOperatingTemperature(-2)
                .maxOperatingTemperature(2)
                .timeHookedMultiplier(ItemStat.MULTIPLIER_T5)
                .waitTimeReductionMultiplier(ItemStat.MULTIPLIER_T5)
                .bobberControl(ItemStat.BASE_T5)
                .fishControl(ItemStat.BASE_T3)
                .fishQuality(4),
                new Item.Settings().rarity(EPIC)
        );
    }

    public static void registerLine() {

        LINE_PLANT = register(
                "line_plant",
                settings -> new LinePartItem(settings)
                .weightClass(1)
                .maxLineLength(4),
                new Item.Settings().rarity(COMMON)
        );

        LINE_SPIDER = Items.register(
                "line_spider",
                settings -> new LinePartItem(settings)
                        .weightClass(2)
                        .minOperatingTemperature(-1)
                        .maxOperatingTemperature(1)
                        .maxLineLength(16)
                        .fishControl(ItemStat.BASE_T2),
                new Item.Settings().rarity(COMMON)
        );
        LINE_WOOL = register(
                "line_wool",
                settings -> new LinePartItem(settings)
                .weightClass(2)
                .maxLineLength(8)
                .fishControl(ItemStat.BASE_T2),
                new Item.Settings().rarity(COMMON)
        );
        LINE_STRIDER = register(
                "line_strider",
                settings -> new LinePartItem(settings)
                .weightClass(3)
                .minOperatingTemperature(1)
                .maxOperatingTemperature(1)
                .maxLineLength(16)
                .fishControl(ItemStat.BASE_T3),
                new Item.Settings().rarity(UNCOMMON)
        );
        LINE_PHANTOM = register(
                "line_phantom",
                settings -> new LinePartItem(settings)
                .weightClass(3)
                .minOperatingTemperature(-1)
                .maxLineLength(32)
                .fishControl(ItemStat.BASE_T2)
                .fishQuality(1),
                new Item.Settings().rarity(UNCOMMON)
        );
        LINE_EVOKER = register(
                "line_evoker",
                settings -> new LinePartItem(settings)
                .weightClass(4)
                .minOperatingTemperature(-2)
                .maxOperatingTemperature(1)
                .maxLineLength(64)
                .fishControl(ItemStat.BASE_T3)
                .fishQuality(1),
                new Item.Settings().rarity(RARE)
        );
        LINE_PIGLIN = register(
                "line_piglin",
                settings -> new LinePartItem(settings)
                .weightClass(5)
                .minOperatingTemperature(-1)
                .maxOperatingTemperature(2)
                .maxLineLength(64)
                .fishControl(ItemStat.BASE_T3),
                new Item.Settings().rarity(RARE)
        );
        LINE_HEART = register(
                "line_heart",
                settings -> new LinePartItem(settings)
                .weightClass(4)
                .minOperatingTemperature(-2)
                .maxOperatingTemperature(1)
                .maxLineLength(128)
                .fishControl(ItemStat.BASE_T5),
                new Item.Settings().rarity(RARE)
        );
        LINE_HEART_PIGLIN = register(
                "line_heart_piglin",
                settings -> new LinePartItem(settings)
                .weightClass(6)
                .minOperatingTemperature(-2)
                .maxOperatingTemperature(2)
                .maxLineLength(128)
                .fishControl(ItemStat.BASE_T5)
                .fishQuality(1),
                new Item.Settings().rarity(EPIC)
        );
        LINE_HEART_EVOKER = register(
                "line_heart_evoker",
                settings -> new LinePartItem(settings)
                .weightClass(5)
                .minOperatingTemperature(-2)
                .maxOperatingTemperature(2)
                .maxLineLength(256)
                .fishControl(ItemStat.BASE_T5)
                .fishQuality(3),
                new Item.Settings().rarity(EPIC)
        );
    }

    private static void registerHook() {
        HOOK_FLINT = register(
                "hook_flint",
                settings -> new HookPartItem(settings)
                .weightClass(1)
                .minOperatingTemperature(-1)
                .maxOperatingTemperature(1)
                .fishRarity(ItemStat.BASE_T1)
                .fishControl(ItemStat.BASE_T2),
                new Item.Settings().rarity(COMMON)
        );
        HOOK_FLINT_POISON = register(
                "hook_flint_poison",
                settings -> new HookPartItem(settings)
                .weightClass(1)
                .minOperatingTemperature(-1)
                .maxOperatingTemperature(1)
                .fishRarity(ItemStat.BASE_T1)
                .fishControl(ItemStat.BASE_T2)
                .timeHookedMultiplier(ItemStat.MULTIPLIER_T4)
                .addOnEntityHitEffects(HookPartItem.ON_HIT_POISON),
                new Item.Settings().rarity(COMMON)
        );
        HOOK_FLINT_SHARP = register(
                "hook_flint_sharp",
                settings -> new HookPartItem(settings)
                .weightClass(1)
                .minOperatingTemperature(-1)
                .maxOperatingTemperature(1)
                .fishRarity(ItemStat.BASE_T1)
                .fishControl(ItemStat.BASE_T2)
                .damage(2)
                .biteFailChance(new ItemStat(0.1f, 1)),
                new Item.Settings().rarity(COMMON)
        );
        HOOK_FLINT_SERRATED = register(
                "hook_flint_serrated",
                settings -> new HookPartItem(settings)
                .weightClass(1)
                .minOperatingTemperature(-1)
                .maxOperatingTemperature(1)
                .fishRarity(ItemStat.BASE_T1)
                .fishControl(ItemStat.BASE_T2)
                .fishQuality(-1)
                .timeHookedMultiplier(ItemStat.MULTIPLIER_T4)
                .reelDamage(3),
                new Item.Settings().rarity(COMMON)
        );
        HOOK_BONE = register(
                "hook_bone",
                settings -> new HookPartItem(settings)
                .weightClass(2)
                .minOperatingTemperature(-1)
                .maxOperatingTemperature(1)
                .fishRarity(ItemStat.BASE_T2)
                .fishControl(ItemStat.BASE_T1),
                new Item.Settings().rarity(COMMON)
        );
        HOOK_BONE_POISON = register(
                "hook_bone_poison",
                settings -> new HookPartItem(settings)
                .weightClass(2)
                .minOperatingTemperature(-1)
                .maxOperatingTemperature(1)
                .fishRarity(ItemStat.BASE_T2)
                .fishControl(ItemStat.BASE_T1)
                .timeHookedMultiplier(ItemStat.MULTIPLIER_T4)
                .addOnEntityHitEffects(HookPartItem.ON_HIT_POISON),
                new Item.Settings().rarity(COMMON)
        );
        HOOK_BONE_SHARP = register(
                "hook_bone_sharp",
                settings -> new HookPartItem(settings)
                .weightClass(2)
                .minOperatingTemperature(-1)
                .maxOperatingTemperature(1)
                .fishRarity(ItemStat.BASE_T2)
                .fishControl(ItemStat.BASE_T1)
                .damage(2)
                .biteFailChance(ItemStat.MULTIPLIER_T01),
                new Item.Settings().rarity(COMMON)
        );
        HOOK_BONE_SERRATED = register(
                "hook_bone_serrated",
                settings -> new HookPartItem(settings)
                .weightClass(2)
                .minOperatingTemperature(-1)
                .maxOperatingTemperature(1)
                .fishRarity(ItemStat.BASE_T2)
                .fishControl(ItemStat.BASE_T1)
                .fishQuality(-1)
                .timeHookedMultiplier(ItemStat.MULTIPLIER_T4)
                .reelDamage(3),
                new Item.Settings().rarity(COMMON)
        );
        HOOK_COPPER = register(
                "hook_copper",
                settings -> new HookPartItem(settings)
                .weightClass(3)
                .minOperatingTemperature(-1)
                .fishRarity(ItemStat.BASE_T3)
                .fishControl(ItemStat.BASE_T1),
                new Item.Settings().rarity(COMMON)
        );
        HOOK_COPPER_POISON = register(
                "hook_copper_poison",
                settings -> new HookPartItem(settings)
                .weightClass(3)
                .minOperatingTemperature(-1)
                .fishRarity(ItemStat.BASE_T3)
                .fishControl(ItemStat.BASE_T1)
                .timeHookedMultiplier(ItemStat.MULTIPLIER_T8)
                .addOnEntityHitEffects(HookPartItem.ON_HIT_POISON),
                new Item.Settings().rarity(COMMON)
        );
        HOOK_COPPER_SHARP = register(
                "hook_copper_sharp",
                settings -> new HookPartItem(settings)
                .weightClass(3)
                .minOperatingTemperature(-1)
                .fishRarity(ItemStat.BASE_T3)
                .fishControl(ItemStat.BASE_T1)
                .damage(3)
                .biteFailChance(ItemStat.MULTIPLIER_T0125),
                new Item.Settings().rarity(COMMON)
        );
        HOOK_COPPER_SERRATED = register(
                "hook_copper_serrated",
                settings -> new HookPartItem(settings)
                .weightClass(3)
                .minOperatingTemperature(-1)
                .fishRarity(ItemStat.BASE_T3)
                .fishControl(ItemStat.BASE_T1)
                .fishQuality(-1)
                .timeHookedMultiplier(ItemStat.MULTIPLIER_T6)
                .reelDamage(4),
                new Item.Settings().rarity(COMMON)
        );
        HOOK_COPPER_STICKY = register(
                "hook_copper_sticky",
                settings -> new HookPartItem(settings)
                .weightClass(3)
                .minOperatingTemperature(-1)
                .fishRarity(ItemStat.BASE_T3)
                .fishControl(ItemStat.BASE_T1)
                .sticky(true),
                new Item.Settings().rarity(COMMON)
        );
        HOOK_IRON = register(
                "hook_iron",
                settings -> new HookPartItem(settings)
                .weightClass(3)
                .minOperatingTemperature(-1)
                .maxOperatingTemperature(1)
                .fishRarity(ItemStat.BASE_T1)
                .fishControl(ItemStat.BASE_T3),
                new Item.Settings().rarity(COMMON)
        );
        HOOK_IRON_POISON = register(
                "hook_iron_poison",
                settings -> new HookPartItem(settings)
                .weightClass(4)
                .minOperatingTemperature(-1)
                .maxOperatingTemperature(1)
                .fishRarity(ItemStat.BASE_T1)
                .fishControl(ItemStat.BASE_T3)
                .timeHookedMultiplier(ItemStat.MULTIPLIER_T8)
                .addOnEntityHitEffects(HookPartItem.ON_HIT_POISON),
                new Item.Settings().rarity(COMMON)
        );
        HOOK_IRON_SHARP = register(
                "hook_iron_sharp",
                settings -> new HookPartItem(settings)
                .weightClass(4)
                .minOperatingTemperature(-1)
                .maxOperatingTemperature(1)
                .fishRarity(ItemStat.BASE_T1)
                .fishControl(ItemStat.BASE_T3)
                .damage(3)
                .biteFailChance(ItemStat.MULTIPLIER_T0125),
                new Item.Settings().rarity(COMMON)
        );
        HOOK_IRON_SERRATED = register(
                "hook_iron_serrated",
                settings -> new HookPartItem(settings)
                .weightClass(4)
                .minOperatingTemperature(-1)
                .maxOperatingTemperature(1)
                .fishRarity(ItemStat.BASE_T1)
                .fishControl(ItemStat.BASE_T3)
                .fishQuality(-1)
                .timeHookedMultiplier(ItemStat.MULTIPLIER_T6)
                .reelDamage(4),
                new Item.Settings().rarity(COMMON)
        );
        HOOK_IRON_STICKY = register(
                "hook_iron_sticky",
                settings -> new HookPartItem(settings)
                .weightClass(4)
                .minOperatingTemperature(-1)
                .maxOperatingTemperature(1)
                .fishRarity(ItemStat.BASE_T1)
                .fishControl(ItemStat.BASE_T3)
                .sticky(true),
                new Item.Settings().rarity(COMMON)
        );
        HOOK_GOLD = register(
                "hook_gold",
                settings -> new HookPartItem(settings)
                .weightClass(3)
                .fishRarity(ItemStat.BASE_T3)
                .fishControl(ItemStat.BASE_T2)
                .fishQuality(1),
                new Item.Settings().rarity(UNCOMMON)
        );
        HOOK_GOLD_POISON = register(
                "hook_gold_poison",
                settings -> new HookPartItem(settings)
                .weightClass(3)
                .fishRarity(ItemStat.BASE_T3)
                .fishControl(ItemStat.BASE_T2)
                .fishQuality(1)
                .timeHookedMultiplier(ItemStat.MULTIPLIER_T9)
                .addOnEntityHitEffects(HookPartItem.ON_HIT_POISON),
                new Item.Settings().rarity(UNCOMMON)
        );
        HOOK_GOLD_SHARP = register(
                "hook_gold_sharp",
                settings -> new HookPartItem(settings)
                .weightClass(3)
                .fishRarity(ItemStat.BASE_T3)
                .fishControl(ItemStat.BASE_T2)
                .fishQuality(1)
                .damage(4)
                .biteFailChance(ItemStat.MULTIPLIER_T015),
                new Item.Settings().rarity(UNCOMMON)
        );
        HOOK_GOLD_SERRATED = register(
                "hook_gold_serrated",
                settings -> new HookPartItem(settings)
                .weightClass(3)
                .fishRarity(ItemStat.BASE_T3)
                .fishControl(ItemStat.BASE_T2)
                .timeHookedMultiplier(ItemStat.MULTIPLIER_T5)
                .reelDamage(5),
                new Item.Settings().rarity(UNCOMMON)
        );
        HOOK_GOLD_STICKY = register(
                "hook_gold_sticky",
                settings -> new HookPartItem(settings)
                .weightClass(3)
                .fishRarity(ItemStat.BASE_T3)
                .fishControl(ItemStat.BASE_T2)
                .fishQuality(1)
                .sticky(true),
                new Item.Settings().rarity(UNCOMMON)
        );
        HOOK_PRISMARINE = register(
                "hook_prismarine",
                settings -> new HookPartItem(settings)
                .weightClass(4)
                .minOperatingTemperature(-1)
                .maxOperatingTemperature(1)
                .fishRarity(ItemStat.BASE_T2)
                .fishControl(ItemStat.BASE_T3),
                new Item.Settings().rarity(UNCOMMON)
        );
        HOOK_PRISMARINE_POISON = register(
                "hook_prismarine_poison",
                settings -> new HookPartItem(settings)
                .weightClass(4)
                .minOperatingTemperature(-1)
                .maxOperatingTemperature(1)
                .fishRarity(ItemStat.BASE_T2)
                .fishControl(ItemStat.BASE_T3)
                .timeHookedMultiplier(ItemStat.MULTIPLIER_T9)
                .addOnEntityHitEffects(HookPartItem.ON_HIT_POISON),
                new Item.Settings().rarity(UNCOMMON)
        );
        HOOK_PRISMARINE_SHARP = register(
                "hook_prismarine_sharp",
                settings -> new HookPartItem(settings)
                .weightClass(4)
                .minOperatingTemperature(-1)
                .maxOperatingTemperature(1)
                .fishRarity(ItemStat.BASE_T2)
                .fishControl(ItemStat.BASE_T3)
                .damage(4)
                .biteFailChance(ItemStat.MULTIPLIER_T015),
                new Item.Settings().rarity(UNCOMMON)
        );
        HOOK_PRISMARINE_SERRATED = register(
                "hook_prismarine_serrated",
                settings -> new HookPartItem(settings)
                .weightClass(4)
                .minOperatingTemperature(-1)
                .maxOperatingTemperature(1)
                .fishRarity(ItemStat.BASE_T2)
                .fishControl(ItemStat.BASE_T3)
                .timeHookedMultiplier(ItemStat.MULTIPLIER_T5)
                .reelDamage(5),
                new Item.Settings().rarity(UNCOMMON)
        );
        HOOK_PRISMARINE_STICKY = register(
                "hook_prismarine_sticky",
                settings -> new HookPartItem(settings)
                .weightClass(4)
                .minOperatingTemperature(-1)
                .maxOperatingTemperature(1)
                .fishRarity(ItemStat.BASE_T2)
                .fishControl(ItemStat.BASE_T3)
                .sticky(true),
                new Item.Settings().rarity(UNCOMMON)
        );
        HOOK_NAUTILUS = register(
                "hook_nautilus",
                settings -> new HookPartItem(settings)
                .weightClass(5)
                .minOperatingTemperature(-1)
                .maxOperatingTemperature(1)
                .fishRarity(ItemStat.BASE_T5)
                .fishControl(ItemStat.BASE_T3)
                .fishQuality(2),
                new Item.Settings().rarity(RARE)
        );
        HOOK_NAUTILUS_POISON = register(
                "hook_nautilus_poison",
                settings -> new HookPartItem(settings)
                .weightClass(5)
                .minOperatingTemperature(-1)
                .maxOperatingTemperature(1)
                .fishRarity(ItemStat.BASE_T5)
                .fishControl(ItemStat.BASE_T3)
                .fishQuality(2)
                .timeHookedMultiplier(ItemStat.MULTIPLIER_T10)
                .addOnEntityHitEffects(HookPartItem.ON_HIT_POISON)
                .addOnEntityHitEffects(HookPartItem.ON_HIT_WITHER),
                new Item.Settings().rarity(EPIC)
        );
        HOOK_NAUTILUS_SHARP = register(
                "hook_nautilus_sharp",
                settings -> new HookPartItem(settings)
                .weightClass(5)
                .minOperatingTemperature(-1)
                .maxOperatingTemperature(1)
                .fishRarity(ItemStat.BASE_T5)
                .fishControl(ItemStat.BASE_T3)
                .fishQuality(2)
                .damage(6)
                .biteFailChance(ItemStat.MULTIPLIER_T01),
                new Item.Settings().rarity(EPIC)
        );
        HOOK_NAUTILUS_SERRATED = register(
                "hook_nautilus_serrated",
                settings -> new HookPartItem(settings)
                .weightClass(5)
                .minOperatingTemperature(-1)
                .maxOperatingTemperature(1)
                .fishRarity(ItemStat.BASE_T5)
                .fishControl(ItemStat.BASE_T3)
                .timeHookedMultiplier(ItemStat.MULTIPLIER_T10)
                .reelDamage(7),
                new Item.Settings().rarity(EPIC)
        );
        HOOK_NAUTILUS_STICKY = register(
                "hook_nautilus_sticky",
                settings -> new HookPartItem(settings)
                .weightClass(5)
                .minOperatingTemperature(-1)
                .maxOperatingTemperature(1)
                .fishRarity(ItemStat.BASE_T5)
                .fishControl(ItemStat.BASE_T3)
                .fishQuality(2)
                .sticky(true),
                new Item.Settings().rarity(EPIC)
        );
        HOOK_NETHERITE = register(
                "hook_netherite",
                settings -> new HookPartItem(settings)
                .weightClass(6)
                .minOperatingTemperature(-2)
                .maxOperatingTemperature(2)
                .fishRarity(ItemStat.BASE_T3)
                .fishControl(ItemStat.BASE_T5),
                new Item.Settings().rarity(RARE)
        );
        HOOK_NETHERITE_POISON = register(
                "hook_netherite_poison",
                settings -> new HookPartItem(settings)
                .weightClass(6)
                .minOperatingTemperature(-2)
                .maxOperatingTemperature(2)
                .fishRarity(ItemStat.BASE_T3)
                .fishControl(ItemStat.BASE_T5)
                .timeHookedMultiplier(ItemStat.MULTIPLIER_T10)
                .addOnEntityHitEffects(HookPartItem.ON_HIT_POISON)
                .addOnEntityHitEffects(HookPartItem.ON_HIT_FIRE),
                new Item.Settings().rarity(EPIC)
        );
        HOOK_NETHERITE_SHARP = register(
                "hook_netherite_sharp",
                settings -> new HookPartItem(settings)
                .weightClass(6)
                .minOperatingTemperature(-2)
                .maxOperatingTemperature(2)
                .fishRarity(ItemStat.BASE_T3)
                .fishControl(ItemStat.BASE_T5)
                .damage(9)
                .biteFailChance(ItemStat.MULTIPLIER_T02),
                new Item.Settings().rarity(EPIC)
        );
        HOOK_NETHERITE_SERRATED = register(
                "hook_netherite_serrated",
                settings -> new HookPartItem(settings)
                .weightClass(6)
                .minOperatingTemperature(-2)
                .maxOperatingTemperature(2)
                .fishRarity(ItemStat.BASE_T3)
                .fishControl(ItemStat.BASE_T5)
                .fishQuality(-2)
                .timeHookedMultiplier(ItemStat.MULTIPLIER_T10)
                .reelDamage(10),
                new Item.Settings().rarity(EPIC)
        );
        HOOK_NETHERITE_STICKY = register(
                "hook_netherite_sticky",
                settings -> new HookPartItem(settings)
                .weightClass(6)
                .minOperatingTemperature(-2)
                .maxOperatingTemperature(2)
                .fishRarity(ItemStat.BASE_T3)
                .fishControl(ItemStat.BASE_T5)
                .sticky(true),
                new Item.Settings().rarity(EPIC)
        );
    }

    private static void registerBait() {
        BAIT_PLANT = register(
                "bait_plant",
                settings -> new BaitPartItem(settings)
                .minOperatingTemperature(-1)
                .fishRarity(ItemStat.BASE_T1)
                .plant()
                .waitTimeReductionMultiplier(ItemStat.MULTIPLIER_T3),
                new Item.Settings().maxDamage(64).rarity(COMMON)
        );
        BAIT_ROTTEN_FLESH = register(
                "bait_rotten_flesh",
                settings -> new BaitPartItem(settings)
                .minOperatingTemperature(-1)
                .maxOperatingTemperature(1)
                .fishRarity(ItemStat.BASE_T1)
                .meat()
                .waitTimeReductionMultiplier(ItemStat.MULTIPLIER_T3),
                new Item.Settings().maxDamage(64).rarity(COMMON)
        );
        BAIT_MEAT = register(
                "bait_meat",
                settings -> new BaitPartItem(settings)
                .minOperatingTemperature(-1)
                .maxOperatingTemperature(1)
                .fishRarity(ItemStat.BASE_T2)
                .meat()
                .waitTimeReductionMultiplier(ItemStat.MULTIPLIER_T4),
                new Item.Settings().maxDamage(64).rarity(COMMON)
        );
        BAIT_ORCHID = register(
                "bait_orchid",
                settings -> new BaitPartItem(settings)
                .minOperatingTemperature(-1)
                .fishRarity(ItemStat.BASE_T2)
                .plant()
                .waitTimeReductionMultiplier(ItemStat.MULTIPLIER_T4),
                new Item.Settings().maxDamage(64).rarity(COMMON)
        );
        BAIT_GLOW_BERRIES = register(
                "bait_glow_berries",
                settings -> new BaitPartItem(settings)
                .minOperatingTemperature(-1)
                .fishRarity(ItemStat.BASE_T3)
                .plant()
                .timeHookedMultiplier(ItemStat.MULTIPLIER_T4)
                .fishQuantityBonus(ItemStat.BASE_T2)
                .fishQuality(1),
                new Item.Settings().maxDamage(64).rarity(COMMON)
        );
        BAIT_BLAZE_POWDER = register(
                "bait_blaze_powder",
                settings -> new BaitPartItem(settings)
                .minOperatingTemperature(1)
                .maxOperatingTemperature(1)
                .fishQuality(1),
                new Item.Settings().maxDamage(64).rarity(COMMON)
        );//todo add nether species bonuses
        BAIT_IRON = register(
                "bait_iron",
                settings -> new BaitPartItem(settings)
                .minOperatingTemperature(-1)
                .maxOperatingTemperature(1)
                .treasureBonus(ItemStat.BASE_T3)
                .treasureRarityBonus(ItemStat.BASE_T3)
                .waitTimeReductionMultiplier(ItemStat.MULTIPLIER_T0125)
                .fishQuality(-1),
                new Item.Settings().maxDamage(64).rarity(COMMON)
        );
        BAIT_GHAST = register(
                "bait_ghast",
                settings -> new BaitPartItem(settings)
                .minOperatingTemperature(-1)
                .maxOperatingTemperature(2)
                .fishQuality(2),
                new Item.Settings().maxDamage(64).rarity(COMMON)
        );//todo add nether species bonuses
        BAIT_RABBIT = register(
                "bait_rabbit",
                settings -> new BaitPartItem(settings)
                .minOperatingTemperature(-1)
                .maxOperatingTemperature(1)
                .fishRarity(ItemStat.BASE_T3)
                .meat()
                .waitTimeReductionMultiplier(ItemStat.MULTIPLIER_T10)
                .timeHookedMultiplier(ItemStat.MULTIPLIER_T10)
                .fishQuality(1),
                new Item.Settings().maxDamage(64).rarity(COMMON)
        );
        BAIT_CHORUS = register(
                "bait_chorus",
                settings -> new BaitPartItem(settings)
                .minOperatingTemperature(-1)
                .maxOperatingTemperature(1)
                .fishRarity(ItemStat.BASE_T3)
                .plant()
                .timeHookedMultiplier(ItemStat.MULTIPLIER_T10)
                .fishQuantityBonus(ItemStat.BASE_T3)
                .fishQuality(2),
                new Item.Settings().maxDamage(64).rarity(COMMON)
        );
        BAIT_BREEZE_BLAZE = register(
                "bait_breeze_blaze",
                settings -> new BaitPartItem(settings)
                .minOperatingTemperature(-2)
                .maxOperatingTemperature(2)
                .treasureBonus(ItemStat.BASE_T6)
                .treasureRarityBonus(ItemStat.BASE_T6)
                .waitTimeReductionMultiplier(ItemStat.MULTIPLIER_T025)
                .fishQuality(-1),
                new Item.Settings().maxDamage(32).rarity(COMMON)
        );
        BAIT_TORCHFLOWER = register(
                "bait_torchflower",
                settings -> new BaitPartItem(settings)
                .minOperatingTemperature(-1)
                .maxOperatingTemperature(2)
                .fishRarity(ItemStat.BASE_T5)
                .meat()
                .plant()
                .timeHookedMultiplier(ItemStat.MULTIPLIER_T4)
                .waitTimeReductionMultiplier(ItemStat.MULTIPLIER_T5)
                .fishQuantityBonus(ItemStat.BASE_T3)
                .fishQuality(5)
                .treasureBonus(ItemStat.BASE_T2)
                .treasureRarityBonus(ItemStat.BASE_T2),
                new Item.Settings().maxDamage(128).rarity(COMMON)
        );
        BAIT_PITCHER_PLANT = register(
                "bait_pitcher_plant",
                settings -> new BaitPartItem(settings)
                .minOperatingTemperature(-2)
                .maxOperatingTemperature(1)
                .fishRarity(ItemStat.BASE_T5)
                .meat()
                .plant()
                .timeHookedMultiplier(ItemStat.MULTIPLIER_T10)
                .waitTimeReductionMultiplier(ItemStat.MULTIPLIER_T10)
                .fishQuantityBonus(ItemStat.BASE_T5)
                .fishQuality(5)
                .treasureBonus(ItemStat.BASE_T2)
                .treasureRarityBonus(ItemStat.BASE_T2),
                new Item.Settings().maxDamage(32).rarity(COMMON)
        );
        BAIT_SPECIAL = register(
                "bait_special",
                settings -> new BaitPartItem(settings)
                .minOperatingTemperature(-2)
                .maxOperatingTemperature(1),
                new Item.Settings().maxDamage(64).rarity(UNCOMMON)
        );

    }

    public static final ArrayList<net.minecraft.item.ItemStack> FISHING_ITEMS = new ArrayList<>();

}
