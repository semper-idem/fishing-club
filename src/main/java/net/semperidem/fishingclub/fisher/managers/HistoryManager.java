package net.semperidem.fishingclub.fisher.managers;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.math.ChunkPos;
import net.semperidem.fishingclub.entity.FishermanEntity;
import net.semperidem.fishingclub.entity.IHookEntity;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.fisher.perks.FishingPerks;
import net.semperidem.fishingclub.item.fishing_rod.FishingRodPartController;
import net.semperidem.fishingclub.item.fishing_rod.FishingRodPartType;
import net.semperidem.fishingclub.registry.StatusEffectRegistry;

import java.util.ArrayList;
import java.util.List;

public class HistoryManager extends DataManager {
    private static final long DAY_LENGTH = 24000;
    private static final float DAYS_SINCE_LAST_FISH_PERIOD = 4;

    private final ArrayList<Chunk> usedChunks = new ArrayList<>();
    private long lastCatchTime = 0;
    private long firstCatchOfTheDay = 0;
    private boolean firstCatchInChunk = false;
    private ItemStack lastUsedBait = ItemStack.EMPTY;
    private final ArrayList<String> derekMet = new ArrayList<>();
    private boolean gaveDerekFish = false;
    private String invitingPlayerName;

    public HistoryManager(FishingCard trackedFor) {
        super(trackedFor);
    }

    public void meetDerek(FishermanEntity.SummonType summonType) {
        derekMet.add(summonType.name());
    }

    public void giveDerekFish(){
        gaveDerekFish = true;
    }

    public void setInvitingPlayerName(String inviter) {
        this.invitingPlayerName = inviter;
    }

    public String getInvitingPlayerName() {
        return invitingPlayerName;
    }

    public boolean metDerek(FishermanEntity.SummonType summonType) {
        return derekMet.contains(summonType.name());
    }

    public boolean gaveDerekFish() {
        return gaveDerekFish;
    }

    public void fishHooked(IHookEntity hookEntity) {
        lastUsedBait = FishingRodPartController.getPart(hookEntity.getCaughtUsing(), FishingRodPartType.BAIT);
        checkChunk(hookEntity.getFishedInChunk());
    }

    public void fishCaught() {
        if (isFirstCatchOfTheDay()) {
            return;
        }
        firstCatchOfTheDay = getCurrentTime();
        if (trackedFor.hasPerk(FishingPerks.FREQUENT_CATCH_FIRST_CATCH)) {
            trackedFor.getHolder().addStatusEffect(new StatusEffectInstance(StatusEffectRegistry.FREQUENCY_BUFF,1200));
        }
        if (trackedFor.hasPerk(FishingPerks.QUALITY_INCREASE_FIRST_CATCH)) {
            trackedFor.getHolder().addStatusEffect(new StatusEffectInstance(StatusEffectRegistry.QUALITY_BUFF,1200));
        }
    }

    public ItemStack getLastUsedBait() {
        return lastUsedBait;
    }

    public boolean isFirstCatchOfTheDay() {
        return getCurrentTime() + DAY_LENGTH > firstCatchOfTheDay;
    }

    public boolean isFirstCatchInChunk() {
        return firstCatchInChunk;
    }

    public int getDaysSinceLastCatch() {
        return (int) Math.floor((lastCatchTime - getCurrentTime()) / (1f * DAY_LENGTH));
    }

    public int getMinGrade(ProgressionManager progressionManager) {
        int minGrade = 0;
        if (isFirstCatchInChunk()) {
            minGrade++;
        }
        if (isFirstCatchOfTheDay()) {
            if (progressionManager.hasPerk(FishingPerks.FIRST_CATCH)) {
                minGrade++;
            }
            minGrade++;
        }
        int daysSinceLastFish = getDaysSinceLastCatch();
        if (progressionManager.hasPerk(FishingPerks.QUALITY_TIME_INCREMENT) && daysSinceLastFish > 0) {
            minGrade = (int) (minGrade + Math.floor(daysSinceLastFish / DAYS_SINCE_LAST_FISH_PERIOD));
            if (Math.random() < (1 / DAYS_SINCE_LAST_FISH_PERIOD) * (daysSinceLastFish % DAYS_SINCE_LAST_FISH_PERIOD)) {
                minGrade++;
            }
        }
        return minGrade;
    }


