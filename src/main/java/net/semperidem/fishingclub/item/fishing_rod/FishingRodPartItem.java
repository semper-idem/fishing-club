package net.semperidem.fishingclub.item.fishing_rod;

import net.minecraft.item.Item;
import net.semperidem.fishingclub.registry.FItemRegistry;

import java.util.HashMap;

public class FishingRodPartItem extends Item {
    private final HashMap<FishingRodStat, Float> stats = new HashMap<>();
    private final FishingRodPartType partType;

    public FishingRodPartItem(Settings settings, FishingRodPartType partType) {
        super(settings.maxDamageIfAbsent(1).group(FItemRegistry.FISHING_CLUB_GROUP));
        this.partType = partType;

    }

    public FishingRodPartType getPartType() {
        return partType;
    }

    public HashMap<FishingRodStat, Float> getStatBonuses(){
        return stats;
    }

    public float getStat(FishingRodStat stat){
        return stats.getOrDefault(stat, 0f);
    }

    public FishingRodPartItem withStat(FishingRodStat stat, Float value){
        this.stats.put(stat, value);
        return this;
    }


}
