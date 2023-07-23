package net.semperidem.fishingclub.mixin;


import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import net.semperidem.fishingclub.FishingClub;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    private static FishingRodItem ROD = FishingClub.CUSTOM_FISHING_ROD;
    private int lastPower = 0;

    public LivingEntityMixin(EntityType<?> type, World world) {
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

    @Shadow
    public Packet<?> createSpawnPacket() {
        return null;
    }

    @Shadow public abstract boolean isUsingItem();

    @Shadow public abstract int getItemUseTime();

    @Shadow public abstract ItemStack getMainHandStack();

    @Shadow public abstract ItemStack getOffHandStack();

    @Inject(method = "baseTick", at = @At("TAIL"))
    private void onBaseTick(CallbackInfo ci){
        if (!this.world.isClient) {
            return;
        }
        if (!this.isUsingItem()) {
           return;
        }
        if (this.getItemUseTime() <= 0) {
            return;
        }
        ItemStack mainHandStack = getMainHandStack();
        ItemStack offHandStack = getOffHandStack();
        if (mainHandStack.isOf(ROD) || offHandStack.isOf(ROD)) {
            int power = Math.max(1, Math.min(5, (getItemUseTime() + 60) / 60));
            if (power != lastPower) {
                lastPower = power;
                MinecraftClient.getInstance().player.sendMessage(Text.of("[" + ">".repeat(power) + "]"), true);
            }
        }
    }

}
