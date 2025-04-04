package net.semperidem.fishingclub.fisher.tradesecret;

import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.semperidem.fishingclub.fisher.Card;
import net.semperidem.fishingclub.item.fishing_rod.components.FishingRodCoreItem;
import net.semperidem.fishingclub.registry.Components;
import net.semperidem.fishingclub.registry.StatusEffects;

import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;

public class TradeSecrets {
    static final HashMap<String, TradeSecret> NAME_TO_SKILL = new HashMap<>();
    public static TradeSecret BOBBER_THROW_CHARGE;
    public static TradeSecret FISH_WHISPERER;
    public static TradeSecret FISH_QUANTITY;
    public static TradeSecret BOAT_BOBBER_SIZE;//todo add open water check on all boat perks
    public static TradeSecret BOAT_FISH_QUANTITY;
    public static TradeSecret BOAT_LINE_HEALTH;
    public static TradeSecret BOAT_LUCK;
    public static TradeSecret RAIN_CATCH_RATE;
    public static TradeSecret RAIN_FISH_QUALITY;
    public static TradeSecret RAIN_SUMMON;
    public static TradeSecret FIRST_CATCH;
    public static TradeSecret FIRST_CATCH_BUFF_QUALITY;
    public static TradeSecret FIRST_CATCH_BUFF_CATCH_RATE;
    public static TradeSecret CHANGE_OF_SCENERY;
    public static TradeSecret PLACE_IN_MY_HEART;
    public static TradeSecret INSTANT_FISH_CREDIT;
    public static TradeSecret BOMB_FISHING;
    public static TradeSecret BUFF_BOBBER_SIZE;
    public static TradeSecret BUFF_FISH_SPEED;
    public static TradeSecret BUFF_EXP;
    public static TradeSecret BUFF_LUCK;
    public static TradeSecret PASSIVE_EXP;
    public static TradeSecret SHARE_EXP;
    public static TradeSecret SHARE_QUALITY;
    public static TradeSecret LINK;
    public static TradeSecret LINK_SUMMON;
    public static TradeSecret ROD_SUMMON;
    public static TradeSecret FISHER_ZEAL;
    public static TradeSecret FISHER_SENSE;
    //TODO Chain hook

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

        BOAT_LINE_HEALTH = TradeSecret.builder()
                .name("boat_line_health")
                .levelValues(2, 3, 4)
                .conditional(TradeSecret.REQUIRES_BOAT)
                .build();
        BOAT_BOBBER_SIZE = TradeSecret.builder()
                .name("boat_bobber_size")
                .levelValues(0.05f, 0.1f, 0.2f)
                .require(BOAT_LINE_HEALTH)
                .conditional(TradeSecret.REQUIRES_BOAT)
                .build();
        BOAT_FISH_QUANTITY = TradeSecret.builder()
                .name("boat_fish_quantity")
                .levelValues(0.05f, 0.1f, 0.2f)
                .require(BOAT_BOBBER_SIZE)
                .conditional(TradeSecret.REQUIRES_BOAT)
                .build();
        BOAT_LUCK = TradeSecret.builder()
                .name("boat_luck")
                .levelValues(0.15f, 0.25f, 0.45f, 0.65f ,1f)
                .costPerLevel(2, 2 ,2, 2, 3)
                .require(BOAT_FISH_QUANTITY)
                .conditional(TradeSecret.REQUIRES_BOAT)
                .build();

        RAIN_CATCH_RATE = TradeSecret.builder()
                .name("rain_catch_rate")
                .levelValues(0.25f, 0.5f, 1)
                .costPerLevel(1,2,3)
                .conditional(TradeSecret.REQUIRES_RAIN)
                .build();
        RAIN_FISH_QUALITY = TradeSecret.builder()
                .name("rain_fish_quality")
                .levelValues(0.25f, 0.5f, 1f)
                .costPerLevel(1,1,1)
                .require(RAIN_CATCH_RATE)
                .conditional(TradeSecret.REQUIRES_RAIN)
                .build();
        RAIN_SUMMON = TradeSecret.builder().name("rain_summon")
                .levelCooldown(1, 0.875f, 0.75f, 0.675f, 0.5f)
                .costPerLevel(4, 1, 1, 1, 1)
                .require(RAIN_FISH_QUALITY)
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
        /*
         * TODO
         * Increase xp of first catch,
         * Add base so we prevent super unluck,
         * Add timed xp buff status effect
         *
         *
         * */

