package net.semperidem.fishingclub.fisher.shop;

import net.minecraft.item.Item;

public class StockEntry {
    public int requiredLevel;
    public Item item;
    public double price;
    public double discount;
    public int discountPer;
    public double minPrice;

    StockEntry(int requiredLevel, Item item, double price, double discount, int discountPer, double minPrice) {
        this(requiredLevel, item, price);
        this.discount = discount;
        this.discountPer = discountPer;
        this.minPrice = minPrice;
    }
    StockEntry(int requiredLevel, Item item, double price) {
        this.requiredLevel = requiredLevel;
        this.item = item;
        this.price = price;
    }

    public double getPriceFor(int count) {
        double totalDiscount = count == 1 || discountPer == 0 ? 0 : count / discountPer * discount;
        double discountedPrice = Math.max(minPrice,price - totalDiscount);
        return discountedPrice * count;
    }

}
