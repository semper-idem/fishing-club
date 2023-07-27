package net.semperidem.fishingclub.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.PlayerSpawnS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import net.semperidem.fishingclub.fisher.FisherInfo;
import net.semperidem.fishingclub.network.ServerPacketSender;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin extends Entity {
    @Unique private FisherInfo fisherInfo = new FisherInfo();

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void readCustomDataFromNbt(NbtCompound nbtCompound, CallbackInfo ci){
        fisherInfo = new FisherInfo((PlayerEntity)(Object)this, nbtCompound);
        if (this.world.isClient()) return;
        ServerPacketSender.sendFisherInfoSync((ServerPlayerEntity) (Object)this, nbtCompound);
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void writeCustomDataToNbt(NbtCompound nbtCompound, CallbackInfo ci){
        fisherInfo.writeNbt(nbtCompound);
        if (this.world.isClient()) return;
        ServerPacketSender.sendFisherInfoSync((ServerPlayerEntity) (Object)this, nbtCompound);
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
