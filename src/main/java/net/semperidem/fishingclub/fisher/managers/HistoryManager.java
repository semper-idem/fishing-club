package net.semperidem.fishingclub.fisher.managers;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.math.ChunkPos;
import net.semperidem.fishingclub.entity.IHookEntity;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.fisher.perks.FishingPerks;
import net.semperidem.fishingclub.item.fishing_rod.FishingRodPartController;
import net.semperidem.fishingclub.item.fishing_rod.FishingRodPartType;
import net.semperidem.fishingclub.registry.FStatusEffectRegistry;

import java.util.ArrayList;

public class HistoryManager extends DataManager {
    private static final String USED_CHUNKS_TAG = "used_chunks";
    private static final String FIRST_CATCH_OF_THE_DAY_TAG = "fcotd";
    private static final String LAST_CATCH_TIME_TAG = "lct";
    private static final String LAST_USED_BAIT_TAG = "lub";
    private static final String TAG = "history";
    private static final long DAY_LENGTH = 24000;

    private ArrayList<Chunk> usedChunks = new ArrayList<>();
    private long lastCatchTime = 0;
    private long firstCatchOfTheDay = 0;
    private boolean firstCatchInChunk = false;
    private ItemStack lastUsedBait = ItemStack.EMPTY;

    public HistoryManager(FishingCard trackedFor) {
        super(trackedFor);
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
            trackedFor.getHolder().addStatusEffect(new StatusEffectInstance(FStatusEffectRegistry.FREQUENCY_BUFF,1200));
        }
        if (trackedFor.hasPerk(FishingPerks.QUALITY_INCREASE_FIRST_CATCH)) {
            trackedFor.getHolder().addStatusEffect(new StatusEffectInstance(FStatusEffectRegistry.QUALITY_BUFF,1200));
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


    private void checkChunk(ChunkPos chunkPos) {
        Chunk chunk = Chunk.create(chunkPos);
        for(Chunk usedChunk : usedChunks) {
            if (usedChunk.matches(chunkPos)) {
                firstCatchInChunk = false;
            }
        }//todo streams
        usedChunks.add(chunk);
        firstCatchInChunk = true;
    }

    private long getCurrentTime() {
        return trackedFor.getHolder().getWorld().getTime();
    }

    @Override
    public void readNbt(NbtCompound nbtCompound) {
        NbtCompound historyTag = nbtCompound.getCompound(TAG);
        NbtList tag = historyTag.getList(USED_CHUNKS_TAG, NbtElement.COMPOUND_TYPE);
        usedChunks = new ArrayList<>();
        tag.forEach(chunk -> usedChunks.add(new Chunk((NbtCompound) chunk)));
        lastCatchTime = historyTag.getLong(LAST_CATCH_TIME_TAG);
        firstCatchOfTheDay = historyTag.getLong(FIRST_CATCH_OF_THE_DAY_TAG);
        lastUsedBait = ItemStack.fromNbt(historyTag.getCompound(LAST_USED_BAIT_TAG));
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
        nbtCompound.put(TAG, historyTag);
    }

    private static class Chunk implements NbtData{
        private static final String X_TAG = "x";
        private static final String Z_TAG = "z";
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

}
