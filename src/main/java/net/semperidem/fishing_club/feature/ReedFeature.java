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
import net.semperidem.fishing_club.block.ReedBlock;
import net.semperidem.fishing_club.registry.FCBlocks;

public class ReedFeature extends Feature<DefaultFeatureConfig> {

	public ReedFeature(Codec<DefaultFeatureConfig> configCodec) {
		super(configCodec);
	}

	@Override
	public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
		StructureWorldAccess structureWorldAccess = context.getWorld();
		BlockPos blockPos = context.getOrigin();
		Random random = context.getRandom();
		int j = structureWorldAccess.getTopY(Heightmap.Type.OCEAN_FLOOR, blockPos.getX(), blockPos.getZ());//todo move to configured feature json
		BlockPos posWater = new BlockPos(blockPos.getX(), j, blockPos.getZ());
		BlockPos posAbove = posWater.up();
		BlockState blockState = FCBlocks.REED_BLOCK.getDefaultState().with(ReedBlock.WATERLOGGED, true);
		if (!blockState.canPlaceAt(structureWorldAccess, posWater)) {
			return false;
		}
		if (!structureWorldAccess.getBlockState(posWater).isOf(Blocks.WATER)) {
			return false;
		}
		if (!(structureWorldAccess.getBlockState(posAbove).isAir())) {
			return false;
		}
		structureWorldAccess.setBlockState(posWater, blockState, 2);
		if (random.nextInt(2) > 0) {
			structureWorldAccess.setBlockState(posAbove, blockState.with(ReedBlock.WATERLOGGED, false), 2);
		}
		return true;
	}
}
