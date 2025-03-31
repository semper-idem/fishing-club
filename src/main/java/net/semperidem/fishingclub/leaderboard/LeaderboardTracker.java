package net.semperidem.fishingclub.leaderboard;

import java.util.ArrayList;
import java.util.Arrays;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.fish.specimen.SpecimenData;
import net.semperidem.fishingclub.fisher.Card;
import net.semperidem.fishingclub.registry.Components;
import org.ladysnake.cca.api.v3.component.Component;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.scoreboard.ScoreboardComponentFactoryRegistry;

public class LeaderboardTracker implements Component {
    public static final String TAG_KEY = "leaderboard";
    private final Scoreboard scoreboard;

    private static final float DISCOUNT_PER_TITLE = 0.1f;
    public final Leaderboard<SpecimenData> bestWeight;
    public final Leaderboard<SpecimenData> worstWeight;
    public final Leaderboard<SpecimenData> bestLength;
    public final Leaderboard<SpecimenData> worstLength;
    public final Leaderboard<Card> highestCredit;
    public final Leaderboard<Card> highestLevel;

    public LeaderboardTracker(Scoreboard scoreboard) {
        this.scoreboard = scoreboard;
        this.bestWeight = new Leaderboard<>("weight+", Text.literal("§lHeaviest Fish"), "kg", false, SpecimenData::weight);
        this.worstWeight = new Leaderboard<>("weight-", Text.literal("§lLightest Fish"), "kg", true, SpecimenData::weight);
        this.bestLength = new Leaderboard<>("length+", Text.literal("§lLongest Fish"), "cm", false, SpecimenData::length);
        this.worstLength = new Leaderboard<>("length-", Text.literal("§lShortest Fish"), "cm", true, SpecimenData::length);
        this.highestCredit = new Leaderboard<>("_credit+", Text.literal("§lMost Credit"), "$", false, card -> (float) card.getCredit());
        this.highestLevel = new Leaderboard<>("_level+", Text.literal("§lHighest Level"), "", false, card -> (float) card.getLevel());
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


    public void record(PlayerEntity caughtBy, SpecimenData fish) {
        bestWeight.consume(caughtBy, fish);
        worstWeight.consume(caughtBy, fish);
        bestLength.consume(caughtBy, fish);
        worstLength.consume(caughtBy, fish);
    }

    public void record(PlayerEntity caughtBy, Card card, Leaderboard<Card> leaderboard) {
        leaderboard.consume(caughtBy, card);
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

    private static final long MC_DAILY = 24000;
    private static final long MC_WEEKLY = MC_DAILY * 7;
    private static final long MC_YEARLY = (long) (MC_WEEKLY * 52.1775);

    private static final int TICKS_IN_SECOND = 20;

    private static final long HOURLY = 3600 * TICKS_IN_SECOND;
    private static final long DAILY = HOURLY * 24;
    private static final long WEEKLY = DAILY * 7;//TODO IMPLEMENT TIMED LEADERBOARDS

    public static LeaderboardTracker of(Scoreboard scoreboard) {
        return Components.LEADERBOARD_TRACKER.get(scoreboard);
    }

    @Override
    public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        Leaderboard.readNbt(tag, bestWeight);
        Leaderboard.readNbt(tag, worstWeight);
        Leaderboard.readNbt(tag, bestLength);
        Leaderboard.readNbt(tag, worstLength);
        Leaderboard.readNbt(tag, highestCredit);
        Leaderboard.readNbt(tag, highestLevel);
    }

    @Override
    public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        Leaderboard.writeNbt(tag, bestWeight);
        Leaderboard.writeNbt(tag, worstWeight);
        Leaderboard.writeNbt(tag, bestLength);
        Leaderboard.writeNbt(tag, worstLength);
        Leaderboard.writeNbt(tag, highestCredit);
        Leaderboard.writeNbt(tag, highestLevel);
    }
}
