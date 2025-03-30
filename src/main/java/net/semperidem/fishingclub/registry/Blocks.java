package net.semperidem.fishingclub.registry;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.WoodType;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.block.*;
import net.semperidem.fishingclub.entity.FishDisplayBlockEntity;
import net.semperidem.fishingclub.entity.TackleBoxBlockEntity;

import java.util.Set;
import java.util.function.Function;

import static net.minecraft.block.Blocks.BAMBOO_PLANKS;

public class Blocks {
    public static Block TACKLE_BOX_BLOCK;
    public static Block FISH_DISPLAY_BLOCK_BAMBOO;
    public static Block REED_BLOCK;
    public static Block ENERGY_DENSE_KELP;
    public static Block ENERGY_DENSE_KELP_PLANT;
    public static Block DRIED_ENERGY_DENSE_KELP_BLOCK;
    public static Block NUTRITIOUS_KELP;
    public static Block NUTRITIOUS_KELP_PLANT;
    public static Block DRIED_NUTRITIOUS_KELP_BLOCK;
    public static Block WATERLOGGED_LILY_PAD_BLOCK;
    public static Block DUCKWEED_BLOCK;
    public static BlockEntityType<FishDisplayBlockEntity> FISH_DISPLAY;
    public static BlockEntityType<TackleBoxBlockEntity> TACKLE_BOX;

    private static Block registerFishDisplayBlock(WoodType woodType, MapColor mapColor) {
        return register(
          "fish_display_" + woodType.name().toLowerCase(),
                (settings) -> {
              return new FishDisplayBlock(woodType, settings);
                },
            AbstractBlock.Settings.create()
              .mapColor(mapColor)
              .solid()
              .instrument(NoteBlockInstrument.BASS)
              .noCollision()
              .strength(1.0F)
              .burnable()
          );
    }


    private static Block register(RegistryKey<Block> key, Function<AbstractBlock.Settings, Block> factory, AbstractBlock.Settings settings) {
        Block block = factory.apply(settings.registryKey(key));
        return Registry.register(Registries.BLOCK, key, block);
    }

    private static RegistryKey<Block> keyOf(String id) {
        return RegistryKey.of(RegistryKeys.BLOCK, FishingClub.identifier(id));
    }

    private static Block register(String id, Function<AbstractBlock.Settings, Block> factory, AbstractBlock.Settings settings) {
        return register(keyOf(id), factory, settings);
    }

    public static void register() {
        DUCKWEED_BLOCK = register(
            "duckweed",
            DuckweedBlock::new,
                AbstractBlock.Settings.create()
                        .mapColor(MapColor.DARK_GREEN)
                        .noCollision()
                        .ticksRandomly()
                        .sounds(BlockSoundGroup.LEAF_LITTER)
                        .pistonBehavior(PistonBehavior.DESTROY)
        );


        WATERLOGGED_LILY_PAD_BLOCK = register(
            "waterlogged_lily_pad",
            WaterloggedLilyPadBlock::new,
                AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_GREEN)
                    .breakInstantly()
                    .sounds(BlockSoundGroup.LILY_PAD)
                    .nonOpaque()
                    .pistonBehavior(PistonBehavior.DESTROY)
        );


        DRIED_NUTRITIOUS_KELP_BLOCK = register(
            "dried_nutritious_kelp_block",
                Block::new,
                AbstractBlock.Settings.create()
                    .mapColor(MapColor.GREEN)
                    .strength(0.5F, 2.5F)
                    .sounds(BlockSoundGroup.GRASS)
        );

        NUTRITIOUS_KELP_PLANT = register(
            "nutritious_kelp_plant",
            NutritiousKelpPlantBlock::new,
                AbstractBlock.Settings.create()
                    .mapColor(MapColor.WATER_BLUE)
                    .noCollision()
                    .breakInstantly()
                    .sounds(BlockSoundGroup.WET_GRASS)
                    .pistonBehavior(PistonBehavior.DESTROY)
        );
        NUTRITIOUS_KELP = register(
            "nutritious_kelp",
            NutritiousKelpBlock::new,
                AbstractBlock.Settings.create()
                    .mapColor(MapColor.WATER_BLUE)
                    .noCollision()
                    .ticksRandomly()
                    .breakInstantly()
                    .sounds(BlockSoundGroup.WET_GRASS)
                    .pistonBehavior(PistonBehavior.DESTROY)
        );

        Registry.register(Registries.BLOCK_TYPE, "nutritious_kelp", NutritiousKelpBlock.CODEC);

        DRIED_ENERGY_DENSE_KELP_BLOCK = register(
            "dried_energy_dense_kelp_block",
                Block::new,
                AbstractBlock.Settings.create()
                    .mapColor(MapColor.GREEN)
                    .strength(0.5F, 2.5F)
                    .sounds(BlockSoundGroup.GRASS)
        );

        ENERGY_DENSE_KELP_PLANT = register(
            "energy_dense_kelp_plant",
            EnergyDenseKelpPlantBlock::new,
                AbstractBlock.Settings.create()
                    .mapColor(MapColor.WATER_BLUE)
                    .noCollision()
                    .breakInstantly()
                    .sounds(BlockSoundGroup.WET_GRASS)
                    .pistonBehavior(PistonBehavior.DESTROY)
            );
        ENERGY_DENSE_KELP = register(
            "energy_dense_kelp",
            EnergyDenseKelpBlock::new,
                AbstractBlock.Settings.create()
                    .mapColor(MapColor.WATER_BLUE)
                    .noCollision()
                    .ticksRandomly()
                    .breakInstantly()
                    .sounds(BlockSoundGroup.WET_GRASS)
                    .pistonBehavior(PistonBehavior.DESTROY)
        );

        Registry.register(Registries.BLOCK_TYPE, "energy_dense_kelp", EnergyDenseKelpPlantBlock.CODEC);

        REED_BLOCK =  register(
            "reed",
            ReedBlock::new,
                (AbstractBlock.Settings.create()
                .mapColor(MapColor.GREEN)
                .noCollision()
                .ticksRandomly()
                .breakInstantly()
                .sounds(BlockSoundGroup.GRASS)
                .pistonBehavior(PistonBehavior.DESTROY))
        );

        TACKLE_BOX_BLOCK = register(
          "tackle_box",
          TackleBoxBlock::new,
                  AbstractBlock.Settings.create()
            .mapColor(MapColor.CYAN)
            .solid()
            .strength(2.0F)
            .dynamicBounds()
            .nonOpaque()
            .pistonBehavior(PistonBehavior.DESTROY)
        );

        FISH_DISPLAY_BLOCK_BAMBOO = registerFishDisplayBlock(WoodType.BAMBOO, BAMBOO_PLANKS.getDefaultMapColor());

        TACKLE_BOX = register("tackle_box", TackleBoxBlockEntity::new, Blocks.TACKLE_BOX_BLOCK);
        FISH_DISPLAY = register("fish_display_bamboo",FishDisplayBlockEntity::new, Blocks.TACKLE_BOX_BLOCK);

    }

    private static <T extends BlockEntity> BlockEntityType<T> register(
            String name,
            FabricBlockEntityTypeBuilder.Factory<? extends T> entityFactory,
            Block... blocks
    ) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, FishingClub.identifier(name), FabricBlockEntityTypeBuilder.<T>create(entityFactory, blocks).build());
    }
}
