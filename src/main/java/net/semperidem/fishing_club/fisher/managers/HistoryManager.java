package net.semperidem.fishing_club.fisher.managers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.ChunkPos;
import net.semperidem.fishing_club.entity.FishermanEntity;
import net.semperidem.fishing_club.entity.IHookEntity;
import net.semperidem.fishing_club.fish.FishComponent;
import net.semperidem.fishing_club.fisher.FishingCard;
import net.semperidem.fishing_club.fisher.perks.FishingPerks;
import net.semperidem.fishing_club.registry.FCStatusEffects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HistoryManager extends DataManager {
    private static final long DAY_LENGTH = 24000;
    private static final float DAYS_SINCE_LAST_FISH_PERIOD = 4;

    private final ArrayList<Chunk> usedChunks = new ArrayList<>();
    private long lastCatchTime = 0;
    private long firstCatchOfTheDay = 0;
    private ItemStack lastUsedBait = ItemStack.EMPTY;
    private final ArrayList<String> derekMet = new ArrayList<>();
    private boolean gaveDerekFish = false;
    private final ArrayList<ItemStack> unclaimedRewards = new ArrayList<>();
    private final HashMap<String, SpeciesStatistics> fishAtlas = new HashMap<>();

    public HistoryManager(FishingCard trackedFor) {
        super(trackedFor);
    }

    public void meetDerek(FishermanEntity.SummonType summonType) {
        derekMet.add(summonType.name());
        sync();
    }

    public void giveDerekFish(){
        this.gaveDerekFish = true;
        sync();
    }

    public HashMap<String, SpeciesStatistics> getFishAtlas() {
        return fishAtlas;
    }

    public void addUnclaimedReward(ItemStack rewardStack) {
        unclaimedRewards.add(rewardStack);
        sync();
    }

    public void claimReward(ItemStack rewardStack) {
        if (!(unclaimedRewards.contains(rewardStack))) {
            return;
        }
        unclaimedRewards.remove(rewardStack);
        sync();
    }

    public ArrayList<ItemStack> getUnclaimedRewards() {
        return unclaimedRewards;
    }

    public boolean metDerek(FishermanEntity.SummonType summonType) {
        return derekMet.contains(summonType.name());
    }

    public boolean gaveDerekFish() {
        return gaveDerekFish;
    }

    private void recordFishCaught(FishComponent fish) {
        String speciesName = fish.speciesName();
        SpeciesStatistics speciesStatistics = new SpeciesStatistics(speciesName);
        if (fishAtlas.containsKey(speciesName)) {
            speciesStatistics = fishAtlas.get(speciesName);
        }
        speciesStatistics.record(fish);
        fishAtlas.put(speciesName, speciesStatistics);
        sync(); //technically not needed cause we always call "fishCaught"
    }

    public void fishCaught(FishComponent fish) {
        recordFishCaught(fish);
        if (isFirstCatchOfTheDay()) {
            return;
        }
        firstCatchOfTheDay = getCurrentTime();
        if (trackedFor.hasPerk(FishingPerks.FREQUENT_CATCH_FIRST_CATCH)) {
            trackedFor.getHolder().addStatusEffect(new StatusEffectInstance(FCStatusEffects.FREQUENCY_BUFF,1200));
        }
        if (trackedFor.hasPerk(FishingPerks.QUALITY_INCREASE_FIRST_CATCH)) {
            trackedFor.getHolder().addStatusEffect(new StatusEffectInstance(FCStatusEffects.QUALITY_BUFF,1200));
        }
        sync();
    }

    public ItemStack getLastUsedBait() {
        return lastUsedBait;
    }

    public boolean isFirstCatchOfTheDay() {
        return getCurrentTime() + DAY_LENGTH > firstCatchOfTheDay;
    }

    public boolean isFirstCatchInChunk(IHookEntity caughtWith) {
        if (!(caughtWith instanceof Entity caughtWithEntity)) {
            return false;
        }
        ChunkPos chunkPos = caughtWithEntity.getChunkPos();
        boolean firstCatchInChunk = usedChunks.stream().noneMatch(usedChunk -> usedChunk.matches(chunkPos));
        if (firstCatchInChunk) {
            usedChunks.add(Chunk.create(chunkPos));
            sync();//checkChunk writes
        }
        return firstCatchInChunk;
    }

    public int getDaysSinceLastCatch() {
        return (int) Math.floor((lastCatchTime - getCurrentTime()) / (1f * DAY_LENGTH));
    }

    public int getMinGrade(IHookEntity caughtWith, ProgressionManager progressionManager) {
        int minGrade = 0;
        if (isFirstCatchInChunk(caughtWith)) {
            minGrade++;
        }
        if (isFirstCatchOfTheDay()) {
            if (progressionManager.hasPerk(FishingPerks.FIRST_CATCH)) {
                minGrade++;
            }
            minGrade++;
        }
        int daysSinceLastFishComponent = getDaysSinceLastCatch();
        if (progressionManager.hasPerk(FishingPerks.QUALITY_TIME_INCREMENT) && daysSinceLastFishComponent > 0) {
            minGrade = (int) (minGrade + Math.floor(daysSinceLastFishComponent / DAYS_SINCE_LAST_FISH_PERIOD));
            if (Math.random() < (1 / DAYS_SINCE_LAST_FISH_PERIOD) * (daysSinceLastFishComponent % DAYS_SINCE_LAST_FISH_PERIOD)) {
                minGrade++;
            }
        }
        return minGrade;
    }

    private long getCurrentTime() {
        return trackedFor.getHolder().getWorld().getTime();
    }

    @Override
    public void readNbt(NbtCompound nbtCompound, RegistryWrapper.WrapperLookup wrapperLookup) {
        NbtCompound historyTag = nbtCompound.getCompound(TAG);
        NbtList usedChunksTag = historyTag.getList(USED_CHUNKS_TAG, NbtElement.COMPOUND_TYPE);
        usedChunks.clear();
        usedChunksTag.forEach(chunk -> usedChunks.add(new Chunk((NbtCompound) chunk)));
        lastCatchTime = historyTag.getLong(LAST_CATCH_TIME_TAG);
        firstCatchOfTheDay = historyTag.getLong(FIRST_CATCH_OF_THE_DAY_TAG);
        if (historyTag.contains(LAST_USED_BAIT_TAG)) {
            lastUsedBait = ItemStack.fromNbt(wrapperLookup, historyTag.getCompound(LAST_USED_BAIT_TAG)).orElse(ItemStack.EMPTY);
        }
        derekMet.clear();
        derekMet.addAll(List.of(historyTag.getString(DEREK_MET_TAG).split(";")));
        gaveDerekFish = historyTag.getBoolean(WELCOMED_DEREK_TAG);
        NbtList unclaimedRewardsTag = historyTag.getList(UNCLAIMED_REWARDS_TAG, NbtElement.COMPOUND_TYPE);
        unclaimedRewardsTag.forEach(rewardTag -> unclaimedRewards.add(ItemStack.fromNbt(wrapperLookup, rewardTag).orElse(ItemStack.EMPTY)));
        NbtList fishAtlasTag = historyTag.getList(FISH_ATLAS_TAG, NbtElement.COMPOUND_TYPE);
        fishAtlasTag.forEach(speciesTag -> {
            SpeciesStatistics stat = new SpeciesStatistics((NbtCompound) speciesTag);
            fishAtlas.put(stat.getName(), stat);
        });
    }

    @Override
    public void writeNbt(NbtCompound nbtCompound, RegistryWrapper.WrapperLookup wrapperLookup) {
        NbtCompound historyTag = new NbtCompound();
        NbtList usedChunksTag = new NbtList();
        usedChunks.forEach(chunk -> usedChunksTag.add(chunk.toNbt()));
        historyTag.put(USED_CHUNKS_TAG, usedChunksTag);
        historyTag.putLong(LAST_CATCH_TIME_TAG, lastCatchTime);
        historyTag.putLong(FIRST_CATCH_OF_THE_DAY_TAG, firstCatchOfTheDay);
        if (!lastUsedBait.isEmpty()) {
            historyTag.put(LAST_USED_BAIT_TAG, lastUsedBait.encode(wrapperLookup));
        }
        historyTag.putString(DEREK_MET_TAG, String.join(";", derekMet));
        historyTag.putBoolean(WELCOMED_DEREK_TAG, gaveDerekFish);
        NbtList unclaimedRewardsTag = new NbtList();
        unclaimedRewards.forEach(reward -> unclaimedRewardsTag.add(reward.encode(wrapperLookup)));
        historyTag.put(UNCLAIMED_REWARDS_TAG, unclaimedRewardsTag);
        NbtList fishAtlasTag = new NbtList();
        fishAtlas.forEach((s, speciesStatistics) -> fishAtlasTag.add(speciesStatistics.toNbt()));
        historyTag.put(FISH_ATLAS_TAG, fishAtlasTag);
        nbtCompound.put(TAG, historyTag);
    }

    private static class Chunk {
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

        public void readNbt(NbtCompound nbtCompound) {
            this.x = nbtCompound.getInt(X_TAG);
            this.z = nbtCompound.getInt(Z_TAG);
        }

        public void writeNbt(NbtCompound nbtCompound) {
            nbtCompound.putInt(X_TAG, x);
            nbtCompound.putInt(Z_TAG, z);
        }
    }



    private static final String TAG = "history";
    private static final String USED_CHUNKS_TAG = "used_chunks";
    private static final String UNCLAIMED_REWARDS_TAG = "unclaimed_rewards";
    private static final String FIRST_CATCH_OF_THE_DAY_TAG = "first_catch_of_the_day";
    private static final String LAST_CATCH_TIME_TAG = "last_catch_time";
    private static final String LAST_USED_BAIT_TAG = "last_used_bait";
    private static final String X_TAG = "x";
    private static final String Z_TAG = "z";
    private static final String DEREK_MET_TAG = "derek_met";
    private static final String WELCOMED_DEREK_TAG = "welcomed_derek";
    private static final String FISH_ATLAS_TAG = "fish_atlas_tag";

}
