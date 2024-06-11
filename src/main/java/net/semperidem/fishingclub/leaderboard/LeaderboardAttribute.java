package net.semperidem.fishingclub.leaderboard;

import net.semperidem.fishingclub.fish.Fish;

import java.util.function.BiFunction;

public class LeaderboardAttribute<T> {

    String attributeName;
    private BiFunction<T, Leaderboard, Float> valueGetter;
    boolean isSum = false;



    LeaderboardAttribute(String attributeName, boolean isSum, BiFunction<T, Leaderboard, Float> valueGetter) {
        this.attributeName = attributeName;
        this.valueGetter = valueGetter;
        this.isSum = isSum;
        LeaderboardAttributes.LEADERBOARD_ATTRIBUTES.put(attributeName, this);
    }

    LeaderboardAttribute(String attributeName, BiFunction<T, Leaderboard, Float> valueGetter) {
        this(attributeName, false, valueGetter);
    }
    LeaderboardAttribute(String attributeName) {
        this.attributeName = attributeName;
        this.valueGetter = (possiblyFish, leaderboard) -> {
            if (possiblyFish instanceof Fish fish) {
                return fish.getAttributeMap().get(attributeName);
            }
            return 0f;
        };
        LeaderboardAttributes.LEADERBOARD_ATTRIBUTES.put(attributeName, this);
    }

    public Float getValue(T t, Leaderboard leaderboard) {
        return valueGetter.apply(t, leaderboard);
    }

    public static LeaderboardAttribute get(String attributeName) {
        return LeaderboardAttributes.LEADERBOARD_ATTRIBUTES.get(attributeName);
    }
}
