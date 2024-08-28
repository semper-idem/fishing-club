package net.semperidem.fishingclub.mixin.client.renderer;

import net.minecraft.client.render.entity.FishingBobberEntityRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.semperidem.fishingclub.registry.FCItems;
import net.semperidem.fishingclub.registry.FCTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FishingBobberEntityRenderer.class)
public class FishingBobberEntityRendererMixin {


    @Redirect(method = "getHandPos", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"))
    private boolean fishing_club$isOf(ItemStack instance, Item item) {
        return instance.isOf(item) || instance.isIn(FCTags.ROD_CORE);
    }

}
