package net.semperidem.fishingclub.mixin.common;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Arm;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.semperidem.fishingclub.FishingLevelProperties;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.fisher.FishingPlayerEntity;
import net.semperidem.fishingclub.fisher.perks.FishingPerks;
import net.semperidem.fishingclub.registry.StatusEffectRegistry;
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
    @Unique
    public FishingCard fishingCard = new FishingCard((PlayerEntity) (Object)this);
    @Unique
    private static final String FISHING_CARD_TAG = "fishing_card";


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
        //Add aoe effect
        Box aoeBox = new Box(getBlockPos());
        aoeBox.expand(5);
        List<ServerPlayerEntity> nearPlayers = getEntityWorld()
                .getOtherEntities(null, aoeBox)
                .stream()
                .filter(ServerPlayerEntity.class::isInstance)
                .map(ServerPlayerEntity.class::cast)
                .toList();

        for(ServerPlayerEntity otherPlayer : nearPlayers) {
            otherPlayer.addStatusEffect(new StatusEffectInstance(StatusEffectRegistry.EXP_BUFF, 200));
            otherPlayer.addStatusEffect(new StatusEffectInstance(StatusEffectRegistry.QUALITY_BUFF, 200));
            otherPlayer.addStatusEffect(new StatusEffectInstance(StatusEffectRegistry.BOBBER_BUFF, 200));
            otherPlayer.addStatusEffect(new StatusEffectInstance(StatusEffectRegistry.FREQUENCY_BUFF, 200));
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
