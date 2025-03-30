package net.semperidem.fishingclub.mixin.common;

import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.Block;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.Item;
import net.semperidem.fishingclub.item.fishing_rod.components.FishingRodCoreItem;
import net.semperidem.fishingclub.item.fishing_rod.components.RodConfiguration;
import net.semperidem.fishingclub.registry.Components;
import net.semperidem.fishingclub.registry.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;


@Mixin(net.minecraft.item.Items.class)
public class ItemsMixin {

//    @WrapOperation(
//            method = "<clinit>",
//            at = @At(value = "INVOKE", target = "FishingRodItem.<init>")
//    )
//    private static FishingRodItem fishing_club$fishingRod(Item.Settings settings) {
//        //Need to register before Rod so we can use it
//        Components.registerRodConfigurationEarly();
//        Items.registerLineEarly();
//        RodConfiguration defaultRod = RodConfiguration.EMPTY.equip(Items.LINE_SPIDER.getDefaultStack());
//        return new FishingRodCoreItem(settings.maxDamage(Items.VANILLA_ROD_DURABILITY)
//                .component(Components.ROD_CONFIGURATION, defaultRod))
//                .weightClass(2);//need to register parts before
//    }

}
