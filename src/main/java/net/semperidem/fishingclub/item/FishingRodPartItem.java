package net.semperidem.fishingclub.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.semperidem.fishingclub.client.game.FishGameLogic;

import java.util.HashMap;

public class FishingRodPartItem extends Item {
    private HashMap<FishGameLogic.Stat, Float> stats;
    private PartType partType;
    private String key;
    public FishingRodPartItem(Settings settings, PartType partType, String key) {
        super(settings.maxDamage(1).group(ItemGroup.MISC));
        this.partType = partType;
        this.stats = new HashMap<>();
        FishingRodPartItems.KEY_TO_PART_MAP.put(key, this);
        this.key = key;

    }

    public PartType getPartType() {
        return partType;
    }

    public HashMap<FishGameLogic.Stat, Float> getStatBonuses(){
        return stats;
    }

    public String getKey(){
        return key;
    }

    public FishingRodPartItem withStat(FishGameLogic.Stat stat, Float value){
        this.stats.put(stat, value);
        return this;
    }


    public enum PartType {
        BOBBER,
        CORE,
        LINE,
        HOOK,
        BAIT
    }
}
