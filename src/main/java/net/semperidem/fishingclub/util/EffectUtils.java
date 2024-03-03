package net.semperidem.fishingclub.util;

import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.semperidem.fishingclub.entity.FishermanEntity;

import static net.minecraft.particle.ParticleTypes.*;

public class EffectUtils {

    public static void onDerekSummonEffect(ServerWorld serverWorld, FishermanEntity fishermanEntity) {
        Vec3d deltaPosition = new Vec3d(1,1,1);
        Vec3d pos = fishermanEntity.getPos().add(0,1,0);
        spawnParticle(serverWorld, CAMPFIRE_COSY_SMOKE, pos, deltaPosition, 0.01, 50);
        spawnParticle(serverWorld, CAMPFIRE_SIGNAL_SMOKE, pos, deltaPosition, 0.02, 50);
        spawnParticle(serverWorld, CAMPFIRE_COSY_SMOKE, pos, deltaPosition, 0.03, 50);
        spawnParticle(serverWorld, CAMPFIRE_SIGNAL_SMOKE, pos, deltaPosition, 0.01, 50);
        spawnParticle(serverWorld, EXPLOSION, pos, deltaPosition, 0.1, 10);
        serverWorld.playSoundFromEntity(null, fishermanEntity, SoundEvents.ITEM_BUCKET_FILL_FISH, SoundCategory.PLAYERS, 0.3f, 0.2f, 0L);
    }

    public static void spawnParticle(ServerWorld serverWorld, ParticleEffect particle, Vec3d position, Vec3d deltaPosition, double speed, int count){
        serverWorld.spawnParticles(
                particle,
                position.x,
                position.y,
                position.z,
                count,
                deltaPosition.x,
                deltaPosition.y,
                deltaPosition.z,
                speed
        );
    }
}
