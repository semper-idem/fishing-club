package net.semperidem.fishingclub.fisher.managers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.*;
import net.semperidem.fishingclub.fisher.Card;
import net.semperidem.fishingclub.fisher.tradesecret.TradeSecrets;
import net.semperidem.fishingclub.item.fishing_rod.components.FishingRodCoreItem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

public class CardLinking extends CardData {
    private static final float SHARED_EFFECT_DURATION = 0.75f;
    private static final int MIN_LENGTH_TO_SHARE = 200;
    private static final int REQUEST_VALID_TIME = 600;
    public String targetUUID = "";
    public long requestTick = 0L;


    private ArrayList<UUID> linked = new ArrayList<>();

    public CardLinking(Card trackedFor) {
        super(trackedFor);
    }

    private int linkLimit() {
        return this.card.tradeSecretLevel(TradeSecrets.LINK);
    }

    public void shareStatusEffect(StatusEffectInstance sei, LivingEntity source, HashSet<UUID> shared) {
        if (!sei.getEffectType().value().isBeneficial()) {
            return;
        }

        if (sei.getDuration() < MIN_LENGTH_TO_SHARE) {
            return;
        }

        if (this.card.owner() == source) {
            return;
        }
        MinecraftServer server = card.owner().getServer();
        if (server == null) {
            return;
        }
        PlayerManager playerManager = server.getPlayerManager();
        this.linked.stream()
                .filter(linkedUUID -> !shared.contains(linkedUUID))
                .forEach(linkedUUID -> {
                    PlayerEntity linked = playerManager.getPlayer(linkedUUID);
                    if (linked == null) {
                        return;
                    }

                    if (!(linked.getMainHandStack().getItem() instanceof FishingRodCoreItem)){
                        return;
                    }

                    StatusEffectInstance weakerSEI = this.getWeakerEffect(sei);
                    linked.addStatusEffect(weakerSEI);
                    shared.add(linkedUUID);
                    Card.of(linked).shareStatusEffect(this.getWeakerEffect(sei), source, shared);
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
        if (linked.contains(targetUUID)) {
            linked.remove(targetUUID);
            messageUnlink(card.owner(), playerTarget);
            this.sync();
            return;
        }

        if (linked.size() == linkLimit) {
            messageLimit();
            return;
        }
        linked.add(targetUUID);
        messageLink(card.owner(), playerTarget);
        sync();
    }

    public void requestSummon() {
        linked.forEach(this::requestSummonLink);
    }

    private void requestSummonLink(UUID linkedFisherUUID) {
        PlayerEntity linkedFisher = card.owner().getWorld().getPlayerByUuid(linkedFisherUUID);
        if (linkedFisher == null) {
            return;
        }
        Card.of(linkedFisher).setSummonRequest((ServerPlayerEntity) card.owner());
        messageRequestSummon(linkedFisher);
        sync();
    }


    //TODO review messages
    private void messageRequestSummon(PlayerEntity target) {
        target.sendMessage(Text.of("[Fishing Club] Your fishing friend:" + card.owner().getDisplayName().getString() + " send you summon request, You have 30s to accept"), false);
        target.sendMessage(MutableText.of(new PlainTextContent.Literal("Accept?")).setStyle(Style.EMPTY.withClickEvent(new ClickEvent.RunCommand("/fishingclub summon_accept"))), false);

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
        card.owner().sendMessage(Text.of("[Fishing Club] Link limit reached, Cast  Link  on already linked player to remove it or type command /fishingclub link PLAYER_NAME"), true);
    }

    private void messageFail() {
        card.owner().sendMessage(Text.of("[Fishing Club] You can only link with players"), true);
    }


    public void set(ServerPlayerEntity target) {
        targetUUID = target.getUuidAsString();
        requestTick = target.getWorld().getTime();
        sync();
    }

    public boolean canAccept() {
        return card.owner().getWorld().getTime() - requestTick < REQUEST_VALID_TIME;
    }

    public boolean isTarget(ServerPlayerEntity possibleTarget) {
        return possibleTarget.getUuidAsString().equalsIgnoreCase(targetUUID);
    }

    public void execute() {
        if (!(card.owner() instanceof ServerPlayerEntity source)) {
            return;
        }
        if (!canAccept()) {
            return;
        }
        targetUUID = "";
        requestTick = 0;
        source.server.getPlayerManager().getPlayerList().stream()
                .filter(Objects::nonNull)
                .filter(this::isTarget)
                .findAny()
                .ifPresent(target -> teleport(source, target));
    }

    private static void teleport(ServerPlayerEntity source, ServerPlayerEntity target) {
        source.teleport(
                target.getX(),
                target.getY(),
                target.getZ(),
                true
        );
    }

    @Override
    public void readNbt(NbtCompound nbtCompound, RegistryWrapper.WrapperLookup wrapperLookup) {
        if (!nbtCompound.contains(TAG)) {
            return;
        }
        linked = new ArrayList<>();
        NbtList linkedFishersNbt = nbtCompound.getListOrEmpty(TAG);
        linkedFishersNbt.forEach(nbtElement -> linked.add(UUID.fromString(String.valueOf(nbtElement))));
        if (!nbtCompound.contains(TAG)) {
            return;
        }
        NbtCompound summonTag = nbtCompound.getCompoundOrEmpty(TAG);
        targetUUID = summonTag.getString(TARGET_TAG, "");
        requestTick = summonTag.getLong(REQUEST_TICK_TAG, 0);


    }

    @Override
    public void writeNbt(NbtCompound nbtCompound, RegistryWrapper.WrapperLookup wrapperLookup) {
        NbtList linkedFishersNbt = new NbtList();
        linked.forEach(linkedUUID -> linkedFishersNbt.add(NbtString.of(linkedUUID.toString())));
        nbtCompound.put(TAG, linkedFishersNbt);
        NbtCompound summonTag = new NbtCompound();
        summonTag.putString(TARGET_TAG, targetUUID);
        summonTag.putLong(REQUEST_TICK_TAG, requestTick);
        nbtCompound.put(TAG, summonTag);
    }

    private static final String TAG = "linked";
    private static final String TARGET_TAG = "target_UUID";
    private static final String REQUEST_TICK_TAG = "request_tick";


}
