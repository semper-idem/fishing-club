package net.semperidem.fishingclub.item;


import net.minecraft.item.tooltip.TooltipData;

public record FishingNetTooltipData(NetContentComponent contents) implements TooltipData {
    public FishingNetTooltipData(NetContentComponent contents) {
        this.contents = contents;
    }

    public NetContentComponent contents() {
        return this.contents;
    }
}