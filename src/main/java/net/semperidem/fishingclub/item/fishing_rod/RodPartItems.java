package net.semperidem.fishingclub.item.fishing_rod;

import net.minecraft.item.Item;
import net.semperidem.fishingclub.item.fishing_rod.components.LinePartItem;
import net.semperidem.fishingclub.item.fishing_rod.components.PartItem;
import net.semperidem.fishingclub.item.fishing_rod.components.CorePartItem;

import static net.minecraft.util.Rarity.*;
import static net.semperidem.fishingclub.registry.ItemRegistry.registerItem;

public class RodPartItems {
  public static PartItem EMPTY_COMPONENT;
  public static CorePartItem CORE_WOODEN_OAK;
  public static CorePartItem CORE_IRON;
  public static LinePartItem LINE_SPIDER;
  public static LinePartItem LINE_WOOL;

  public static void registerParts() {
    EMPTY_COMPONENT = registerItem("empty_component", new PartItem(new Item.Settings()));
    CORE_WOODEN_OAK =
        registerItem(
            "core_wooden_oak",
            new CorePartItem(new Item.Settings().maxDamage(64).rarity(COMMON))
                .weightCapacity(10)
                .castPowerMultiplier(0.9f));
    CORE_IRON =
        registerItem(
            "core_iron",
            new CorePartItem(new Item.Settings().maxDamage(128).rarity(COMMON))
                .weightCapacity(15)
                .castPowerMultiplier(0.9f));
    LINE_SPIDER =
        registerItem(
            "line_spider",
            new LinePartItem(new Item.Settings().maxDamage(32).rarity(COMMON))
                .weightCapacity(15)
                .maxLineLength(8));
    LINE_WOOL =
        registerItem(
            "line_wool",
            new LinePartItem(new Item.Settings().maxDamage(32).rarity(COMMON))
                .weightCapacity(10)
                .maxLineLength(64));
  }
}
