package net.semperidem.fishing_club.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;
import net.semperidem.fishing_club.registry.FCBlocks;

public class ReedFeature extends Feature<DefaultFeatureConfig> {

	public ReedFeature(Codec<DefaultFeatureConfig> configCodec) {
		super(configCodec);
	}

	@Override
	public boolean generate(FeatureContext<DefaultFeatureConfig> context) {

		int i = 0;
		StructureWorldAccess structureWorldAccess = context.getWorld();
		BlockPos blockPos = context.getOrigin();
		Random random = context.getRandom();
		int j = structureWorldAccess.getTopY(Heightmap.Type.OCEAN_FLOOR, blockPos.getX(), blockPos.getZ());
		BlockPos blockPos2 = new BlockPos(blockPos.getX(), j, blockPos.getZ());
		if (structureWorldAccess.getBlockState(blockPos2).isOf(Blocks.WATER)) {
			BlockState blockState = FCBlocks.REED_BLOCK.getDefaultState();
			int k = 1 + random.nextInt(2);

			for (int l = 0; l < k; l++) {
				if (!blockState.canPlaceAt(structureWorldAccess, blockPos2)) {
					break;
				}
				structureWorldAccess.setBlockState(blockPos2, blockState, 2);
				blockPos2 = blockPos2.up();
				i++;
			}
		}

		return i > 0;

	}

}
