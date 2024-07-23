package net.semperidem.fishing_club.registry;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.MapColor;
import net.minecraft.block.WoodType;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.semperidem.fishing_club.FishingClub;
import net.semperidem.fishing_club.block.FishDisplayBlock;
import net.semperidem.fishing_club.block.TackleBoxBlock;
import net.semperidem.fishing_club.entity.FishDisplayBlockEntity;
import net.semperidem.fishing_club.entity.TackleBoxBlockEntity;

import static net.minecraft.block.Blocks.BAMBOO_PLANKS;

public class FCBlocks {
    public static TackleBoxBlock TACKLE_BOX_BLOCK;
    public static BlockEntityType<TackleBoxBlockEntity> TACKLE_BOX;
    public static FishDisplayBlock FISH_DISPLAY_BLOCK_BAMBOO;
    public static BlockEntityType<FishDisplayBlockEntity> FISH_DISPLAY;


    private static FishDisplayBlock registerFishDisplayBlock(WoodType woodType, MapColor mapColor) {
        return Registry.register(
          Registries.BLOCK,
          FishingClub.getIdentifier("fish_display_" + woodType.name().toLowerCase()),
          new FishDisplayBlock(
            woodType,
            AbstractBlock.Settings.create()
              .mapColor(mapColor)
              .solid()
              .instrument(NoteBlockInstrument.BASS)
              .noCollision()
              .strength(1.0F)
              .burnable()
          ));
    }


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

        FISH_DISPLAY_BLOCK_BAMBOO = registerFishDisplayBlock(WoodType.BAMBOO, BAMBOO_PLANKS.getDefaultMapColor());

        TACKLE_BOX = Registry.register(
          Registries.BLOCK_ENTITY_TYPE,
          FishingClub.getIdentifier("tackle_box"),
          BlockEntityType.Builder.create(TackleBoxBlockEntity::new, FCBlocks.TACKLE_BOX_BLOCK).build()
        );
        FISH_DISPLAY = Registry.register(
          Registries.BLOCK_ENTITY_TYPE,
          FishingClub.getIdentifier("fish_display_bamboo"),
          BlockEntityType.Builder.create(FishDisplayBlockEntity::new, FCBlocks.FISH_DISPLAY_BLOCK_BAMBOO, FCBlocks.FISH_DISPLAY_BLOCK_BAMBOO).build()
        );
    }
}
