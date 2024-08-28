package net.semperidem.fishingclub.mixin.common;

import net.minecraft.item.FishingRodItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.semperidem.fishingclub.item.fishing_rod.MemberFishingRodItem;
import net.semperidem.fishingclub.item.fishing_rod.components.RodConfiguration;
import net.semperidem.fishingclub.registry.FCComponents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(Items.class)
public class ItemsMixin {

    @Redirect(
            method = "<clinit>",
            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=fishing_rod")),
            at = @At(value = "NEW", target = "(Lnet/minecraft/item/Item$Settings;)Lnet/minecraft/item/FishingRodItem;", ordinal = 0)
    )
    private static FishingRodItem fishing_club$fishingRod(Item.Settings settings) {
        return new MemberFishingRodItem(settings.maxDamage(100).component(FCComponents.ROD_CONFIGURATION, RodConfiguration.getDefault()));
    }

}
