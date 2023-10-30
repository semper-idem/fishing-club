package net.semperidem.fishingclub.item.fishing_rod;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.semperidem.fishingclub.registry.FItemRegistry;

import java.util.HashMap;

public class FishingRodPartItem extends Item {
    private HashMap<FishingRodStat, Float> stats;
    private FishingRodPartType partType;
    private String key;
    public FishingRodPartItem(Settings settings, FishingRodPartType partType, String key) {
        super(settings.maxDamageIfAbsent(1).group(FItemRegistry.FISHING_CLUB_GROUP));
        this.partType = partType;
        this.stats = new HashMap<>();
        FishingRodPartItems.KEY_TO_PART_MAP.put(key, this);
        this.key = key;

    }

    @Override
    public ItemStack getDefaultStack() {
        ItemStack defaultStack = new ItemStack(this);
        defaultStack.getNbt().putString("key", this.key);
        return defaultStack;
    }
    public FishingRodPartType getPartType() {
        return partType;
    }

    public HashMap<FishingRodStat, Float> getStatBonuses(){
        return stats;
    }

    public String getKey(){
        return key;
    }

    public FishingRodPartItem withStat(FishingRodStat stat, Float value){
        this.stats.put(stat, value);
        return this;
    }


}
