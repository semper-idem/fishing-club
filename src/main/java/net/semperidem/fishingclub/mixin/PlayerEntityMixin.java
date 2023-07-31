package net.semperidem.fishingclub.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.PlayerSpawnS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.fisher.FisherInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin extends Entity {
    @Unique private FisherInfo fisherInfo = new FisherInfo();


    @Inject(method = "initDataTracker", at = @At("TAIL"))
    private void onInitDataTracker(CallbackInfo ci){
        dataTracker.startTracking(FishingClub.FISHER_NBT, new FisherInfo((PlayerEntity) (Object)this).getNbt());
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void readCustomDataFromNbt(NbtCompound nbtCompound, CallbackInfo ci){
        if (nbtCompound.contains("fisher_info")) {
            dataTracker.set(FishingClub.FISHER_NBT, nbtCompound.getCompound("fisher_info"));
        }
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void writeCustomDataToNbt(NbtCompound nbtCompound, CallbackInfo ci){
        NbtCompound fisherTag = dataTracker.get(FishingClub.FISHER_NBT);
        nbtCompound.put("fisher_info", fisherTag);
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
