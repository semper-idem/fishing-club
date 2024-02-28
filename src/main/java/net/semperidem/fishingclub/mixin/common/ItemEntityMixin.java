package net.semperidem.fishingclub.mixin.common;


import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import net.semperidem.fishingclub.entity.FishermanEntity;
import net.semperidem.fishingclub.fish.FishUtil;
import net.semperidem.fishingclub.registry.ItemRegistry;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity{
    @Unique
    UUID summonerUUID;

    public ItemEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow public abstract ItemStack getStack();

    @Shadow private int itemAge;

    @Shadow private @Nullable UUID owner;

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        if (!(getStack().isOf(ItemRegistry.GOLD_FISH) || getStack().isOf(FishUtil.FISH_ITEM))) {
            return;
        }

        if (summonerUUID == null) {
            setSummoner();
        }


        if (itemAge < 20) {
            return;
        }
        FishermanEntity derek = new FishermanEntity(this.world, getStack(), summonerUUID);
        derek.setPosition(this.getPos());
        world.spawnEntity(derek);
        if (!(world instanceof ServerWorld serverWorld)) {
            return;
        }
        serverWorld.spawnParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, getX(), getY() + 1, getZ(), 100,1,1,1,0.1);
        serverWorld.spawnParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, getX(), getY() + 1, getZ(), 100,1,1,1,0.1);
        serverWorld.spawnParticles(ParticleTypes.EXPLOSION, getX(), getY() + 2, getZ(), 50,0.5,0.5,0.5,0.1);
        serverWorld.playSound(null, getX(), getY(), getZ(), SoundEvents.ITEM_BUCKET_FILL_FISH, SoundCategory.PLAYERS, 0.3f, 0.2f, 0L);
        discard();
    }

    @Unique
    private void setSummoner() {
        summonerUUID = FishUtil.getSummonerUUID(getStack());
        if (summonerUUID == null) {
            summonerUUID = owner;
        }

    }
}
