package net.semperidem.fishingclub.leaderboard;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.SaveProperties;
import net.semperidem.fishingclub.FishingLevelProperties;
import net.semperidem.fishingclub.fish.Fish;
import net.semperidem.fishingclub.fisher.FishingCard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class LeaderboardTracker {
    private static final float DISCOUNT_PER_TITLE = 0.1f;
    public final Leaderboard<Fish> bestWeight;
    public final Leaderboard<Fish> worstWeight;
    public final Leaderboard<Fish> bestLength;
    public final Leaderboard<Fish> worstLength;
    public final Leaderboard<FishingCard> highestCredit;
    public final Leaderboard<FishingCard> highestLevel;
    public final Leaderboard<FishingCard> longestCapeClaimTotal;
    public LeaderboardTracker() {
        bestWeight = new Leaderboard<>("weight+", Text.literal("§lHeaviest Fish"), "kg", false, fish -> fish.weight);
        worstWeight = new Leaderboard<>("weight-", Text.literal("§lLightest Fish"), "kg", true, fish -> fish.weight);
        bestLength = new Leaderboard<>("length+", Text.literal("§lLongest Fish"), "cm", false, fish -> fish.length);
        worstLength = new Leaderboard<>("length-", Text.literal("§lShortest Fish"), "cm", true, fish -> fish.length);
        highestCredit = new Leaderboard<>("_credit+", Text.literal("§lMost Credit"), "$", false, card -> (float) card.getCredit());
        highestLevel = new Leaderboard<>("_level+", Text.literal("§lHighest Level"), "", false, card -> (float) card.getLevel());
        longestCapeClaimTotal = new Leaderboard<>("_capeClaim", Text.literal("§lTotal Crowned Time"), " ticks", false, card -> {
            float capeTotal = card.getCapeTime();
            SaveProperties saveProperties = Objects.requireNonNull(card.getHolder().getServer()).getSaveProperties();
            if (!(saveProperties instanceof FishingLevelProperties fishingLevelProperties)) {
                return capeTotal;
            }

            if (fishingLevelProperties.getFishingKingUUID().compareTo(card.getHolder().getUuid()) != 0) {
                return capeTotal;
            }

            return capeTotal + fishingLevelProperties.getCurrentClaimTime();
        });
    }

    public float getDiscount(PlayerEntity player) {
        float discount = 0;
        if (longestCapeClaimTotal.isFirst(player)) {
                return 0;
        }
        if (bestWeight.isFirst(player)) {
            discount += DISCOUNT_PER_TITLE;
        }
        if (worstWeight.isFirst(player)) {
            discount += DISCOUNT_PER_TITLE;
        }
        if (bestLength.isFirst(player)) {
            discount += DISCOUNT_PER_TITLE;
        }
        if (worstLength.isFirst(player)) {
            discount += DISCOUNT_PER_TITLE;
        }
        if (highestLevel.isFirst(player)) {
            discount += DISCOUNT_PER_TITLE;
        }
        return discount;
    }


    public void record(PlayerEntity caughtBy, Fish fish) {
        bestWeight.consume(caughtBy, fish);
        worstWeight.consume(caughtBy, fish);
        bestLength.consume(caughtBy, fish);
        worstLength.consume(caughtBy, fish);
    }

    public void record(PlayerEntity caughtBy, FishingCard fishingCard, Leaderboard<FishingCard> leaderboard) {
        leaderboard.consume(caughtBy, fishingCard);
    }

    public ArrayList<Leaderboard<?>> getLeaderboards() {
        return new ArrayList<>(Arrays.asList(
                longestCapeClaimTotal,
                bestWeight,
                worstWeight,
                bestLength,
                worstLength,
                highestCredit,
                highestLevel
        ));
    }
    public static void tick() {

    }


    private static final long MC_DAILY = 24000;
    private static final long MC_WEEKLY = MC_DAILY * 7;
    private static final long MC_YEARLY = (long) (MC_WEEKLY * 52.1775);

    private static final int TICKS_IN_SECOND = 20;

    private static final long HOURLY = 3600 * TICKS_IN_SECOND;
    private static final long DAILY = HOURLY * 24;
    private static final long WEEKLY = DAILY * 7;//TODO IMPLEMENT TIMED LEADERBOARDS
}
