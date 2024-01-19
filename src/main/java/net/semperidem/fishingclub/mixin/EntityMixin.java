package net.semperidem.fishingclub.mixin;


import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import net.semperidem.fishingclub.FishingDatabase;
import net.semperidem.fishingclub.client.FishingClubClient;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.fisher.FishingCardSerializer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Unique private FishingCard fishingCard;
    @Shadow public abstract boolean isPlayer();
    @Shadow public World world;

    @Shadow protected UUID uuid;

    //Called when saving entity
    @Inject(method = "writeNbt", at = @At("RETURN"))
    private void onWriteNbt(NbtCompound nbt, CallbackInfoReturnable<NbtCompound> cir) {
        if (!isPlayer() || world.isClient || fishingCard == null) {
            return;
        }
        nbt.put(FishingCardSerializer.TAG, FishingCardSerializer.toNbt(fishingCard));
    }

    //Called when loading entity
    @Inject(method = "readNbt", at = @At("TAIL"))
    private void onReadNbt(NbtCompound nbt, CallbackInfo ci) {
        if (!isPlayer() || world.isClient || !nbt.contains(FishingCardSerializer.TAG)) {
            return;
        }
        fishingCard = FishingCardSerializer.fromNbt((PlayerEntity) (Object) this, nbt.getCompound(FishingCardSerializer.TAG));
        FishingDatabase.putCard(uuid, fishingCard);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void onTick(CallbackInfo ci){
        if (!isPlayer()) {
            return;
        }

        if (world.isClient) {
            fishingCard = FishingClubClient.CLIENT_INFO;
        }

        if (fishingCard != null) {
            fishingCard.tick();
        }
    }
}
