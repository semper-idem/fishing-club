package net.semperidem.fishing_club.item;


import net.minecraft.component.type.BundleContentsComponent;
import net.minecraft.item.tooltip.TooltipData;

public record FishingNetTooltipData(FishingNetContentComponent contents) implements TooltipData {
    public FishingNetTooltipData(FishingNetContentComponent contents) {
        this.contents = contents;
    }

    public FishingNetContentComponent contents() {
        return this.contents;
    }
}