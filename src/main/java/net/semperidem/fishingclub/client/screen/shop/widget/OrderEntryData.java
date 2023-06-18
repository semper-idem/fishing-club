package net.semperidem.fishingclub.client.screen.shop.widget;

import net.minecraft.item.Item;

import java.util.Objects;

public class OrderEntryData {
    OfferEntryData offerEntryData;
    int count;
    int total;
    public OrderEntryData(OfferEntryData offerEntryData) {
        this.offerEntryData = offerEntryData;
        this.count = 1;
        this.total = offerEntryData.basePrice;
    }
    public OrderEntryData(OfferEntryData offerEntryData, int count) {
        this.offerEntryData = offerEntryData;
        this.count = count;
    }

    public int getPriceForCount(int count){
        return this.offerEntryData.getPriceForCount(count);
    }

    public String getName(){
        return offerEntryData.getName();
    }

    public Item getItem(){
        return this.offerEntryData.offerItem;//todo getter setter
    }

    public int getCount(){
        return count;
    }

    public int getQuantity(){
        return count * offerEntryData.batchSize;
    }

    public OrderEntryData add(){
        this.count++;
        total = offerEntryData.getPriceForCount(count);
        return this;
    }
    public OrderEntryData remove(){
        this.count--;
        total = offerEntryData.getPriceForCount(count);
        return this;
    }

    public boolean matches(OrderEntryData orderEntryData){
        return Objects.equals(orderEntryData.getName(), this.getName()) && orderEntryData.offerEntryData.batchSize == this.offerEntryData.batchSize;
    }
    public boolean matches(OfferEntryData offerEntryData){
        return Objects.equals(offerEntryData.getName(), this.getName()) && offerEntryData.batchSize == this.offerEntryData.batchSize;
    }

}
