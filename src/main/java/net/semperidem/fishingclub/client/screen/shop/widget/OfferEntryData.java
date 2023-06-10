package net.semperidem.fishingclub.client.screen.shop.widget;

import net.minecraft.item.Item;

public class OfferEntryData {
    Item offerItem;
    int basePrice;
    int batchSize;
    float discountIncrement;
    float discountMinimum;
    int lastPrice;

    public OfferEntryData(Item offerItem, int basePrice, int batchSize, float discountIncrement, float discountMinimum) {
        this.offerItem = offerItem;
        this.basePrice = basePrice;
        this.batchSize = Math.max(1,batchSize);
        this.discountIncrement = discountIncrement;
        this.discountMinimum = discountMinimum;
        this.lastPrice = basePrice;
    }

    public int getPriceForCount(int count){
        int total = 0;
        for(int i = 1; i <= count; i++) {
            total += getPriceForNth(i);
        }
        return total;
    }

    public int getPriceForNth(int n){
        float actualDiscount = Math.max(discountMinimum, 1 - discountIncrement * (n / 4));
        return (int)(basePrice * actualDiscount);
    }

    public String getName(){
        return offerItem.getName().getString();
    }
    public int getPrice(){
        return basePrice;
    }
}
