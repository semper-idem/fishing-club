package net.semperidem.fishingclub.leaderboard;

import net.minecraft.network.PacketByteBuf;
import net.semperidem.fishingclub.fish.Fish;
import net.semperidem.fishingclub.fisher.FishingCard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

public class Leaderboard implements Serializable {
    private final String name;
    private long resetInterval = 0;
    private long lastResetTick = 0;
    private final boolean descending;
    public final ArrayList<Entry> standings = new ArrayList<>();



    public Leaderboard(String name, boolean descending, long resetInterval, long lastResetTick) {
        this.name = name;
        this.descending = descending;
        this.resetInterval = resetInterval;
        this.lastResetTick = lastResetTick;
    }

    public Leaderboard(String name, boolean descending, long resetInterval) {
        this (
                name,
                descending,
                resetInterval,
                0
        );
    }

    public Leaderboard(String name, boolean descending) {
        this (
                name,
                descending,
                0
        );
    }

    public Leaderboard(String name) {
        this(
                name,
                false
        );
    }

    public String getName() {
        return name;
    }

    public Entry getCurrentRecord(UUID uuid) {
        for(Entry entry : standings) {
            if (entry.holder.compareTo(uuid) == 0) {
                return entry;
            }
        }
        return null;
    }


    void consume(UUID holder, String holderName, double value) {
        int place = 0;
        int previousPlace = getIndexOf(holder);
        if (previousPlace >= 0) {
            if ((getHolderValue(holder) >= value && descending) || (getHolderValue(holder) <= value && !descending)) {
                return;
            }
        }
        for(Entry entry : standings) {
            if ((value > entry.value && descending) || (value < entry.value && !descending)) {
                break;
            }
            place++;
        }
        if (previousPlace >= 0) {
            standings.remove(previousPlace);
        }
        standings.add(place, new Entry(
                holder,
                holderName,
                value
        ));
    }


    void consume(UUID holder, String holderName, LeaderboardAttribute<Fish> attribute, Fish fish) {
        double value = attribute.getValue(fish, this);
        consume(holder, holderName, value);
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

    //TODO Test leaderboard with 1000 records
    public static void writeToPacket(PacketByteBuf packet, Leaderboard leaderboard) {
        packet.writeString(leaderboard.name);
        packet.writeBoolean(leaderboard.descending);
        packet.writeLong(leaderboard.resetInterval);
        packet.writeLong(leaderboard.lastResetTick);
        packet.writeInt(leaderboard.standings.size());
        for(Entry entry : leaderboard.standings) {
            packet.writeUuid(entry.holder);
            packet.writeString(entry.holderName);
            packet.writeDouble(entry.value);
        }
    }

    public static Leaderboard fromPacket(PacketByteBuf packet) {
        Leaderboard leaderboard = new Leaderboard(
                packet.readString(),
                packet.readBoolean(),
                packet.readLong(),
                packet.readLong()

        );
        int size = packet.readInt();
        for(int i = 0; i < size; i++) {
            leaderboard.standings.add(
                    new Entry(
                            packet.readUuid(),
                            packet.readString(),
                            packet.readDouble()
                    )
            );
        }
        return leaderboard;
    }

    private ArrayList<Entry> getStandings() {
        ArrayList<Entry> copiedStandings = new ArrayList<>();
        Collections.copy(standings, copiedStandings);
        return copiedStandings;
    }

    public static class Entry implements Serializable{
        public String holderName;
        public final double value;
        private final UUID holder;

        private Entry(UUID holder, String holderName, double value) {
            this.holderName = holderName;
            this.holder = holder;
            this.value = value;
        }

        @Override
        public String toString() {
            return this.holder.toString() + " " + String.format("%.2f", this.value);
        }
    }
}
