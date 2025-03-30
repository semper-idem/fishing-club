package net.semperidem.fishingclub.util;

import net.minecraft.particle.ParticleEffect;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.semperidem.fishingclub.entity.FishermanEntity;

import static net.minecraft.particle.ParticleTypes.*;

public class EffectUtils {

  public static void onDerekSummonEffect(ServerWorld serverWorld, FishermanEntity fishermanEntity) {
    Vec3d deltaPosition = new Vec3d(1, 1, 1);
    Vec3d pos = fishermanEntity.getPos().add(0, 1, 0);
    smokeBombEffect(serverWorld, pos, deltaPosition);
    serverWorld.playSound(
        null,
        pos.x,
        pos.y,
        pos.z,
        SoundEvents.ENTITY_ENDERMAN_TELEPORT,
        SoundCategory.PLAYERS,
        0.5f,
        1f,
        0L);
    serverWorld.playSoundFromEntity(
        null,
        fishermanEntity,
        serverWorld
            .getRegistryManager()
            .getOrThrow(RegistryKeys.SOUND_EVENT)
            .getEntry(SoundEvents.ITEM_BUCKET_FILL_FISH),
        SoundCategory.PLAYERS,
        1f,
        1f,
        0L);
  }

  public static void onDerekDisappearEffect(
      ServerWorld serverWorld, FishermanEntity fishermanEntity) {
    Vec3d deltaPosition = new Vec3d(1, 1, 1);
    Vec3d pos = fishermanEntity.getPos().add(0, 1, 0);
    smokeBombEffect(serverWorld, pos, deltaPosition);
    serverWorld.playSound(
        null,
        pos.x,
        pos.y,
        pos.z,
        SoundEvents.ENTITY_ENDERMAN_TELEPORT,
        SoundCategory.PLAYERS,
        1f,
        1f,
        0L);
    serverWorld.playSound(
        null,
        pos.x,
        pos.y,
        pos.z,
        SoundEvents.ENTITY_GENERIC_EXPLODE,
        SoundCategory.PLAYERS,
        0.3f,
        1f,
        0L);
  }

  private static void smokeBombEffect(
      ServerWorld serverWorld, Vec3d position, Vec3d deltaPosition) {
    spawnParticle(serverWorld, CAMPFIRE_COSY_SMOKE, position, deltaPosition, 0.01, 50);
    spawnParticle(serverWorld, CAMPFIRE_SIGNAL_SMOKE, position, deltaPosition, 0.02, 50);
    spawnParticle(serverWorld, CAMPFIRE_COSY_SMOKE, position, deltaPosition, 0.03, 50);
    spawnParticle(serverWorld, CAMPFIRE_SIGNAL_SMOKE, position, deltaPosition, 0.01, 50);
    spawnParticle(serverWorld, EXPLOSION, position, deltaPosition, 0.1, 10);
  }

  public static void spawnParticle(
      ServerWorld serverWorld,
      ParticleEffect particle,
      Vec3d position,
      Vec3d deltaPosition,
      double speed,
      int count) {
    serverWorld.spawnParticles(
        particle,
        position.x,
        position.y,
        position.z,
        count,
        deltaPosition.x,
        deltaPosition.y,
        deltaPosition.z,
        speed);
  }
}
