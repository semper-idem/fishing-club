package net.semperidem.fishingclub.fisher.managers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.*;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.fisher.FishingCardManager;
import net.semperidem.fishingclub.fisher.perks.FishingPerks;

import java.util.ArrayList;
import java.util.UUID;

public class LinkingManager extends DataManager {
    private static final String TAG ="linked";
    private ArrayList<UUID> linkedFishers = new ArrayList<>();

    public LinkingManager(FishingCard trackedFor) {
        super(trackedFor);
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
        PlayerManager playerManager = trackedFor.getHolder().getServer().getPlayerManager();
        for(UUID linkedFisher : linkedFishers) {
            FishingCardManager.getPlayerCard(playerManager.getPlayer(linkedFisher))
                    .shareStatusEffect(getWeakerEffect(sei));
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
        PlayerManager playerManager = trackedFor.getHolder().getServer().getPlayerManager();
        for(UUID linkedFisher : linkedFishers) {
            FishingCardManager.getPlayerCard(playerManager.getPlayer(linkedFisher)).setSharedBait(baitToShare.copy());
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

    public void requestSummon() {
        for(UUID linkedFisherUUID : linkedFishers) {
            requestSummonLink(linkedFisherUUID);
        }
    }

    private void requestSummonLink(UUID linkedFisherUUID) {
        PlayerEntity linkedFisher = trackedFor.getHolder().getWorld().getPlayerByUuid(linkedFisherUUID);
        if (linkedFisher == null) {
            return;
        }
        FishingCardManager.getPlayerCard(linkedFisher).setSummonRequest((ServerPlayerEntity) trackedFor.getHolder());
        messageRequestSummon(linkedFisher);
    }

    private void messageRequestSummon(PlayerEntity target) {
        target.sendMessage(Text.of("[Fishing Club] Your friend:" + trackedFor.getHolder().getDisplayName().getString() + " send you summon request, You have 30s to accept"), false);
        target.sendMessage(MutableText.of(new LiteralTextContent("Accept?")).setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/fishing-club summon_accept"))), false);

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
