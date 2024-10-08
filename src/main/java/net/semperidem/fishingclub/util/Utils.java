package net.semperidem.fishingclub.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.fluid.FluidState;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Utils {

    public static  String getVectorString(Vec3d vector) {
        return String.format("%5.2f", vector.x) + String.format("%5.2f", vector.y) + String.format("%5.2f", vector.z);
    }

    public static double getEntityDepth(Entity entity) {
        double nextYPos = entity.getPos().y + entity.getVelocity().y;
        int nextYBlockPos = (int) Math.floor(nextYPos);
        BlockPos pos = new BlockPos(entity.getBlockX(), (int) nextYPos, entity.getBlockZ());
        FluidState fluidState = entity.getWorld().getFluidState(pos);
        FluidState fluidAbove = entity.getWorld().getFluidState(pos.up());
        float waterLevel = (fluidAbove.isOf(fluidState.getFluid()) ? 1 : fluidState.getHeight());
        float waterLevelAbove = fluidAbove.getHeight();
        double depth = (nextYPos - nextYBlockPos) - waterLevel - waterLevelAbove;
        return MathHelper.clamp(depth, -2f, 0f);
    }

    public static void castEffect(ServerPlayerEntity caster, StatusEffectInstance effect){
        castEffect(caster, effect, 4);
    }

    public static void castEffect(ServerPlayerEntity caster, StatusEffectInstance effect, int range){
        if (range == 0) {
            caster.addStatusEffect(effect);
            return;
        }
        Box aoe = new Box(caster.getBlockPos());
        aoe.expand(range);
        List<Entity> iterableEntities = caster.getEntityWorld().getOtherEntities(null, aoe);
        iterableEntities.add(caster);
        iterableEntities.stream().filter(o -> o instanceof ServerPlayerEntity).forEach(o -> ((ServerPlayerEntity) o).addStatusEffect(effect));
    }


    public static String epochToFormatted(long epoch) {
        return DateTimeFormatter
                .ofPattern("HH:mm, dd.MM.yyyy")
                .format(LocalDateTime.ofInstant(
                        Instant.ofEpochMilli(epoch),
                        ZoneOffset.systemDefault()
                ));
    }
}
