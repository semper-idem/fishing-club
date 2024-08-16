package net.semperidem.fishingclub.fisher.shop;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.HashMap;

public class StockEntry {
    private static final HashMap<String, StockEntry> ITEM_TO_ENTRY = new HashMap<>();
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
        ITEM_TO_ENTRY.put(item.getName().getString(), this);//this breaks if we have more offers of one item fixme
    }

    public double getPriceFor(int count) {
        double totalDiscount = count == 1 || discountPer == 0 ? 0 : count / discountPer * discount;
        double discountedPrice = Math.max(minPrice,price - totalDiscount);
        return discountedPrice * count;
    }

    public static double getPriceFor(ItemStack cartItem, int quantity) {
        StockEntry stockEntry;
        if ((stockEntry = ITEM_TO_ENTRY.get(cartItem.getItem().getName().getString())) != null) {
            return stockEntry.getPriceFor(quantity);
        }
        return -1;
    }

}
