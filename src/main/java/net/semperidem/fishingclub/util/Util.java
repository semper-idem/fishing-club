package net.semperidem.fishingclub.util;

import net.minecraft.entity.Entity;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class Util {

    public static  String getVectorString(Vec3d vector) {
        return String.format("%5.2f", vector.x) + String.format("%5.2f", vector.y) + String.format("%5.2f", vector.z);
    }

    public static double getEntityDepth(Entity entity) {
        double nextYPos = entity.getPos().y + entity.getVelocity().y;
        int nextYBlockPos = (int) Math.floor(nextYPos);
        BlockPos pos = new BlockPos(entity.getBlockX(), nextYPos, entity.getBlockZ());
        FluidState fluidState = entity.world.getFluidState(pos);
        FluidState fluidAbove = entity.world.getFluidState(pos.up());
        float waterLevel = (fluidAbove.isOf(fluidState.getFluid()) ? 1 : fluidState.getHeight());
        float waterLevelAbove = fluidAbove.getHeight();
        double depth = (nextYPos - nextYBlockPos) - waterLevel - waterLevelAbove;
        return MathHelper.clamp(depth, -2f, 0f);
    }
}
