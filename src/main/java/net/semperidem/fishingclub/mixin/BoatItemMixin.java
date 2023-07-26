package net.semperidem.fishingclub.mixin;

import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.BoatItem;
import net.minecraft.item.Item;
import net.semperidem.fishingclub.FishingClub;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BoatItem.class)
public class BoatItemMixin {

    @Inject(method = "<init>", at = @At("TAIL"))
    private void onInit(boolean chest, BoatEntity.Type type, Item.Settings settings, CallbackInfo ci){
        FishingClub.BOATS.add((BoatItem)(Object)this);
    }
}
