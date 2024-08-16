package net.semperidem.fishingclub.fisher.level_reward;

import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

public interface LevelUpEffect {
    void execute(ServerWorld serverWorld, double x, double y, double z);

    LevelUpEffect COMMON_EFFECT = (world, x, y, z) -> {
        world.spawnParticles(ParticleTypes.FIREWORK, x, y + 1.5, z, 25,0,0,0,0.1);
        world.spawnParticles(ParticleTypes.SCRAPE, x, y + 1.5, z, 25,0.5,0.5,0.5,0.1);
        world.playSound(null, x, y, z, SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 0.25f, 0.7f, 0L);
    };

    LevelUpEffect UNCOMMON_EFFECT = (world, x, y, z) -> {
        world.spawnParticles(ParticleTypes.DRAGON_BREATH, x, y + 2, z, 100,0,0,0,0.1);
        world.spawnParticles(ParticleTypes.FIREWORK, x, y + 2, z, 50,0,0,0,0.1);
        world.spawnParticles(ParticleTypes.SCRAPE, x, y + 2, z, 50,0.5,0.5,0.5,0.1);
        world.playSound(null, x, y, z, SoundEvents.ENTITY_FIREWORK_ROCKET_BLAST, SoundCategory.PLAYERS, 0.3f, 0.2f, 0L);
    };

    LevelUpEffect RARE_EFFECT = (world, x, y, z) -> {
        world.spawnParticles(ParticleTypes.DRAGON_BREATH, x, y + 2.5, z, 150,0,0,0,0.1);
        world.spawnParticles(ParticleTypes.SOUL_FIRE_FLAME, x, y + 2.5, z, 25,0,0,0,0.1);
        world.spawnParticles(ParticleTypes.FIREWORK, x, y + 2.5, z, 100,0,0,0,0.1);
        world.spawnParticles(ParticleTypes.SCRAPE, x, y + 2.5, z, 100,0.5,0.5,0.5,0.1);
        world.playSound(null, x, y, z, SoundEvents.ENTITY_FIREWORK_ROCKET_LARGE_BLAST, SoundCategory.PLAYERS, 0.5f, 0.2f, 0L);
    };
}
