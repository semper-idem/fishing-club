package net.semperidem.fishingclub.feature;

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
import net.semperidem.fishingclub.registry.FCBlocks;

public class DuckweedFeature extends Feature<DefaultFeatureConfig> {

	public DuckweedFeature(Codec<DefaultFeatureConfig> configCodec) {
		super(configCodec);
	}

	@Override
	public boolean generate(FeatureContext<DefaultFeatureConfig> context) {

		int i = 0;
		StructureWorldAccess structureWorldAccess = context.getWorld();
		BlockPos blockPos = context.getOrigin();
		Random random = context.getRandom();
		int j = structureWorldAccess.getTopY(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, blockPos.getX(), blockPos.getZ());
		BlockPos blockPos2 = new BlockPos(blockPos.getX(), j, blockPos.getZ());
		if (structureWorldAccess.getBlockState(blockPos2).isOf(Blocks.WATER)) {
			BlockState blockState = FCBlocks.DUCKWEED_BLOCK.getDefaultState();

				if (!blockState.canPlaceAt(structureWorldAccess, blockPos2)) {
					return false;
				}
				structureWorldAccess.setBlockState(blockPos2, blockState, 2);
			return true;
		}

		return false;

	}

}
