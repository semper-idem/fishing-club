package net.semperidem.fishingclub.registry;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.WoodType;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.block.*;
import net.semperidem.fishingclub.entity.FishDisplayBlockEntity;
import net.semperidem.fishingclub.entity.TackleBoxBlockEntity;

import static net.minecraft.block.Blocks.BAMBOO_PLANKS;

public class Blocks {
    public static TackleBoxBlock TACKLE_BOX_BLOCK;
    public static BlockEntityType<TackleBoxBlockEntity> TACKLE_BOX;
    public static FishDisplayBlock FISH_DISPLAY_BLOCK_BAMBOO;
    public static BlockEntityType<FishDisplayBlockEntity> FISH_DISPLAY;
    public static ReedBlock REED_BLOCK;
    public static EnergyDenseKelpBlock ENERGY_DENSE_KELP;
    public static EnergyDenseKelpPlantBlock ENERGY_DENSE_KELP_PLANT;
    public static Block DRIED_ENERGY_DENSE_KELP_BLOCK;
    public static NutritiousKelpBlock NUTRITIOUS_KELP;
    public static NutritiousKelpPlantBlock NUTRITIOUS_KELP_PLANT;
    public static Block DRIED_NUTRITIOUS_KELP_BLOCK;
    public static WaterloggedLilyPadBlock WATERLOGGED_LILY_PAD_BLOCK;
    public static DuckweedBlock DUCKWEED_BLOCK;

    private static FishDisplayBlock registerFishDisplayBlock(WoodType woodType, MapColor mapColor) {
        return Registry.register(
          Registries.BLOCK,
          FishingClub.identifier("fish_display_" + woodType.name().toLowerCase()),
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

        DUCKWEED_BLOCK = Registry.register(
            Registries.BLOCK,
            FishingClub.identifier("duckweed"),
            new DuckweedBlock(
                AbstractBlock.Settings.create().
                    mapColor(MapColor.DARK_GREEN)
                    .noCollision()
                    .ticksRandomly()
                    .sounds(BlockSoundGroup.PINK_PETALS).pistonBehavior(PistonBehavior.DESTROY)
            )
        );


        WATERLOGGED_LILY_PAD_BLOCK = Registry.register(
            Registries.BLOCK,
            FishingClub.identifier("waterlogged_lily_pad"),
            new WaterloggedLilyPadBlock(
                AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_GREEN)
                    .breakInstantly()
                    .sounds(BlockSoundGroup.LILY_PAD)
                    .nonOpaque()
                    .pistonBehavior(PistonBehavior.DESTROY)
            )
        );


        DRIED_NUTRITIOUS_KELP_BLOCK = Registry.register(
            Registries.BLOCK,
            FishingClub.identifier("dried_nutritious_kelp_block"),
            new Block(
                AbstractBlock.Settings.create()
                    .mapColor(MapColor.GREEN)
                    .strength(0.5F, 2.5F)
                    .sounds(BlockSoundGroup.GRASS)
            )
        );

        NUTRITIOUS_KELP_PLANT = Registry.register(
            Registries.BLOCK,
            FishingClub.identifier("nutritious_kelp_plant"),
            new NutritiousKelpPlantBlock(
                AbstractBlock.Settings.create()
                    .mapColor(MapColor.WATER_BLUE)
                    .noCollision()
                    .breakInstantly()
                    .sounds(BlockSoundGroup.WET_GRASS)
                    .pistonBehavior(PistonBehavior.DESTROY)
            ));
        NUTRITIOUS_KELP = Registry.register(
            Registries.BLOCK,
            FishingClub.identifier("nutritious_kelp"),
            new NutritiousKelpBlock(
                AbstractBlock.Settings.create()
                    .mapColor(MapColor.WATER_BLUE)
                    .noCollision()
                    .ticksRandomly()
                    .breakInstantly()
                    .sounds(BlockSoundGroup.WET_GRASS)
                    .pistonBehavior(PistonBehavior.DESTROY)
            )
        );

        Registry.register(Registries.BLOCK_TYPE, "nutritious_kelp", NUTRITIOUS_KELP.getCodec());

        DRIED_ENERGY_DENSE_KELP_BLOCK = Registry.register(
            Registries.BLOCK,
            FishingClub.identifier("dried_energy_dense_kelp_block"),
            new Block(
                AbstractBlock.Settings.create()
                    .mapColor(MapColor.GREEN)
                    .strength(0.5F, 2.5F)
                    .sounds(BlockSoundGroup.GRASS)
            )
        );

        ENERGY_DENSE_KELP_PLANT = Registry.register(
            Registries.BLOCK,
            FishingClub.identifier("energy_dense_kelp_plant"),
            new EnergyDenseKelpPlantBlock(
                AbstractBlock.Settings.create()
                    .mapColor(MapColor.WATER_BLUE)
                    .noCollision()
                    .breakInstantly()
                    .sounds(BlockSoundGroup.WET_GRASS)
                    .pistonBehavior(PistonBehavior.DESTROY)
            ));
        ENERGY_DENSE_KELP = Registry.register(
            Registries.BLOCK,
            FishingClub.identifier("energy_dense_kelp"),
            new EnergyDenseKelpBlock(
                AbstractBlock.Settings.create()
                    .mapColor(MapColor.WATER_BLUE)
                    .noCollision()
                    .ticksRandomly()
                    .breakInstantly()
                    .sounds(BlockSoundGroup.WET_GRASS)
                    .pistonBehavior(PistonBehavior.DESTROY)
            )
        );

        Registry.register(Registries.BLOCK_TYPE, "energy_dense_kelp", ENERGY_DENSE_KELP.getCodec());

        REED_BLOCK =  Registry.register(
            Registries.BLOCK,
            FishingClub.identifier("reed"),
            new ReedBlock(AbstractBlock.Settings.create()
                .mapColor(MapColor.GREEN)
                .noCollision()
                .ticksRandomly()
                .breakInstantly()
                .sounds(BlockSoundGroup.GRASS)
                .pistonBehavior(PistonBehavior.DESTROY))
        );

        TACKLE_BOX_BLOCK = Registry.register(
          Registries.BLOCK,
          FishingClub.identifier("tackle_box"),
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
          FishingClub.identifier("tackle_box"),
          BlockEntityType.Builder.create(TackleBoxBlockEntity::new, Blocks.TACKLE_BOX_BLOCK).build()
        );
        FISH_DISPLAY = Registry.register(
          Registries.BLOCK_ENTITY_TYPE,
          FishingClub.identifier("fish_display_bamboo"),
          BlockEntityType.Builder.create(FishDisplayBlockEntity::new, Blocks.FISH_DISPLAY_BLOCK_BAMBOO, Blocks.FISH_DISPLAY_BLOCK_BAMBOO).build()
        );

    }
}
