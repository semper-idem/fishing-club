package net.semperidem.fishingclub.leaderboard;

import com.google.gson.InstanceCreator;

import java.lang.reflect.Type;

public class LeaderboardInstanceCreator implements InstanceCreator<Leaderboard> {
    @Override
    public Leaderboard createInstance(Type type) {
        return new Leaderboard("Unnamed");
    }
}
