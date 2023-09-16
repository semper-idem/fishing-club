package net.semperidem.fishingclub.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.PlayerSpawnS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import net.semperidem.fishingclub.client.FishingClubClient;
import net.semperidem.fishingclub.fisher.FishingCard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin extends Entity {
    @Unique
    FishingCard fishingCard;

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void readCustomDataFromNbt(NbtCompound nbtCompound, CallbackInfo ci){
        if (!nbtCompound.contains(FishingCard.TAG)) return;
        fishingCard = new FishingCard((PlayerEntity) (Object) this, nbtCompound.getCompound(FishingCard.TAG));
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void writeCustomDataToNbt(NbtCompound nbtCompound, CallbackInfo ci){
        if (fishingCard == null) return;
        nbtCompound.put(FishingCard.TAG, fishingCard.toNbt());
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void onTick(CallbackInfo ci){
        if (fishingCard == null){
            if (world.isClient) {
                fishingCard = FishingClubClient.CLIENT_INFO;
            } else {
                fishingCard = new FishingCard((PlayerEntity) (Object) this);
            }
        }
        fishingCard.tick();
    }


    public PlayerEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow
    protected void initDataTracker() {

    }

    @Shadow
    protected void readCustomDataFromNbt(NbtCompound nbt) {

    }

    @Shadow
    protected void writeCustomDataToNbt(NbtCompound nbt) {

    }

    @Override
    public Packet<?> createSpawnPacket() {
        return new PlayerSpawnS2CPacket((ServerPlayerEntity)(Object)this);
    }

}