    private void checkChunk(ChunkPos chunkPos) {
        firstCatchInChunk = usedChunks.stream().anyMatch(usedChunk -> usedChunk.matches(chunkPos));
        usedChunks.add(Chunk.create(chunkPos));
    }

    private long getCurrentTime() {
        return trackedFor.getHolder().getWorld().getTime();
    }

    @Override
    public void readNbt(NbtCompound nbtCompound) {
        NbtCompound historyTag = nbtCompound.getCompound(TAG);
        NbtList tag = historyTag.getList(USED_CHUNKS_TAG, NbtElement.COMPOUND_TYPE);
        usedChunks.clear();
        tag.forEach(chunk -> usedChunks.add(new Chunk((NbtCompound) chunk)));
        lastCatchTime = historyTag.getLong(LAST_CATCH_TIME_TAG);
        firstCatchOfTheDay = historyTag.getLong(FIRST_CATCH_OF_THE_DAY_TAG);
        lastUsedBait = ItemStack.fromNbt(historyTag.getCompound(LAST_USED_BAIT_TAG));
        derekMet.clear();
        derekMet.addAll(List.of(historyTag.getString(DEREK_MET_TAG).split(";")));
        gaveDerekFish = historyTag.getBoolean(WELCOMED_DEREK_TAG);
        invitingPlayerName = historyTag.getString(INVITER_TAG);
    }

    @Override
    public void writeNbt(NbtCompound nbtCompound) {
        NbtCompound historyTag = new NbtCompound();
        NbtList nbtList = new NbtList();
        usedChunks.forEach(chunk -> nbtList.add(chunk.toNbt()));
        historyTag.put(USED_CHUNKS_TAG, nbtList);
        historyTag.putLong(LAST_CATCH_TIME_TAG, lastCatchTime);
        historyTag.putLong(FIRST_CATCH_OF_THE_DAY_TAG, firstCatchOfTheDay);
        historyTag.put(LAST_USED_BAIT_TAG, lastUsedBait.writeNbt(new NbtCompound()));
        historyTag.putString(DEREK_MET_TAG, String.join(";", derekMet));
        historyTag.putBoolean(WELCOMED_DEREK_TAG, gaveDerekFish);
        historyTag.putString(INVITER_TAG, invitingPlayerName);
        nbtCompound.put(TAG, historyTag);
    }

    private static class Chunk implements NbtData{
        int x;
        int z;

        private Chunk(NbtCompound chunkTag) {
            readNbt(chunkTag);
        }
        private Chunk() {}
        public static Chunk create(ChunkPos chunkPos) {
            Chunk chunk = new Chunk();
            chunk.x = chunkPos.x;
            chunk.z = chunkPos.z;
            return chunk;
        }

        public boolean matches(ChunkPos chunkPos){
            return chunkPos.x == x && chunkPos.z == z;
        }

        private NbtCompound toNbt() {
            NbtCompound chunkNbt = new NbtCompound();
            writeNbt(chunkNbt);
            return chunkNbt;
        }

        @Override
        public void readNbt(NbtCompound nbtCompound) {
            this.x = nbtCompound.getInt(X_TAG);
            this.z = nbtCompound.getInt(Z_TAG);
        }

        @Override
        public void writeNbt(NbtCompound nbtCompound) {
            nbtCompound.putInt(X_TAG, x);
            nbtCompound.putInt(Z_TAG, z);
        }
    }


    private static final String TAG = "history";
    private static final String USED_CHUNKS_TAG = "used_chunks";
    private static final String FIRST_CATCH_OF_THE_DAY_TAG = "first_catch_of_the_day";
    private static final String LAST_CATCH_TIME_TAG = "last_catch_time";
    private static final String LAST_USED_BAIT_TAG = "last_used_bait";
    private static final String X_TAG = "x";
    private static final String Z_TAG = "z";
    private static final String DEREK_MET_TAG = "derek_met";
    private static final String WELCOMED_DEREK_TAG = "welcomed_derek";
    private static final String INVITER_TAG = "invited_by";
}
