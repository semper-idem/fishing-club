package net.semperidem.fishing_club.item;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BundleContentsComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import net.semperidem.fishing_club.registry.FCComponents;

import java.util.List;

public class DoubleFishingNetItem extends FishingNetItem {
    public DoubleFishingNetItem(Settings settings) {
        super(settings);
        this.size = 54;
    }

    @Override
    public FishingNetContentComponent getContent(ItemStack stack) {
        return stack.getOrDefault(FCComponents.DOUBLE_FISHING_NET_CONTENT, FishingNetContentComponent.DOUBLE);
    }

    @Override
    public void setContent(ItemStack stack, FishingNetContentComponent bundleContentsComponent) {
        stack.set(FCComponents.DOUBLE_FISHING_NET_CONTENT, bundleContentsComponent);
    }

}
