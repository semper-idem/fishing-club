package net.semperidem.fishing_club.mixin.common;


import net.minecraft.block.KelpBlock;
import net.minecraft.util.shape.VoxelShape;
import net.semperidem.fishing_club.block.EnergyDenseKelpBlock;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(KelpBlock.class)
public class KelpBlockMixin {
	@Final @Shadow protected final static VoxelShape SHAPE = EnergyDenseKelpBlock.SHAPE;

}
