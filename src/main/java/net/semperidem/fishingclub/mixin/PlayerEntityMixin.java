package net.semperidem.fishingclub.mixin;


import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Arm;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin extends LivingEntity {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void readNbt(NbtCompound nbt){
        super.readNbt(nbt);
        System.out.println("bonus read");
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
