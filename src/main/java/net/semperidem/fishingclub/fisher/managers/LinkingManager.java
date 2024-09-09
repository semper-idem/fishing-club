package net.semperidem.fishingclub.fisher.managers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.*;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.fisher.tradesecret.TradeSecrets;
import net.semperidem.fishingclub.item.fishing_rod.components.FishingRodCoreItem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

public class LinkingManager extends DataManager {
    private static final float SHARED_EFFECT_DURATION = 0.75f;
    private static final int MIN_LENGTH_TO_SHARE = 200;

    private ArrayList<UUID> linkedFishers = new ArrayList<>();

    public LinkingManager(FishingCard trackedFor) {
        super(trackedFor);
    }

    private int linkLimit() {
        return this.trackedFor.tradeSecretLevel(TradeSecrets.FISHERMAN_LINK);
    }

    public void shareStatusEffect(StatusEffectInstance sei, LivingEntity source, HashSet<UUID> sharedWith) {
        if (!sei.getEffectType().value().isBeneficial()) {
            return;
        }

        if (sei.getDuration() < MIN_LENGTH_TO_SHARE) {
            return;
        }

        if (this.trackedFor.holder() == source) {
            return;
        }
        this.linkedFishers.stream()
                .filter(linkedFishersUUID -> !sharedWith.contains(linkedFishersUUID))
                .forEach(linkedFisher -> {
                    MinecraftServer server = trackedFor.holder().getServer();
                    if (server == null) {
                        return;
                    }
                    PlayerEntity linkedFisherPlayer = server.getPlayerManager().getPlayer(linkedFisher);
                    if (linkedFisherPlayer == null) {
                        return;
                    }

                    if (!(linkedFisherPlayer.getMainHandStack().getItem() instanceof FishingRodCoreItem)){
                        return;
                    }

                    StatusEffectInstance weakerStatusEffectInstance = this.getWeakerEffect(sei);
                    linkedFisherPlayer.addStatusEffect(weakerStatusEffectInstance);
                    sharedWith.add(linkedFisher);
                    FishingCard.of(linkedFisherPlayer).shareStatusEffect(this.getWeakerEffect(sei), source, sharedWith);
                });
    }

    private StatusEffectInstance getWeakerEffect(StatusEffectInstance sei) {
        return new StatusEffectInstance(
                sei.getEffectType(),
                (int) (sei.getDuration() * SHARED_EFFECT_DURATION),
                sei.getAmplifier(),
                sei.isAmbient(),
                sei.shouldShowParticles(),
                sei.shouldShowIcon()
        );
    }

    public void linkTarget(Entity target) {
        int linkLimit = linkLimit();
        if (linkLimit == 0) {
            return;
        }

        if (!(target instanceof PlayerEntity playerTarget)) {
            messageFail();
            return;
        }

        UUID targetUUID = target.getUuid();
        if (linkedFishers.contains(targetUUID)) {
            linkedFishers.remove(targetUUID);
            messageUnlink(trackedFor.holder(), playerTarget);
            this.sync();
            return;
        }

        if (linkedFishers.size() == linkLimit) {
            messageLimit();
            return;
        }
        linkedFishers.add(targetUUID);
        messageLink(trackedFor.holder(), playerTarget);
        sync();
    }

    public void requestSummon() {
        linkedFishers.forEach(this::requestSummonLink);
    }

    private void requestSummonLink(UUID linkedFisherUUID) {
        PlayerEntity linkedFisher = trackedFor.holder().getWorld().getPlayerByUuid(linkedFisherUUID);
        if (linkedFisher == null) {
            return;
        }
        FishingCard.of(linkedFisher).setSummonRequest((ServerPlayerEntity) trackedFor.holder());
        messageRequestSummon(linkedFisher);
        sync();
    }


    //TODO review messages
    private void messageRequestSummon(PlayerEntity target) {
        target.sendMessage(Text.of("[Fishing Club] Your fishing friend:" + trackedFor.holder().getDisplayName().getString() + " send you summon request, You have 30s to accept"), false);
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

    private void messageLimit() {
        trackedFor.holder().sendMessage(Text.of("[Fishing Club] Link limit reached, Cast  Link  on already linked player to remove it or type command /fishing-club link PLAYER_NAME"), true);
    }

    private void messageFail() {
        trackedFor.holder().sendMessage(Text.of("[Fishing Club] You can only link with players"), true);
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

    private static final String TAG = "linked";
}
