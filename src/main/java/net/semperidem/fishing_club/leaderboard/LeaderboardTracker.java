package net.semperidem.fishing_club.leaderboard;

import java.util.ArrayList;
import java.util.Arrays;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.semperidem.fishing_club.fish.FishComponent;
import net.semperidem.fishing_club.fisher.FishingCard;

public class LeaderboardTracker {
    private static final float DISCOUNT_PER_TITLE = 0.1f;
    public final Leaderboard<FishComponent> bestWeight;
    public final Leaderboard<FishComponent> worstWeight;
    public final Leaderboard<FishComponent> bestLength;
    public final Leaderboard<FishComponent> worstLength;
    public final Leaderboard<FishingCard> highestCredit;
    public final Leaderboard<FishingCard> highestLevel;
    public LeaderboardTracker() {
        bestWeight = new Leaderboard<>("weight+", Text.literal("§lHeaviest Fish"), "kg", false, FishComponent::weight);
        worstWeight = new Leaderboard<>("weight-", Text.literal("§lLightest Fish"), "kg", true, FishComponent::weight);
        bestLength = new Leaderboard<>("length+", Text.literal("§lLongest Fish"), "cm", false, FishComponent::length);
        worstLength = new Leaderboard<>("length-", Text.literal("§lShortest Fish"), "cm", true, FishComponent::length);
        highestCredit = new Leaderboard<>("_credit+", Text.literal("§lMost Credit"), "$", false, card -> (float) card.getCredit());
        highestLevel = new Leaderboard<>("_level+", Text.literal("§lHighest Level"), "", false, card -> (float) card.getLevel());
    }

    public float getDiscount(PlayerEntity player) {
        float discount = 0;
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


    public void record(PlayerEntity caughtBy, FishComponent fish) {
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
