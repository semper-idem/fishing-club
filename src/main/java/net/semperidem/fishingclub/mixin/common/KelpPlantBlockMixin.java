package net.semperidem.fishingclub.mixin.common;

import net.minecraft.block.KelpPlantBlock;
import net.minecraft.util.shape.VoxelShape;
import net.semperidem.fishingclub.block.EnergyDenseKelpPlantBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(KelpPlantBlock.class)
public class KelpPlantBlockMixin{

	@Redirect(method = "<init>", at = @At(
		value = "INVOKE",
		target = "Lnet/minecraft/util/shape/VoxelShapes;fullCube()Lnet/minecraft/util/shape/VoxelShape;"
	))
	private static VoxelShape changeShape(){

		return EnergyDenseKelpPlantBlock.SHAPE;
	}

}
