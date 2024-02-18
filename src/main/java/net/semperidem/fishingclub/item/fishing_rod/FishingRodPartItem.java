package net.semperidem.fishingclub.item.fishing_rod;

import net.minecraft.item.Item;
import net.semperidem.fishingclub.registry.ItemRegistry;

import java.util.Arrays;
import java.util.List;

public class FishingRodPartItem extends Item {
    private final List<FishingRodStat> stats;
    private final FishingRodPartType partType;

    public FishingRodPartItem(Settings settings, FishingRodPartType partType, FishingRodStat...stats) {
        super(settings.maxDamageIfAbsent(1).group(ItemRegistry.FISHING_CLUB_GROUP));
        this.partType = partType;
        this.stats = Arrays.asList(stats);
    }

    public FishingRodPartType getPartType() {
        return partType;
    }

    public float getStat(FishingRodStatType stat){
        return stats.stream().filter(o -> o.statType == stat).findFirst().map(fishingRodStat -> fishingRodStat.value).orElse(0F);
    }



}
