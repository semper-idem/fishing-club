package net.semperidem.fishingclub.registry;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.registry.Registry;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.block.FisherWorkbenchBlock;


public class BlockRegistry {
    public static final Block FISHER_WORKBENCH_BLOCK = new FisherWorkbenchBlock(FabricBlockSettings.copy(Blocks.CRAFTING_TABLE));

    public static void register(){
        Registry.register(Registry.BLOCK, FishingClub.getIdentifier("fisher_workbench"), BlockRegistry.FISHER_WORKBENCH_BLOCK);
    }
}
