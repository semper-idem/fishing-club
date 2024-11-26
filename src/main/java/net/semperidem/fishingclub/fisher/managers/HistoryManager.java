package net.semperidem.fishingclub.fisher.managers;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.Text;
import net.minecraft.util.math.ChunkPos;
import net.semperidem.fishingclub.entity.FishermanEntity;
import net.semperidem.fishingclub.entity.IHookEntity;
import net.semperidem.fishingclub.fish.specimen.SpecimenData;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.fisher.tradesecret.TradeSecrets;
import net.semperidem.fishingclub.util.ResourceUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
    private final HashSet<Integer> heardMessageIndexSet = new HashSet<>();

    public HistoryManager(FishingCard trackedFor) {
        super(trackedFor);
    }

    public void hearMessage() {
        HashSet<Integer> unheardMessageSet = new HashSet<>(ResourceUtil.MESSAGE_IN_BOTTLE.keySet());
        unheardMessageSet.removeAll(heardMessageIndexSet);
        ArrayList<Integer> unheardMessageList = new ArrayList<>(unheardMessageSet);
        int unheardMessageIndex = unheardMessageList.get(trackedFor.getRandom().nextInt(unheardMessageList.size()));
        trackedFor.holder().sendMessage(Text.of(ResourceUtil.MESSAGE_IN_BOTTLE.get(unheardMessageIndex)));
        heardMessageIndexSet.add(unheardMessageIndex);
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

    private void recordFishCaught(SpecimenData fish) {
        String speciesName = fish.species().name();
        SpeciesStatistics speciesStatistics = new SpeciesStatistics(speciesName);
        if (fishAtlas.containsKey(speciesName)) {
            speciesStatistics = fishAtlas.get(speciesName);
        }
        speciesStatistics.record(fish);
        fishAtlas.put(speciesName, speciesStatistics);
    }

    public void fishCaught(SpecimenData fish) {
        recordFishCaught(fish);
        firstCatchOfTheDay();
        this.sync();
    }

    private void firstCatchOfTheDay() {
        if (!isFirstCatchOfTheDay()) {
            return;
        }
        this.firstCatchOfTheDay = getCurrentTime();
        this.trackedFor.useTradeSecret(TradeSecrets.FIRST_CATCH_BUFF_CATCH_RATE, null);
        this.trackedFor.useTradeSecret(TradeSecrets.FIRST_CATCH_BUFF_QUALITY, null);
    }

    public ItemStack getLastUsedBait() {
        return lastUsedBait;
    }

    public boolean isFirstCatchOfTheDay() {
        return getCurrentTime() + DAY_LENGTH > firstCatchOfTheDay;
    }

    public boolean isFirstCatchInChunk(IHookEntity caughtWith) {
        if (!this.trackedFor.knowsTradeSecret(TradeSecrets.CHANGE_OF_SCENERY)) {
            return false;
        }
        if (!(caughtWith instanceof Entity caughtWithEntity)) {
            return false;
        }
        ChunkPos chunkPos = caughtWithEntity.getChunkPos();
        boolean firstCatchInChunk = usedChunks.stream().noneMatch(usedChunk -> usedChunk.matches(chunkPos));
        if (!firstCatchInChunk) {
            return false;
        }

        usedChunks.add(Chunk.create(chunkPos));
        this.sync();//checkChunk writes
        return true;
    }

    public int minQualityIncrement(IHookEntity caughtWith, ProgressionManager progressionManager) {
        int minGrade = 0;
        if (isFirstCatchInChunk(caughtWith)) {
            minGrade++;
        }
        if (isFirstCatchOfTheDay() && progressionManager.knowsTradeSecret(TradeSecrets.FIRST_CATCH)) {
            minGrade++;
        }
        return minGrade;
    }

    private long getCurrentTime() {
        if (trackedFor.holder() == null) {
            return 0;
        }
        return trackedFor.holder().getWorld().getTime();
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
