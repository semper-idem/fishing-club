package net.semperidem.fishingclub.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
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

    @Override
    public ItemStack getDefaultStack() {
        ItemStack defaultStack = new ItemStack(this);
        defaultStack.getNbt().putString("key", this.key);
        return defaultStack;
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
        CORE(1),
        BOBBER(2),
        LINE(3),
        HOOK(4),
        BAIT(5);

        public int slotIndex;

        PartType(int slotIndex) {
            this.slotIndex = slotIndex;
        }
     }
}
