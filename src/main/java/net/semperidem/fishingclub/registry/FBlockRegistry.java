package net.semperidem.fishingclub.registry;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.semperidem.fishingclub.block.FisherWorkbenchBlock;

import static net.semperidem.fishingclub.FishingClub.MOD_ID;

public class FBlockRegistry {
    public static final Block FISHER_WORKBENCH_BLOCK = new FisherWorkbenchBlock(FabricBlockSettings.copy(Blocks.CRAFTING_TABLE));

    public static void register(){
        Registry.register(Registry.BLOCK, new Identifier(MOD_ID, "fisher_workbench"), FBlockRegistry.FISHER_WORKBENCH_BLOCK);
    }
}
