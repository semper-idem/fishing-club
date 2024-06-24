package net.semperidem.fishingclub.mixin.common;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Arm;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.semperidem.fishingclub.FishingLevelProperties;
import net.semperidem.fishingclub.entity.HookEntity;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.fisher.FishingPlayerEntity;
import net.semperidem.fishingclub.registry.StatusEffectRegistry;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.UUID;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin extends LivingEntity implements FishingPlayerEntity {
    @Shadow @Nullable public FishingBobberEntity fishHook;
    @Unique
    public FishingCard fishingCard = new FishingCard((PlayerEntity) (Object)this);
    @Unique
    private static final String FISHING_CARD_TAG = "fishing_card";
    @Unique
    int refreshTimer = 0;
    @Unique
    private static final int REFRESH_RATE = 100;


    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(CallbackInfo ci) {
        if (!(world.getLevelProperties() instanceof FishingLevelProperties fishingLevelProperties)) {
            return;
        }
        UUID kingUUID = fishingLevelProperties.getFishingKingUUID();
        if (!this.uuid.equals(kingUUID)) {
            return;
        }

        if (this.refreshTimer > 0) {
            this.refreshTimer--;
            return;
        }
        this.refreshTimer = REFRESH_RATE;
        //Add aoe effect
        Box aoeBox = new Box(getBlockPos());
        aoeBox.expand(5);
        List<ServerPlayerEntity> nearPlayers = getEntityWorld()
                .getOtherEntities(this, aoeBox)
                .stream()
                .filter(ServerPlayerEntity.class::isInstance)
                .map(ServerPlayerEntity.class::cast)
                .toList();

        for(ServerPlayerEntity otherPlayer : nearPlayers) {
            otherPlayer.addStatusEffect(new StatusEffectInstance(StatusEffectRegistry.EXP_BUFF, 600));
            otherPlayer.addStatusEffect(new StatusEffectInstance(StatusEffectRegistry.QUALITY_BUFF, 600));
            otherPlayer.addStatusEffect(new StatusEffectInstance(StatusEffectRegistry.BOBBER_BUFF, 600));
            otherPlayer.addStatusEffect(new StatusEffectInstance(StatusEffectRegistry.FREQUENCY_BUFF, 600));
        }
    }


    @Inject(method = "tickMovement", at = @At("RETURN"))
    private void afterTickMovement(CallbackInfo ci) {
        if (this.fishHook instanceof HookEntity hookEntity) {
            this.onLanding();
            Vec3d lineVector = hookEntity.getPos().subtract(this.getEyePos());
            double currentLineLength = lineVector.length();
            if (currentLineLength > hookEntity.getLineLength()) {
                double tensionForce = currentLineLength / hookEntity.getLineLength() * 0.1;
                Vec3d appliedMotion = lineVector
                        .multiply(1.0 / currentLineLength)
                        .multiply(
                                tensionForce,
                                tensionForce * 1.2,
                                tensionForce
                        );
                this.addVelocity(appliedMotion.x, appliedMotion.y, appliedMotion.z);
                hookEntity.applyTensionFromOwner(appliedMotion);
            }
        }
    }

    @Override
    public void readNbt(NbtCompound nbt){
        fishingCard = new FishingCard((PlayerEntity) (Object)this, nbt.getCompound(FISHING_CARD_TAG));
        super.readNbt(nbt);
    }
    @Override
    public NbtCompound writeNbt(NbtCompound nbt){
        nbt.put(FISHING_CARD_TAG, fishingCard.toNbt());
        return super.writeNbt(nbt);
    }

    @Override
    public FishingCard getCard() {
        return fishingCard;
    }


    @Shadow
    public Iterable<ItemStack> getArmorItems() {
        return null;
    }

    @Shadow
    public ItemStack getEquippedStack(EquipmentSlot slot) {
        return null;
    }

    @Shadow
    public void equipStack(EquipmentSlot slot, ItemStack stack) {

    }

    @Shadow
    public Arm getMainArm() {
        return null;
    }

}
