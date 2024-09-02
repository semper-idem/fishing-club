package net.semperidem.fishingclub.fisher.managers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.*;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.fisher.perks.TradeSecrets;

import java.util.ArrayList;
import java.util.UUID;

public class LinkingManager extends DataManager {
    private static final int LINK_LIMIT = 1;
    private static final int DOUBLE_LINK_LIMIT = LINK_LIMIT * 2;
    private static final float SHARED_EFFECT_LENGTH = 0.9f;
    private static final int MIN_LENGTH_TO_SHARE = 20;

    private ArrayList<UUID> linkedFishers = new ArrayList<>();

    public LinkingManager(FishingCard trackedFor) {
        super(trackedFor);
    }

    private int getLinkLimit() {
        if (trackedFor.hasPerk(TradeSecrets.DOUBLE_LINK)) {
            return DOUBLE_LINK_LIMIT;
        }
        if (trackedFor.hasPerk(TradeSecrets.FISHERMAN_LINK)) {
            return LINK_LIMIT;
        }
        return 0;
    }

    public void shareStatusEffect(StatusEffectInstance sei, LivingEntity source) {
        if (sei.getDuration() < MIN_LENGTH_TO_SHARE) {
            return;
        }
        if (trackedFor.getHolder() != source) {
            trackedFor.getHolder().addStatusEffect(sei);
        }
        linkedFishers.forEach(linkedFisher -> FishingCard.of(
                        trackedFor
                                .getHolder()
                                .getServer()
                                .getPlayerManager()
                                .getPlayer(linkedFisher)
                ).shareStatusEffect(getWeakerEffect(sei), source)//this is prob too complicated todo verify
        );
    }

    private StatusEffectInstance getWeakerEffect(StatusEffectInstance sei) {
        return new StatusEffectInstance(
                sei.getEffectType(),
                (int) (sei.getDuration() * SHARED_EFFECT_LENGTH),
                sei.getAmplifier(),
                sei.isAmbient(),
                sei.shouldShowParticles(),
                sei.shouldShowIcon()
        );
    }

    public void shareBait(ItemStack baitToShare) {
        linkedFishers.forEach(linkedFisher -> FishingCard.of(
                trackedFor
                        .getHolder()
                        .getServer()
                        .getPlayerManager()
                        .getPlayer(linkedFisher)
        ).setSharedBait(baitToShare.copy()));
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
        sync();
    }

    public void requestSummon() {
        linkedFishers.forEach(this::requestSummonLink);
    }

    private void requestSummonLink(UUID linkedFisherUUID) {
        PlayerEntity linkedFisher = trackedFor.getHolder().getWorld().getPlayerByUuid(linkedFisherUUID);
        if (linkedFisher == null) {
            return;
        }
        FishingCard.of(linkedFisher).setSummonRequest((ServerPlayerEntity) trackedFor.getHolder());
        messageRequestSummon(linkedFisher);
        sync();
    }


    //TODO
    private void messageRequestSummon(PlayerEntity target) {
        target.sendMessage(Text.of("[Fishing Club] Your friend:" + trackedFor.getHolder().getDisplayName().getString() + " send you summon request, You have 30s to accept"), false);
        //change summon_accpept to just tp PLAYER_NAM to SUMMONER_NAME
        target.sendMessage(MutableText.of(new PlainTextContent.Literal("Accept?")).setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/fishing-club summon_accept"))), false);

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
    public void readNbt(NbtCompound nbtCompound, RegistryWrapper.WrapperLookup wrapperLookup) {
        if (!nbtCompound.contains(TAG)) {
            return;
        }
        linkedFishers = new ArrayList<>();
        NbtList linkedFishersNbt = nbtCompound.getList(TAG, NbtElement.STRING_TYPE);
        linkedFishersNbt.forEach(nbtElement -> linkedFishers.add(UUID.fromString(nbtElement.asString())));
    }

    @Override
    public void writeNbt(NbtCompound nbtCompound, RegistryWrapper.WrapperLookup wrapperLookup) {
        NbtList linkedFishersNbt = new NbtList();
        linkedFishers.forEach(linkedFisher -> linkedFishersNbt.add(NbtString.of(linkedFisher.toString())));
        nbtCompound.put(TAG, linkedFishersNbt);
    }

    private static final String TAG ="linked";
}
