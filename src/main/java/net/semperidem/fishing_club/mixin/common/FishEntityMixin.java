package net.semperidem.fishing_club.mixin.common;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.passive.FishEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.semperidem.fishing_club.fish.specimen.SpecimenComponent;
import net.semperidem.fishing_club.fish.FishUtil;
import net.semperidem.fishing_club.item.FishingNetItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FishEntity.class)
public class FishEntityMixin extends Entity {

    @Inject(method = "interactMob", at = @At("HEAD"))
    private void interactMob(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        if (player.getWorld().isClient) {
            return;
        }
        ItemStack stackInHand = player.getStackInHand(hand);
        if (!(stackInHand.getItem() instanceof FishingNetItem fishingNetItem)) {
            return;
        }
        SpecimenComponent fishComponent = SpecimenComponent.of((FishEntity) (Object)this);
        if (fishComponent == null) {
            return;
        }
        ItemStack fishStack = FishUtil.getStackFromFish( fishComponent.get(), 1);
        if (fishingNetItem.insertStack(stackInHand, fishStack, player)) {
            this.discard();
        }
    }

    public FishEntityMixin(EntityType<?> type, World world) {super(type, world);}
    @Shadow protected void initDataTracker(DataTracker.Builder builder){}
    @Shadow public void readCustomDataFromNbt(NbtCompound nbt) {}
    @Shadow public void writeCustomDataToNbt(NbtCompound nbt) {}

}
