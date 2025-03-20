package net.semperidem.fishingclub.fisher.tradesecret;

import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.item.fishing_rod.components.FishingRodCoreItem;
import net.semperidem.fishingclub.registry.FCComponents;
import net.semperidem.fishingclub.registry.FCStatusEffects;

import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;

public class TradeSecrets {
    static final HashMap<String, TradeSecret> NAME_TO_SKILL = new HashMap<>();
    public static TradeSecret BOBBER_THROW_CHARGE;
    public static TradeSecret FISH_WHISPERER;
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
    public static TradeSecret FISHERMAN_LINK;
    public static TradeSecret FISHERMAN_SUMMON;
    public static TradeSecret MAGIC_ROD_SUMMON;
    public static TradeSecret FISHER_ZEAL;
    public static TradeSecret FISHER_SENSE;

    public static void register() {
        BOBBER_THROW_CHARGE = TradeSecret.builder()
                .name("bobber_throw_charge")
                .costPerLevel(2, 1)
                .build();

        FISH_WHISPERER = TradeSecret.builder()
                .name("fish_whisperer")
                .build();

        FISH_QUANTITY = TradeSecret.builder()
                .name("fish_quantity")
                .levelValues(0.05f, 0.1f, 0.2f)
                .build();

        LINE_HEALTH_BOAT = TradeSecret.builder()
                .name("line_health_boat")
                .levelValues(2, 3, 4)
                .conditional(TradeSecret.REQUIRES_BOAT)
                .build();
        BOBBER_SIZE_BOAT = TradeSecret.builder()
                .name("bobber_size_boat")
                .levelValues(0.05f, 0.1f, 0.2f)
                .require(LINE_HEALTH_BOAT)
                .conditional(TradeSecret.REQUIRES_BOAT)
                .build();
        FISH_QUANTITY_BOAT = TradeSecret.builder()
                .name("fish_quantity_boat")
                .levelValues(0.05f, 0.1f, 0.2f)
                .require(BOBBER_SIZE_BOAT)
                .conditional(TradeSecret.REQUIRES_BOAT)
                .build();
        TREASURE_CHANCE_BOAT = TradeSecret.builder()
                .name("treasure_chance_boat")
                .levelValues(0.15f, 0.25f, 0.45f, 0.65f ,1f)
                .costPerLevel(2, 2 ,2, 2, 3)
                .require(FISH_QUANTITY_BOAT)
                .conditional(TradeSecret.REQUIRES_BOAT)
                .build();

        CATCH_RATE_RAIN = TradeSecret.builder()
                .name("catch_rate_rain")
                .levelValues(0.25f, 0.5f, 1)
                .costPerLevel(1,2,3)
                .conditional(TradeSecret.REQUIRES_RAIN)
                .build();
        FISH_QUALITY_RAIN = TradeSecret.builder()
                .name("fish_quality_rain")
                .levelValues(0.25f, 0.5f, 1f)
                .costPerLevel(1,1,1)
                .require(CATCH_RATE_RAIN)
                .conditional(TradeSecret.REQUIRES_RAIN)
                .build();
        SUMMON_RAIN = TradeSecret.builder().name("rain_summon")
                .levelCooldown(1, 0.875f, 0.75f, 0.675f, 0.5f)
                .costPerLevel(4, 1, 1, 1, 1)
                .require(FISH_QUALITY_RAIN)
                .require(FISH_WHISPERER)
                .active(
                        (source, target) -> {
                            source.getServerWorld().setWeather(
                                    100 + source.getRandom().nextInt(300),
                                    ServerWorld.RAIN_WEATHER_DURATION_PROVIDER.get(source.getRandom()),
                                    true,
                                    source.getRandom().nextFloat() < 0.1f
                            );
                            return true;
                            },
                        72000)
                .build();

        FIRST_CATCH = TradeSecret.builder()
                .name("first_catch")
                .build();
        FIRST_CATCH_BUFF_QUALITY = TradeSecret.builder()
                .name("quality_increase_first_catch")
                .active(FCStatusEffects.QUALITY_BUFF, 0, 1200)
                .levelDuration(1, 1.25f, 1.5f, 1.75f, 2f)
                .costPerLevel(2,1,1,1,1)
                .require(FIRST_CATCH)
                .build();
        FIRST_CATCH_BUFF_CATCH_RATE = TradeSecret.builder()
                .name("frequent_catch_first_catch")
                .active(FCStatusEffects.FREQUENCY_BUFF, 0, 1200)
                .levelDuration(1, 1.25f, 1.5f, 1.75f, 2)
                .costPerLevel(2, 1, 1, 1, 1)
                .require(FIRST_CATCH_BUFF_QUALITY)
                .build();

        CHANGE_OF_SCENERY = TradeSecret.builder()
                .name("change_of_scenery")
                .costPerLevel(2)
                .build();

        INSTANT_FISH_CREDIT = TradeSecret.builder()
                .name("instant_fish_credit")
                .levelValues(0.5f, 0.6f, 0.7f, 0.8f, 0.9f, 1)
                .require(FISH_WHISPERER)
                .costPerLevel(1, 2, 3, 4, 5, 6)
                .build();

        BOMB_FISHING = TradeSecret.builder()
                .name("bomb_fishing")
                .costPerLevel(5)
                .build();

        FISHING_SCHOOL = TradeSecret.builder()
                .name("fishing_school")
                .levelDuration(1, 1.5f, 2)
                .active(FCStatusEffects.BOBBER_BUFF, 24000, 6000)
                .require(FISH_WHISPERER)
                .build();
        SLOWER_FISH = TradeSecret.builder()
                .name("slower_fish")
                .levelValues(1, 2, 4)
                .active(FCStatusEffects.SLOW_FISH_BUFF, 24000, 6000)
                .costPerLevel(1, 2, 4)
                .require(FISHING_SCHOOL)
                .build();
        EXPERIENCE_BOOST = TradeSecret.builder()
                .name("experience_boost")
                .levelValues(0, 1, 4, 9)
                .active(FCStatusEffects.EXP_BUFF, 24000, 6000)
                .costPerLevel(1,2,3,4)
                .require(SLOWER_FISH)
                .build();
        LUCKY_FISHING = TradeSecret.builder()
                .name("lucky_fishing")
                .levelValues(1, 2, 3)
                .active(StatusEffects.LUCK, 24000, 6000)
                .require(EXPERIENCE_BOOST)
                .build();

        WATCH_AND_LEARN = TradeSecret.builder()
                .name("watch_and_learn")
                .levelValues(0.1f, 0.125f, 0.15f, 0.175f, 0.2f)
                .build();
        PASSIVE_FISHING_XP_BUFF = TradeSecret.builder()
                .name("passive_fishing_xp_buff")
                .levelValues(0, 1, 4, 9)//spread exp buff with each level up to skill value
                .costPerLevel(1, 2, 3, 4)
                .require(WATCH_AND_LEARN)
                .build();

        MAGIC_ROD_SUMMON = TradeSecret.builder()
                .name("magic_rod_summon")
                .levelCooldown(1, 0.75f, 0.5f, 0.25f)
                .active(
                        (source, target) -> {
                            ItemStack stackInHand = source.getMainHandStack();
                            if (!(stackInHand.getItem() instanceof FishingRodCoreItem)) {
                                return false;
                            }
                            ItemStack clonedRod = stackInHand.copy();
                            clonedRod.set(FCComponents.EXPIRATION_TIME, (int)source.getWorld().getTime() + 24000);
                            source.dropItem(clonedRod, false, false);
                            return true;
                        },
                        96000)
                .build();

        FISHERMAN_LINK = TradeSecret.builder()
                .name("fisherman_link")
                .costPerLevel(1, 3, 9)
                .require(FISH_WHISPERER)
                .active(
                        (source, target) -> {
                            FishingCard.of(source).linkTarget(target);
                            return true;
                        },
                        100
                )
                .build();
        QUALITY_CELEBRATION = TradeSecret.builder()
                .name("quality_celebration")
                .levelDuration(1, 1.5f, 2)
                .active(FCStatusEffects.QUALITY_BUFF, 100, 1200)
                .require(FISHERMAN_LINK)
                .build();
        FISHERMAN_SUMMON = TradeSecret.builder()
                .name("fisherman_summon")
                .levelDuration(1, 0.875f, 0.75f, 0.675f, 0.5f)
                .costPerLevel(3, 1, 1, 1, 1)
                .active(
                        (source, target) -> {
                            FishingCard.of(source).requestSummon();
                            return true;
                            },
                        72000
                )
                .require(FISHERMAN_LINK)
                .build();

        FISHER_ZEAL = TradeSecret.builder()
                .name("fisher_zeal")
                .levelValues(1, 2, 3, 4, 5)
                .costPerLevel(1, 2, 3, 3, 3)
                .build();

        FISHER_SENSE = TradeSecret.builder()
                .name("fisher_sense")
                .levelValues(1,2,8)
                .costPerLevel(3,2,1)
                .require(FISHER_ZEAL)
                .build();
    }

    public static Optional<TradeSecret> fromName(String name) {
        return Optional.of(NAME_TO_SKILL.get(name));
    }

    public static Collection<TradeSecret> all() {
        return NAME_TO_SKILL.values();
    }

    public static int count() {
        return NAME_TO_SKILL.size();
    }
}
