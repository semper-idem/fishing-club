package net.semperidem.fishingclub.leaderboard;

import net.semperidem.fishingclub.fish.Fish;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.BiFunction;
import java.util.function.Function;

public class LeaderboardAttribute {
    public static final HashMap<String,LeaderboardAttribute> LEADERBOARD_ATTRIBUTES = new HashMap<>();

    public static final LeaderboardAttribute WEIGHT = new LeaderboardAttribute("weight");
    public static final LeaderboardAttribute LENGTH = new LeaderboardAttribute("length");
    public static final LeaderboardAttribute VALUE = new LeaderboardAttribute("value");
    public static final LeaderboardAttribute LEVEL = new LeaderboardAttribute("level");
    public static final LeaderboardAttribute DAMAGE = new LeaderboardAttribute("damage");
    public static final LeaderboardAttribute GRADE = new LeaderboardAttribute("grade", true, (fish, leaderboard) -> {
        Leaderboard.Entry currentEntry = leaderboard.getCurrentRecord(fish.caughtByUUID);
        return String.valueOf(currentEntry.value + 1);
    });

    String attributeName;
    private BiFunction<Fish, Leaderboard, String> valueGetter;
    boolean isSum = false;



    LeaderboardAttribute(String attributeName, boolean isSum, BiFunction<Fish, Leaderboard, String> valueGetter) {
        this.attributeName = attributeName;
        this.valueGetter = valueGetter;
        this.isSum = isSum;
        LEADERBOARD_ATTRIBUTES.put(attributeName, this);
    }
    LeaderboardAttribute(String attributeName) {
        this.attributeName = attributeName;
        this.valueGetter = (fish, leaderboard) -> fish.getAttributeMap().get(attributeName);
        LEADERBOARD_ATTRIBUTES.put(attributeName, this);
    }

    public String getValue(Fish fish, Leaderboard leaderboard) {
        return valueGetter.apply(fish, leaderboard);
    }

    public static LeaderboardAttribute get(String attributeName) {
        return LEADERBOARD_ATTRIBUTES.get(attributeName);
    }
}
