package net.semperidem.fishingclub.mixin.common;

import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.registry.RegistryKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LootTables.class)
public interface LootTablesAccessor {
@Invoker("registerLootTable")
 public static RegistryKey<LootTable> registerLootTable(RegistryKey<LootTable> key) {
    throw new AssertionError();
    }
}
