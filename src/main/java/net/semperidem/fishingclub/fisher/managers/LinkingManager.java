package net.semperidem.fishingclub.fisher.managers;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.fisher.perks.FishingPerks;

import java.util.ArrayList;
import java.util.UUID;

public class LinkingManager extends DataManager {
    private static final String TAG ="linked_with";
    private ArrayList<UUID> linkedFishers;

    public LinkingManager(FishingCard trackedFor) {
        super(trackedFor);
        linkedFishers = new ArrayList<>();
    }

    public ArrayList<UUID> getLinkedFishers() {
        return linkedFishers;
    }

    public boolean isLinked(UUID target){
        return linkedFishers.contains(target);
    }

    public void unlinkFisher(UUID target){
        linkedFishers.remove(target);
    }

    private int getLinkLimit() {
        if (trackedFor.hasPerk(FishingPerks.DOUBLE_LINK)) {
            return 2;
        }
        if (trackedFor.hasPerk(FishingPerks.FISHERMAN_LINK)) {
            return 1;
        }
        return 0;
    }

    public void linkedFisher(UUID target){
        int linkLimit = getLinkLimit();
        if (linkLimit == 0) {
            return;
        }
        if (linkedFishers.size() == linkLimit) {
            linkedFishers.remove(0);
        }
        linkedFishers.add(target);
    }

    @Override
    public void readNbt(NbtCompound nbtCompound) {
        linkedFishers = new ArrayList<>();
        NbtList uuidListTag = nbtCompound.getList(TAG, NbtElement.STRING_TYPE);
        for(int i = 0; i < uuidListTag.size(); i++) {
            linkedFishers.add(UUID.fromString(uuidListTag.getString(i)));//Untested conversion from string to uuid
        }
    }

    @Override
    public void writeNbt(NbtCompound nbtCompound) {
        NbtList linkedListTag = new NbtList();
        for(UUID linkedUUID : linkedFishers) {
            linkedListTag.add(NbtString.of(linkedUUID.toString()));
        }
        nbtCompound.put(TAG, linkedListTag);
    }
}
