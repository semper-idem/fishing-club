package net.semperidem.fishingclub.item.fishing_rod.components;

public class ItemStat {
    public static final ItemStat BASE_T1 = new ItemStat(0, 1);
    public static final ItemStat BASE_T2 = new ItemStat(12.5f, 2);
    public static final ItemStat BASE_T3 = new ItemStat(25, 3);
    public static final ItemStat BASE_T4 = new ItemStat(37.5f, 4);
    public static final ItemStat BASE_T5 = new ItemStat(50, 5);
    public static final ItemStat BASE_T6 = new ItemStat(100, 10);
    public static final ItemStat MULTIPLIER_T0 = new ItemStat(0, 0);
    public static final ItemStat MULTIPLIER_T01 = new ItemStat(0.1f, 0.1f);
    public static final ItemStat MULTIPLIER_T0125 = new ItemStat(0.125f, 0.125f);
    public static final ItemStat MULTIPLIER_T015 = new ItemStat(0.15f, 0.15f);
    public static final ItemStat MULTIPLIER_T02 = new ItemStat(0.2f, 0.2f);
    public static final ItemStat MULTIPLIER_T025 = new ItemStat(0.25f, 0.25f);
    public static final ItemStat MULTIPLIER_T05 = new ItemStat(0.5f, 0.5f);
    public static final ItemStat MULTIPLIER_T1 = new ItemStat(0.8f, 1);
    public static final ItemStat MULTIPLIER_T2 = new ItemStat(0.9f, 2);
    public static final ItemStat MULTIPLIER_T3 = new ItemStat(1, 3);
    public static final ItemStat MULTIPLIER_T4 = new ItemStat(1.1f, 4);
    public static final ItemStat MULTIPLIER_T5 = new ItemStat(1.2f, 5);
    public static final ItemStat MULTIPLIER_T6 = new ItemStat(1.3f, 6);
    public static final ItemStat MULTIPLIER_T7 = new ItemStat(1.4f, 7);
    public static final ItemStat MULTIPLIER_T8 = new ItemStat(1.5f, 8);
    public static final ItemStat MULTIPLIER_T9 = new ItemStat(1.75f, 8);
    public static final ItemStat MULTIPLIER_T10 = new ItemStat(2, 10);


    public final float value;
    public final float rating;

    public ItemStat(float value, float rating) {
        this.value = value;
        this.rating = rating;
    }
}
