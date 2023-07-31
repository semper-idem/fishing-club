package net.semperidem.fishingclub.client.game.treasure;

import net.minecraft.item.Item;

public class TreasureReward {
    int tier;
    int cost;
    Range qualityRange;
    Item item;






    class Range{
        float bottom;
        float top;

        Range(float bottom, float top){
            if (bottom > top) {
                this.bottom = top;
                this.top = bottom;
            } else {
                this.bottom = bottom;
                this.top = top;
            }
        }
    }
}
