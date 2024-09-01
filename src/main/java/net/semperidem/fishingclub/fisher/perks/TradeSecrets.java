package net.semperidem.fishingclub.fisher.perks;

import java.util.HashMap;
import java.util.Optional;

public class TradeSecrets {
    static final HashMap<String, TradeSecret> NAME_TO_SKILL = new HashMap<>();
    public static TradeSecret BOBBER_THROW_CHARGE;
    public static TradeSecret FISH_QUANTITY;
    public static TradeSecret BOBBER_SIZE_BOAT;//todo add open water check on all boat perks
    public static TradeSecret FISH_QUANTITY_BOAT;
    public static TradeSecret LINE_HEALTH_BOAT;
    public static TradeSecret TREASURE_CHANCE_BOAT;
    public static TradeSecret CATCH_RATE_RAIN;
    public static TradeSecret FISH_QUALITY_RAIN;
    public static TradeSecret SUMMON_RAIN;
    public static TradeSecret FIRST_CATCH;
    public static TradeSecret FIRST_CATCH_BUFF_QUALITY;
    public static TradeSecret FIRST_CATCH_BUFF_CATCH_RATE;
    public static TradeSecret CHANGE_OF_SCENERY;
    public static TradeSecret INSTANT_FISH_CREDIT;
    public static TradeSecret BOMB_FISHING;
    public static TradeSecret FISHING_SCHOOL;
    public static TradeSecret SLOWER_FISH;
    public static TradeSecret EXPERIENCE_BOOST;
    public static TradeSecret LUCKY_FISHING;
    public static TradeSecret PASSIVE_FISHING_XP_BUFF;
    public static TradeSecret WATCH_AND_LEARN;
    public static TradeSecret QUALITY_CELEBRATION;
    public static TradeSecret SHARED_BUFFS;
    public static TradeSecret FISHERMAN_LINK;
    public static TradeSecret DOUBLE_LINK;
    public static TradeSecret FISHERMAN_SUMMON;
    public static TradeSecret MAGIC_ROD_SUMMON;
    public static TradeSecret FREE_SHOP_SUMMON;

