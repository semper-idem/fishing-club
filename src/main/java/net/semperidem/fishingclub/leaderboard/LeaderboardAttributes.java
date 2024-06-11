package net.semperidem.fishingclub.leaderboard;

import net.minecraft.world.SaveProperties;
import net.semperidem.fishingclub.FishingLevelProperties;
import net.semperidem.fishingclub.fish.Fish;
import net.semperidem.fishingclub.fisher.FishingCard;

import java.util.HashMap;
import java.util.Objects;

public class LeaderboardAttributes {
    public static final HashMap<String,LeaderboardAttribute<?>> LEADERBOARD_ATTRIBUTES = new HashMap<>();

    public static final LeaderboardAttribute<Fish> WEIGHT = new LeaderboardAttribute<>("weight");
    public static final LeaderboardAttribute<Fish> LENGTH = new LeaderboardAttribute<>("length");
    public static final LeaderboardAttribute<Fish> VALUE = new LeaderboardAttribute<>("value");
    public static final LeaderboardAttribute<Fish> LEVEL = new LeaderboardAttribute<>("level");
    public static final LeaderboardAttribute<Fish> DAMAGE = new LeaderboardAttribute<>("damage");

    public static final LeaderboardAttribute<FishingCard> PLAYER_LEVEL = new LeaderboardAttribute<>("player_level",true, (fishingCard, leaderboard) -> (float) fishingCard.getLevel());
    public static final LeaderboardAttribute<FishingCard> CREDIT = new LeaderboardAttribute<>("credit",true, (fishingCard, leaderboard) -> (float) fishingCard.getCredit());

    public static final LeaderboardAttribute<Fish> GRADE = new LeaderboardAttribute<>("grade", true, (fish, leaderboard) -> {
        Leaderboard.Entry currentEntry = leaderboard.getCurrentRecord(fish.caughtByUUID);
        if (currentEntry != null) {
            return (float) (currentEntry.value + fish.grade == 5 ? 1 : 0);
        } else {
            return (float) (fish.grade == 5 ? 1 : 0);
        }
    });

    public static final LeaderboardAttribute<Fish> COUNT = new LeaderboardAttribute<>("count", true, (fish, leaderboard) -> {
        Leaderboard.Entry currentEntry = leaderboard.getCurrentRecord(fish.caughtByUUID);
        if (currentEntry != null) {
            return (float) (currentEntry.value + 1);
        } else {
            return (float) 1;
        }
    });


    public static final LeaderboardAttribute<FishingCard> CAPE_TIME = new LeaderboardAttribute<>("cape_time",true, (fishingCard, leaderboard) -> {
        SaveProperties saveProperties = Objects.requireNonNull(fishingCard.getHolder().getServer()).getSaveProperties();
        if (saveProperties instanceof FishingLevelProperties fishingLevelProperties) {
            return fishingCard.getCapeTime() + fishingLevelProperties.getCurrentClaimTime();
        }
        return fishingCard.getCapeTime();
    });
}
