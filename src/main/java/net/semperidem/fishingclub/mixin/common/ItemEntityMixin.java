package net.semperidem.fishingclub.mixin.common;


import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.semperidem.fishingclub.entity.FishermanEntity;
import net.semperidem.fishingclub.fish.FishComponent;
import net.semperidem.fishingclub.fish.FishUtil;
import net.semperidem.fishingclub.registry.ComponentRegistry;
import net.semperidem.fishingclub.registry.ItemRegistry;
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
    @Unique
    boolean isSummonItem;

    public ItemEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "setStack", at = @At("TAIL"))
    private void onInit(ItemStack stack, CallbackInfo ci) {
        if (stack.isOf(ItemRegistry.GOLD_FISH)) {
            summonerUUID = stack.getOrDefault(ComponentRegistry.CAUGHT_BY, UUID.randomUUID());
            isSummonItem = true;
            return;
        }

        if (!stack.isOf(FishUtil.FISH_ITEM)) {
            return;
        }
        FishComponent fish = stack.get(ComponentRegistry.FISH);
        if (fish != null && fish.quality() >= 4) {
            summonerUUID = fish.caughtByUUID();
            isSummonItem = true;
        }
    }

    @Shadow public abstract ItemStack getStack();

    @Shadow private int itemAge;

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        if (!isSummonItem) {
            return;
        }

        if (itemAge < 100) {//todo configure properly, bigger number
            return;
        }

        if (!isSubmergedInWater()) {
            return;
        }
//TODO uncomment/add biome condition
//        if (!(world.getBiome(getBlockPos()).isIn(BiomeTags.IS_OCEAN) || world.getBiome(getBlockPos()).isIn(BiomeTags.IS_RIVER))) {
//            return;
//        }

        summonDerek();
    }

    @Unique
    private void summonDerek() {
        if (!(this.getWorld() instanceof ServerWorld serverWorld)) {
            return;
        }
        FishermanEntity.summonDerek(getPos(), serverWorld, getStack(), summonerUUID);
        discard();
    }
}
