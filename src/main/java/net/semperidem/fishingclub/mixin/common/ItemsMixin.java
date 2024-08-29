package net.semperidem.fishingclub.mixin.common;

import net.minecraft.item.FishingRodItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.semperidem.fishingclub.item.fishing_rod.components.FishingRodCoreItem;
import net.semperidem.fishingclub.item.fishing_rod.components.RodConfiguration;
import net.semperidem.fishingclub.registry.FCComponents;
import net.semperidem.fishingclub.registry.FCItems;
import net.semperidem.fishingclub.registry.FCRegistry;
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
        //Need to register before Rod so we can use it
        FCRegistry.registerPreFishingRod();
        RodConfiguration defaultRod = RodConfiguration.EMPTY.equip(FCItems.LINE_SPIDER.getDefaultStack());
        return new FishingRodCoreItem(settings.maxDamage(FCItems.VANILLA_ROD_DURABILITY)
                .component(FCComponents.ROD_CONFIGURATION, defaultRod))
                .weightClass(2);//need to register parts before
    }

}