        FIRST_CATCH = TradeSecret.builder()
                .name("first_catch")
                .build();
        FIRST_CATCH_BUFF_QUALITY = TradeSecret.builder()
                .name("first_catch_buff_quality")
                .active(StatusEffects.QUALITY_BUFF, 0, 1200)
                .levelDuration(1, 1.25f, 1.5f, 1.75f, 2f)
                .costPerLevel(2,1,1,1,1)
                .require(FIRST_CATCH)
                .build();
        FIRST_CATCH_BUFF_CATCH_RATE = TradeSecret.builder()
                .name("first_catch_buff_catch_rate")
                .active(StatusEffects.CATCH_RATE_BUFF, 0, 1200)
                .levelDuration(1, 1.25f, 1.5f, 1.75f, 2)
                .costPerLevel(2, 1, 1, 1, 1)
                .require(FIRST_CATCH_BUFF_QUALITY)
                .build();

        CHANGE_OF_SCENERY = TradeSecret.builder()
                .name("change_of_scenery")
                .costPerLevel(2)
                .build();

        PLACE_IN_MY_HEART = TradeSecret.builder()
                .name("place_in_my_heart")
                .costPerLevel(1,1,1)
                .build();

        INSTANT_FISH_CREDIT = TradeSecret.builder()
                .name("instant_fish_credit")
                .levelValues(0.5f, 0.625f, 0.75f, 0.875f, 1f)
                .require(PLACE_IN_MY_HEART)
                .costPerLevel(1, 2, 3, 4, 5)
                .build();

        BOMB_FISHING = TradeSecret.builder()
                .name("bomb_fishing")
                .costPerLevel(5)
                .build();

        BUFF_BOBBER_SIZE = TradeSecret.builder()
                .name("buff_bobber_size")
                .levelDuration(1, 1.5f, 2)
                .active(StatusEffects.BOBBER, 24000, 6000)
                .require(FISH_WHISPERER)
                .build();
        BUFF_FISH_SPEED = TradeSecret.builder()
                .name("buff_fish_speed")
                .levelValues(1, 2, 4)
                .active(StatusEffects.SLOW_FISH, 24000, 6000)
                .costPerLevel(1, 2, 4)
                .require(BUFF_BOBBER_SIZE)
                .build();
        BUFF_EXP = TradeSecret.builder()
                .name("buff_exp")
                .levelValues(0, 1, 4, 9)
                .active(StatusEffects.EXP, 24000, 6000)
                .costPerLevel(1,2,3,4)
                .require(BUFF_FISH_SPEED)
                .build();
        BUFF_LUCK = TradeSecret.builder()
                .name("buff_luck")
                .levelValues(1, 2, 3)
                .active(net.minecraft.entity.effect.StatusEffects.LUCK, 24000, 6000)
                .require(BUFF_EXP)
                .build();

        SHARE_EXP = TradeSecret.builder()
                .name("share_exp")
                .levelValues(0.1f, 0.125f, 0.15f, 0.175f, 0.2f)
                .build();
        PASSIVE_EXP = TradeSecret.builder()
                .name("passive_exp")
                .levelValues(0, 1, 4, 9)//spread exp buff with each level up to skill value
                .costPerLevel(1, 2, 3, 4)
                .require(SHARE_EXP)
                .build();

        ROD_SUMMON = TradeSecret.builder()
                .name("rod_summon")
                .levelCooldown(1, 0.75f, 0.5f, 0.25f)
                .require(TradeSecrets.FISH_WHISPERER)
                .active(
                        (source, target) -> {
                            ItemStack stackInHand = source.getMainHandStack();
                            if (!(stackInHand.getItem() instanceof FishingRodCoreItem)) {
                                return false;
                            }
                            ItemStack clonedRod = stackInHand.copy();
                            clonedRod.set(Components.EXPIRATION_TIME, (int)source.getWorld().getTime() + 24000);
                            source.dropItem(clonedRod, false, false);
                            return true;
                        },
                        96000)
                .build();

        LINK = TradeSecret.builder()
                .name("link")
                .costPerLevel(1, 3, 9)
                .require(FISH_WHISPERER)
                .active(
                        (source, target) -> {
                            Card.of(source).linkTarget(target);
                            return true;
                        },
                        100
                )
                .build();
        SHARE_QUALITY = TradeSecret.builder()
                .name("share_quality")
                .levelDuration(1, 1.5f, 2)
                .active(StatusEffects.QUALITY_BUFF, 100, 1200)
                .require(LINK)
                .build();
        LINK_SUMMON = TradeSecret.builder()
                .name("link_summon")
                .levelDuration(1, 0.875f, 0.75f, 0.675f, 0.5f)
                .costPerLevel(3, 1, 1, 1, 1)
                .active(
                        (source, target) -> {
                            Card.of(source).requestSummon();
                            return true;
                            },
                        72000
                )
                .require(LINK)
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