    public static void register() {
        BOBBER_THROW_CHARGE = TradeSecret.builder()
                .name("bobber_throw_charge")
                .costPerLevel(2, 1)
                .build();

        FISH_QUANTITY = TradeSecret.builder()
                .name("fish_quantity")
                .levelValues(0.1f, 0.15f, 0.2f)
                .costPerLevel(1, 1, 1)
                .build();

        LINE_HEALTH_BOAT = TradeSecret.builder()
                .name("line_health_boat")
                .levelValues(50, 100, 200)
                .build();
        BOBBER_SIZE_BOAT = TradeSecret.builder()
                .name("bobber_size_boat")
                .levelValues(0.05f, 0.1f, 0.2f)
                .parent(LINE_HEALTH_BOAT)
                .build();
        FISH_QUANTITY_BOAT = TradeSecret.builder()
                .name("fish_quantity_boat")
                .levelValues(0.05f, 0.1f, 0.2f)
                .parent(BOBBER_SIZE_BOAT)
                .build();
        TREASURE_CHANCE_BOAT = TradeSecret.builder()
                .name("treasure_chance_boat")
                .levelValues(0.1f, 0.25f, 0.5f)
                .costPerLevel(2, 2 ,2)
                .parent(FISH_QUANTITY_BOAT)
                .build();

        CATCH_RATE_RAIN = TradeSecret.builder()
                .name("catch_rate_rain")
                .levelValues(0.25f, 0.5f, 1)
                .costPerLevel(1,2,3)
                .build();
        FISH_QUALITY_RAIN = TradeSecret.builder()
                .name("fish_quality_rain")
                .levelValues(0.1f, 0.25f, 0.5f)
                .costPerLevel(1,1,1)
                .parent(CATCH_RATE_RAIN)
                .build();
        SUMMON_RAIN = TradeSecret.builder().name("rain_summon")
                .levelValues(1, 0.875f, 0.75f, 0.675f, 0.5f)
                .costPerLevel(4, 1, 1, 1, 1)
                .parent(FISH_QUALITY_RAIN)
                .build();

        FIRST_CATCH = TradeSecret.builder()
                .name("first_catch")
                .build();
        FIRST_CATCH_BUFF_QUALITY = TradeSecret.builder()
                .name("quality_increase_first_catch")
                .levelValues(1, 1.25f, 1.5f, 1.75f, 2f)
                .costPerLevel(2,1,1,1,1)
                .parent(FIRST_CATCH)
                .build();
        FIRST_CATCH_BUFF_CATCH_RATE = TradeSecret.builder()
                .name("frequent_catch_first_catch")
                .levelValues(1, 1.25f, 1.5f, 1.75f, 2)
                .costPerLevel(2, 1, 1, 1, 1)
                .parent(FIRST_CATCH_BUFF_QUALITY)
                .build();

        CHANGE_OF_SCENERY = TradeSecret.builder()
                .name("change_of_scenery")
                .costPerLevel(2)
                .build();

        INSTANT_FISH_CREDIT = TradeSecret.builder()
                .name("instant_fish_credit")
                .levelValues(0.5f, 0.6f, 0.7f, 0.8f, 0.9f, 1)
                .costPerLevel(1, 2, 3, 4, 5, 6)
                .build();

        BOMB_FISHING = TradeSecret.builder()
                .name("bomb_fishing")
                .costPerLevel(5)
                .build();

        FISHING_SCHOOL = TradeSecret.builder()
                .name("fishing_school")
                .levelValues(1, 1.5f, 2)
                .build();
        SLOWER_FISH = TradeSecret.builder()
                .name("slower_fish")
                .levelValues(0.1f, 0.25f, 0.5f)
                .costPerLevel(1, 2, 4)
                .parent(FISHING_SCHOOL)
                .build();
        EXPERIENCE_BOOST = TradeSecret.builder()
                .name("experience_boost")
                .levelValues(0.1f, 0.25f, 0.5f, 1f)
                .costPerLevel(1,2,3,4)
                .parent(SLOWER_FISH)
                .build();
        LUCKY_FISHING = TradeSecret.builder()
                .name("lucky_fishing")
                .levelValues(1, 2, 3)
                .parent(EXPERIENCE_BOOST)
                .build();

        WATCH_AND_LEARN = TradeSecret.builder()
                .name("watch_and_learn")
                .levelValues(0.1f, 0.125f, 0.15f, 0.175f, 0.2f)
                .build();
        PASSIVE_FISHING_XP_BUFF = TradeSecret.builder()
                .name("passive_fishing_xp_buff")
                .levelValues(1, 2, 5, 10)
                .costPerLevel(1, 2, 3, 4)
                .parent(WATCH_AND_LEARN)
                .build();

        MAGIC_ROD_SUMMON = TradeSecret.builder()
                .name("magic_rod_summon")
                .levelValues(1,2,3)
                .build();

        FISHERMAN_LINK = TradeSecret.builder()
                .name("fisherman_link")
                .build();
        SHARED_BUFFS = TradeSecret.builder()
                .name("shared_buffs")
                .levelValues(1, 2, 4)
                .parent(FISHERMAN_LINK)
                .build();
        QUALITY_CELEBRATION = TradeSecret.builder()
                .name("quality_celebration")
                .parent(FISHERMAN_LINK)
                .build();
        DOUBLE_LINK = TradeSecret.builder()
                .name("double_link")
                .parent(FISHERMAN_LINK)
                .costPerLevel(3)
                .build();
        FISHERMAN_SUMMON = TradeSecret.builder()
                .name("fisherman_summon")
                .levelValues(1, 0.875f, 0.75f, 0.675f, 0.5f)
                .costPerLevel(3, 1, 1, 1, 1)
                .parent(FISHERMAN_LINK)
                .build();
        FREE_SHOP_SUMMON = TradeSecret.builder()
                .name("free_shop_summon")
                .costPerLevel(5)
                .parent(FISHERMAN_SUMMON)
                .build();
    }

    public static Optional<TradeSecret> fromName(String name) {
        return Optional.of(NAME_TO_SKILL.get(name));
    }

    public static int count() {
        return NAME_TO_SKILL.size();
    }
}
