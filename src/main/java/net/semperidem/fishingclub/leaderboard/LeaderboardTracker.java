package net.semperidem.fishingclub.leaderboard;

import net.minecraft.entity.player.PlayerEntity;
import net.semperidem.fishingclub.fish.Fish;
import net.semperidem.fishingclub.fisher.FishingCard;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LeaderboardTracker {
    private static final long MC_DAILY = 24000;
    private static final long MC_WEEKLY = MC_DAILY * 7;
    private static final long MC_YEARLY = (long) (MC_WEEKLY * 52.1775);

    private static final int TICKS_IN_SECOND = 20;

    private static final long HOURLY = 3600 * TICKS_IN_SECOND;
    private static final long DAILY = HOURLY * 24;
    private static final long WEEKLY = DAILY * 7;//TODO IMPLEMENT TIMED LEADERBOARDS

    final Map<String, Leaderboard> leaderboards = new HashMap<>();


    public LeaderboardTracker() {
        for(String attribute : LeaderboardAttributes.LEADERBOARD_ATTRIBUTES.keySet()) {
            if (LeaderboardAttributes.LEADERBOARD_ATTRIBUTES.get(attribute).isSum) {
                String name = attribute + "#";
                leaderboards.put(name, new Leaderboard(name, true));
                continue;
            }
            String descendingName = attribute + "+";
            leaderboards.put(descendingName, new Leaderboard(descendingName, true));
            String ascendingName = attribute + "-";
            leaderboards.put(ascendingName, new Leaderboard(ascendingName));
        }
    }

    public Map<String, Leaderboard> getLeaderboards() {
        return leaderboards;
    }

    public void record(PlayerEntity caughtBy, Fish fish) {
        String holderName = caughtBy.getName().getString();
        record(holderName, LeaderboardAttributes.LENGTH, fish);
        record(holderName, LeaderboardAttributes.WEIGHT, fish);
        record(holderName, LeaderboardAttributes.GRADE, fish);
        record(holderName, LeaderboardAttributes.VALUE, fish);
        record(holderName, LeaderboardAttributes.LEVEL, fish);
        record(holderName, LeaderboardAttributes.DAMAGE, fish);
        record(holderName, LeaderboardAttributes.COUNT, fish);
    }

    public void record(PlayerEntity caughtBy, FishingCard fishingCard) {
        String holderName = caughtBy.getName().getString();
        UUID holderUUID = caughtBy.getUuid();

        Leaderboard lbPlayerLevel = leaderboards.get(LeaderboardAttributes.PLAYER_LEVEL.attributeName + '#');
        lbPlayerLevel.consume(holderUUID, holderName, LeaderboardAttributes.PLAYER_LEVEL.getValue(fishingCard, lbPlayerLevel));

        Leaderboard lbCapeTime = leaderboards.get(LeaderboardAttributes.CAPE_TIME.attributeName + '#');
        lbCapeTime.consume(holderUUID, holderName, LeaderboardAttributes.CAPE_TIME.getValue(fishingCard, lbCapeTime));

        Leaderboard lbCredit = leaderboards.get(LeaderboardAttributes.CREDIT.attributeName + '#');
        lbCredit.consume(holderUUID, holderName, LeaderboardAttributes.CREDIT.getValue(fishingCard, lbCredit));

    }

    private void record(String holderName, LeaderboardAttribute attribute, Fish fish) {
        if (attribute.isSum) {
            leaderboards.get(attribute.attributeName + '#').consume(fish.caughtByUUID, holderName, attribute, fish);
            return;
        }
        leaderboards.get(attribute.attributeName + '-').consume(fish.caughtByUUID, holderName, attribute, fish);
        leaderboards.get(attribute.attributeName + '+').consume(fish.caughtByUUID, holderName, attribute, fish);
    }

    public static void tick() {

    }
}
