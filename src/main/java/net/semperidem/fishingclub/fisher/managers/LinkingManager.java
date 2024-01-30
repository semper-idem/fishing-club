package net.semperidem.fishingclub.fisher.managers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.text.Text;
import net.semperidem.fishingclub.FishingDatabase;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.fisher.perks.FishingPerks;

import java.util.ArrayList;
import java.util.UUID;

public class LinkingManager extends DataManager {
    private static final String TAG ="linked";
    private ArrayList<UUID> linkedFishers = new ArrayList<>();

    public LinkingManager(FishingCard trackedFor) {
        super(trackedFor);
    }

    public ArrayList<UUID> getLinkedFishers() {
        return linkedFishers;
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

    public void shareStatusEffect(StatusEffectInstance sei) {
        if (sei.getDuration() < 20) {
            return;
        }
        trackedFor.getHolder().addStatusEffect(sei);
        for(UUID linkedFisher : linkedFishers) {
            FishingDatabase.getCard(linkedFisher).shareStatusEffect(getWeakerEffect(sei));
        }
    }

    private StatusEffectInstance getWeakerEffect(StatusEffectInstance sei) {
        return new StatusEffectInstance(
                sei.getEffectType(),
                (int) (sei.getDuration() * 0.9f),
                sei.getAmplifier(),
                sei.isAmbient(),
                sei.shouldShowParticles(),
                sei.shouldShowIcon()
        );
    }

    public void shareBait(ItemStack baitToShare) {
        for(UUID linkedFisher : linkedFishers) {
            FishingDatabase.getCard(linkedFisher).setSharedBait(baitToShare.copy());
        }
    }

    public void linkTarget(Entity target){
        int linkLimit = getLinkLimit();
        if (linkLimit == 0) {
            return;
        }

        if (!(target instanceof PlayerEntity playerTarget)){
            messageFail();
            return;
        }

        UUID targetUUID = target.getUuid();
        if (linkedFishers.contains(targetUUID)) {
            linkedFishers.remove(targetUUID);
            messageUnlink(trackedFor.getHolder(), playerTarget);
            return;
        }

        if (linkedFishers.size() == linkLimit) {
            linkedFishers.remove(0);
            messageLimit();
        }
        linkedFishers.add(targetUUID);
        messageLink(trackedFor.getHolder(), playerTarget);
    }

    private void messageLink(PlayerEntity source, PlayerEntity target) {
        source.sendMessage(Text.of("[Fishing Club] Linked to: " + target.getDisplayName().getString()), true);
        target.sendMessage(Text.of("[Fishing Club]" + source.getDisplayName().getString() + " has linked to you"), true);

    }

    private void messageUnlink(PlayerEntity source, PlayerEntity target) {
        source.sendMessage(Text.of("[Fishing Club] Unlinked from: " + target.getDisplayName().getString()), true);
        target.sendMessage(Text.of("[Fishing Club]" + source.getDisplayName().getString() + " has unlinked from you"), true);
    }

    private void messageLimit(){
        trackedFor.getHolder().sendMessage(Text.of("[Fishing Club] Link limit reached, removing oldest entry"), true);
    }

    private void messageFail() {
        trackedFor.getHolder().sendMessage(Text.of("[Fishing Club] You can only link players"), true);
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
