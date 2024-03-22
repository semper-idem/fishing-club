package net.semperidem.fishingclub.fisher.shop;

import net.minecraft.item.Item;

public class StockEntry {
    int requiredLevel;
    Item item;
    double price;
    double discount;
    int discountPer;
    double minPrice;

    StockEntry(int requiredLevel, Item item, double price, double discount, int discountPer, double minPrice) {
        this.requiredLevel = requiredLevel;
        this.item = item;
        this.price = price;
        this.discount = discount;
        this.discountPer = discountPer;
        this.minPrice = minPrice;
    }
    StockEntry(int requiredLevel, Item item, double price) {
        this.requiredLevel = requiredLevel;
        this.item = item;
    }
}
