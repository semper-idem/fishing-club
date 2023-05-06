package net.semperidem.fishingclub.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.semperidem.fishingclub.FishingClub;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {



    @Inject(method = "createPlayerAttributes", at = @At("RETURN"), cancellable = true)
    private static void createLivingAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> ci) {
        DefaultAttributeContainer.Builder builder = ci.getReturnValue();
        ci.setReturnValue(builder.add(FishingClub.FISHING_LEVEL));
        ci.setReturnValue(builder.add(FishingClub.FISHING_EXP));
    }

}
