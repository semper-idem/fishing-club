package net.semperidem.fishingclub.leaderboard;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

public class Leaderboard implements Serializable {
    private final String name;
    private final boolean startFromHighest;
    private final ArrayList<Entry> standings = new ArrayList<>();


    public Leaderboard(String name, boolean startFromHighest) {
        this.name = name;
        this.startFromHighest = startFromHighest;
    }

    public String getName() {
        return name;
    }

    public Leaderboard(String name) {
        this(
                name,
                false
        );
    }

    public void consume(UUID holder, double value) {
        int place = 0;
        int previousPlace = getIndexOf(holder);
        if (previousPlace >= 0 && (getHolderValue(holder) >= value & startFromHighest)) {
            return;
        }
        for(Entry entry : standings) {
            if (value > entry.value & startFromHighest) {
                break;
            }
            place++;
        }
        if (previousPlace >= 0) {
            standings.remove(previousPlace);
        }
        standings.add(place, new Entry(holder, value));
    }

    private boolean hasHolderRecord(UUID holder) {
        return getIndexOf(holder) >= 0;
    }

    private double getHolderValue(UUID holder) {
        return standings.get(getIndexOf(holder)).value;
    }

    private int getIndexOf(UUID playerUUID) {
        int place = 0;
        for(Entry entry : standings) {
            if (entry.holder.equals(playerUUID)) {
                return place;
            }
            place++;
        }
        return -1;
    }

    private ArrayList<Entry> getStandings() {
        ArrayList<Entry> copiedStandings = new ArrayList<>();
        Collections.copy(standings, copiedStandings);
        return copiedStandings;
    }

    static class Entry implements Serializable{
        private final double value;
        private final UUID holder;

        private Entry(UUID holder, double value) {
            this.holder = holder;
            this.value = value;
        }
    }
}
