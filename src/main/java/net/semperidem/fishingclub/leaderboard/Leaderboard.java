package net.semperidem.fishingclub.leaderboard;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.semperidem.fishingclub.fisher.FishingCard;
import org.jetbrains.annotations.NotNull;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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

    //TODO Test leaderboard with 1000 records
    public static void writeToPacket(PacketByteBuf packet, Leaderboard<?> leaderboard) {
        packet.writeInt(leaderboard.standings.size());
        for(Iterator<Entry> i = leaderboard.getIterator(); i.hasNext();) {
            i.next().toPacket(packet);
        }
    }

    public static void readStandings(PacketByteBuf packet, Leaderboard<?> leaderboard) {
        int size = packet.readInt();
        for(int i = 0; i < size; i++) {
            Entry e = new Entry(packet);
            leaderboard.standings.add(e);
            leaderboard.unorderedStandings.put(e.key, e);
        }
    }

    private String getFilePath(String path) {
        return path + name + ".json";
    }

    public Iterator<Entry> getIterator(){
        return ascending ? standings.iterator() : standings.descendingIterator();
    }

    public void loadFromPath(String path) {
        try {
            if (!Files.exists(Path.of(path))) {
                return;
            }
            //TODO Possible performance hit on game load
            Gson gson = new GsonBuilder().create();
            FileReader fileReader = new FileReader(getFilePath(path));
            JsonReader jsonReader = new JsonReader(fileReader);
            Entry[] entries = gson.fromJson(jsonReader, Entry[].class);
            for(Entry entry : entries) {
                standings.add(entry);
                unorderedStandings.put(entry.key, entry);
            }
            fileReader.close();
            jsonReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveToPath(String path) {
        try {
            Gson gson = new Gson();
            Files.createDirectories(Path.of(path));
            FileWriter fileWriter = new FileWriter(getFilePath(path));
            gson.toJson(standings, fileWriter);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static class Entry implements Comparable<Entry> {
        public final UUID key;
        public final Float value;
        public final String playerName;
        public final String context;

        private Entry(PacketByteBuf packet) {
            this.key = packet.readUuid();
            this.value = packet.readFloat();
            this.playerName = packet.readString();
            this.context = packet.readString();
        }

        void toPacket(PacketByteBuf packet) {
            packet.writeUuid(this.key);
            packet.writeFloat(this.value);
            packet.writeString(this.playerName);
            packet.writeString(this.context);
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
