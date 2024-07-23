package net.semperidem.fishing_club.registry;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.MapColor;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.semperidem.fishing_club.FishingClub;
import net.semperidem.fishing_club.block.TackleBoxBlock;
import net.semperidem.fishing_club.entity.TackleBoxBlockEntity;

public class FCBlocks {
    public static TackleBoxBlock TACKLE_BOX_BLOCK;
    public static BlockEntityType<TackleBoxBlockEntity> TACKLE_BOX;


    public static void register() {
        TACKLE_BOX_BLOCK = Registry.register(
          Registries.BLOCK,
          FishingClub.getIdentifier("tackle_box"),
          new TackleBoxBlock(AbstractBlock.Settings.create()
            .mapColor(MapColor.CYAN)
            .solid()
            .strength(2.0F)
            .dynamicBounds()
            .nonOpaque()
            .pistonBehavior(PistonBehavior.DESTROY))
        );

        TACKLE_BOX = Registry.register(
          Registries.BLOCK_ENTITY_TYPE,
          FishingClub.getIdentifier("tackle_box"),
          BlockEntityType.Builder.create(TackleBoxBlockEntity::new, FCBlocks.TACKLE_BOX_BLOCK).build()
        );
    }
}
