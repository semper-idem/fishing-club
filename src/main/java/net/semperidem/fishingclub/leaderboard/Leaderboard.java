package net.semperidem.fishingclub.leaderboard;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;

public class Leaderboard<T> {
    public final String name;
    public final Text label;
    public final boolean ascending;
    private final TreeSet<Entry> standings;
    private final HashMap<UUID, Entry> unorderedStandings;
    public final String unit;
    private final Function<T, Float> valueGetter;

    public Leaderboard(String name, Text label, String unit, boolean ascending, Function<T, Float> valueGetter) {
        this.name = name;
        this.label = label;
        this.unit = unit;
        this.ascending = ascending;
        this.valueGetter = valueGetter;
        this.standings = new TreeSet<>();
        this.unorderedStandings = new HashMap<>();

    }

    public String getName() {
        return name;
    }

    public Entry getCurrentRecord(UUID uuid) {
        for(Entry entry : standings) {
            if (entry.key.compareTo(uuid) == 0) {
                return entry;
            }
        }
        return null;
    }

    public boolean isFirst(PlayerEntity player) {
        return standings.first().key.compareTo(player.getUuid()) == 0;
    }

    void consume(PlayerEntity player, T valueHolder) {
        UUID key = player.getUuid();
        Float value = valueGetter.apply(valueHolder);

        Entry presentEntry = unorderedStandings.get(key);
        if (presentEntry != null && presentEntry.value > value ^ ascending) {
            return;
        }
        if (presentEntry != null) {
            standings.remove(presentEntry);
        }
        Entry newEntry = new Entry(
                key,
                value,
                player.getName().getString(),
                valueHolder.toString()
        );
        standings.add(newEntry);
        unorderedStandings.put(key, newEntry);
    }


    public int getIndexOf(UUID playerUUID) {
        int indexOf = -1;
        Iterator<Entry> entries = ascending ? standings.iterator() : standings.descendingIterator();
        Entry e;
        while (entries.hasNext()) {
            indexOf++;
            e = entries.next();
            if (e.key.compareTo(playerUUID) == 0) {
                return indexOf;
            }
        }
        return standings.size();
    }

    public static void writeNbt(NbtCompound tag, Leaderboard<?> leaderboard) {
        NbtList leaderboardEntryList = new NbtList();
        for(Iterator<Entry> i = leaderboard.getIterator(); i.hasNext();) {
            leaderboardEntryList.add(i.next().toNbt());
        }
        tag.put(leaderboard.name, leaderboardEntryList);
    }

    public static void readNbt(NbtCompound tag, Leaderboard<?> leaderboard) {
        tag.getList(leaderboard.name, NbtElement.COMPOUND_TYPE).forEach(entryTag -> {
            Entry e = new Entry((NbtCompound) entryTag);
            leaderboard.standings.add(e);
            leaderboard.unorderedStandings.put(e.key, e);
        });
    }

    private String getFilePath(String path) {
        return path + name + ".json";
    }

    public Iterator<Entry> getIterator(){
        return ascending ? standings.iterator() : standings.descendingIterator();
    }


    public static class Entry implements Comparable<Entry> {
        public final UUID key;
        public final Float value;
        public final String playerName;
        public final String context;

        private Entry(NbtCompound entryTag) {
            this.key = entryTag.getUuid("key");
            this.value = entryTag.getFloat("value");
            this.playerName = entryTag.getString("playerName");
            this.context = entryTag.getString("context");
        }

        NbtCompound toNbt() {
            NbtCompound tag = new NbtCompound();
            tag.putUuid("key", this.key);
            tag.putFloat("value", this.value);
            tag.putString("playerName", this.playerName);
            tag.putString("context", this.context);
            return tag;
        }

        private Entry(UUID key, float value, String playerName, String context) {
            this.key = key;
            this.value = value;
            this.playerName = playerName;
            this.context = context;
        }

        @Override
        public String toString() {
            return this.key.toString() + " " + String.format("%.2f", this.value);
        }

        @Override
        public int compareTo(@NotNull Entry o) {
            return (int) (value - o.value);
        }
    }
}
