package net.semperidem.fishingclub.leaderboard;

import net.minecraft.entity.player.PlayerEntity;
import net.semperidem.fishingclub.fish.Fish;

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
        for(String attribute : LeaderboardAttribute.LEADERBOARD_ATTRIBUTES.keySet()) {
            if (LeaderboardAttribute.LEADERBOARD_ATTRIBUTES.get(attribute).isSum) {
                String descendingName = attribute + "#";
                leaderboards.put(descendingName, new Leaderboard(descendingName, true));
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
        for(String attributeName : fish.getAttributeMap().keySet()) {
            record(
                    caughtBy.getName().getString(),
                    LeaderboardAttribute.get(attributeName),
                    fish
            );
        }
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
