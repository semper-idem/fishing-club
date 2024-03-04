package net.semperidem.fishingclub.mixin.common;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Arm;
import net.minecraft.world.World;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.fisher.FishingPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin extends LivingEntity implements FishingPlayerEntity {
    @Unique
    public FishingCard fishingCard = new FishingCard((PlayerEntity) (Object)this);
    @Unique
    private static final String FISHING_CARD_TAG = "fishing_card";


    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
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
