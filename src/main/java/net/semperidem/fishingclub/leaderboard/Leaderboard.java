package net.semperidem.fishingclub.leaderboard;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;

import java.util.ArrayList;
import java.util.UUID;

public class Leaderboard {
    String name;
    ArrayList<Entry> standings = new ArrayList<>();

    public Leaderboard(String name) {
        this.name = name;
    }

    public Leaderboard(NbtCompound nbt) {
        if (!nbt.contains("name") || !nbt.contains("standings")) {
            return;//todo handle malformed
        }
        this.name = nbt.getString("name");
        NbtList entryList = nbt.getList("standings", NbtElement.COMPOUND_TYPE);
        entryList.forEach(entry -> standings.add(new Entry((NbtCompound) entry)));
    }

    public void validateAndAdd(UUID holder, double value) {
        int place = 0;
        for(Entry entry : standings) {
            if (value > entry.value) {
                break;
            }
            place++;
        }
        int currentPlace = getIndexOf(holder);
        if (currentPlace >= 0 && value > standings.get(currentPlace).value) {
            standings.remove(currentPlace);
        }
        standings.add(place, new Entry(holder, value));
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
        return standings;
    }

    public NbtCompound toNbt() {
        NbtCompound nbt = new NbtCompound();
        nbt.putString("name", name);
        NbtList entryList = new NbtList();
        for(Entry entry : standings) {
            entryList.add(entry.toNbt());
        }
        nbt.put("standings", entryList);
        return nbt;
    }

    static class Entry {
        double value;
        UUID holder;

        private Entry(UUID holder, double value) {
            this.holder = holder;
            this.value = value;
        }

        private Entry(NbtCompound nbt) {
            if (!nbt.contains("holder") || !nbt.contains("value")) {
                return;//todo handle malformed entries
            }
            this.holder = nbt.getUuid("holder");
            this.value = nbt.getDouble("value");
        }

        public double getValue() {
            return value;
        }

        public UUID getHolder() {
            return holder;
        }

        public NbtCompound toNbt() {
            NbtCompound nbt = new NbtCompound();
            nbt.putUuid("holder", holder);
            nbt.putDouble("value", value);
            return nbt;
        }
    }
}
