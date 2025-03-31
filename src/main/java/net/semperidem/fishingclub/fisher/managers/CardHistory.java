package net.semperidem.fishingclub.fisher.managers;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.Text;
import net.minecraft.util.math.ChunkPos;
import net.semperidem.fishingclub.entity.IHookEntity;
import net.semperidem.fishingclub.fish.specimen.SpecimenData;
import net.semperidem.fishingclub.fisher.Card;
import net.semperidem.fishingclub.fisher.tradesecret.TradeSecrets;
import net.semperidem.fishingclub.util.ResourceUtil;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class CardHistory extends CardData {
    private static final long DAY_LENGTH = 24000;
    private static final float DAYS_SINCE_LAST_FISH_PERIOD = 4;

    private String issuedDate;
    private final ArrayList<Chunk> usedChunks = new ArrayList<>();
    private long lastCatchTime = 0;
    private long firstCatchOfTheDay = 0;
    private ItemStack lastUsedBait = ItemStack.EMPTY;
    private final ArrayList<ItemStack> unclaimedRewards = new ArrayList<>();
    private final HashMap<String, AtlasEntry> fishAtlas = new HashMap<>();
    private final HashSet<Integer> heardMessageIndexSet = new HashSet<>();

    public CardHistory(Card trackedFor) {
        super(trackedFor);
    }

    public void hearMessage() {
        HashSet<Integer> unheardMessageSet = new HashSet<>(ResourceUtil.MESSAGE_IN_BOTTLE.keySet());
        unheardMessageSet.removeAll(heardMessageIndexSet);
        ArrayList<Integer> unheardMessageList = new ArrayList<>(unheardMessageSet);
        int unheardMessageIndex = unheardMessageList.get(card.getRandom().nextInt(unheardMessageList.size()));
        card.owner().sendMessage(Text.of(ResourceUtil.MESSAGE_IN_BOTTLE.get(unheardMessageIndex)), true);
        heardMessageIndexSet.add(unheardMessageIndex);
    }

    public boolean isMember() {
        return !this.heardMessageIndexSet.isEmpty();
    }

    public HashMap<String, AtlasEntry> getFishAtlas() {
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

    private void recordFishCaught(SpecimenData fish) {
        String speciesName = fish.species().name();
        AtlasEntry atlasEntry = new AtlasEntry(speciesName);
        if (fishAtlas.containsKey(speciesName)) {
            atlasEntry = fishAtlas.get(speciesName);
        }
        atlasEntry.record(fish);
        fishAtlas.put(speciesName, atlasEntry);
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
        this.card.useTradeSecret(TradeSecrets.FIRST_CATCH_BUFF_CATCH_RATE, null);
        this.card.useTradeSecret(TradeSecrets.FIRST_CATCH_BUFF_QUALITY, null);
    }

    public ItemStack getLastUsedBait() {
        return lastUsedBait;
    }

    public boolean isFirstCatchOfTheDay() {
        return getCurrentTime() + DAY_LENGTH > firstCatchOfTheDay;
    }

    public boolean isFirstCatchInChunk(IHookEntity caughtWith) {
        if (!this.card.knowsTradeSecret(TradeSecrets.CHANGE_OF_SCENERY)) {
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

    public int minQualityIncrement(IHookEntity caughtWith, CardProgression progressionManager) {
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
        if (card.owner() == null) {
            return 0;
        }
        return card.owner().getWorld().getTime();
    }

    public String getIssuedDate(){
        return issuedDate;
    }

    @Override
    public void readNbt(NbtCompound nbtCompound, RegistryWrapper.WrapperLookup wrapperLookup) {
        NbtCompound historyTag = nbtCompound.getCompoundOrEmpty(TAG);
        issuedDate = historyTag.getString(ISSUED_DATE, LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        NbtList usedChunksTag = historyTag.getListOrEmpty(USED_CHUNKS_TAG);
        usedChunks.clear();
        usedChunksTag.forEach(chunk -> usedChunks.add(new Chunk((NbtCompound) chunk)));
        lastCatchTime = historyTag.getLong(LAST_CATCH_TIME_TAG, 0);
        firstCatchOfTheDay = historyTag.getLong(FIRST_CATCH_OF_THE_DAY_TAG, 0);
        if (historyTag.contains(LAST_USED_BAIT_TAG)) {
            lastUsedBait = ItemStack.fromNbt(wrapperLookup, historyTag.getCompoundOrEmpty(LAST_USED_BAIT_TAG)).orElse(ItemStack.EMPTY);
        }
        NbtList unclaimedRewardsTag = historyTag.getListOrEmpty(UNCLAIMED_REWARDS_TAG);
        unclaimedRewardsTag.forEach(rewardTag -> unclaimedRewards.add(ItemStack.fromNbt(wrapperLookup, rewardTag).orElse(ItemStack.EMPTY)));
        NbtList fishAtlasTag = historyTag.getListOrEmpty(FISH_ATLAS_TAG);
        fishAtlasTag.forEach(speciesTag -> {
            AtlasEntry stat = new AtlasEntry((NbtCompound) speciesTag);
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
            historyTag.put(LAST_USED_BAIT_TAG, lastUsedBait.toNbt(wrapperLookup));
        }
        NbtList unclaimedRewardsTag = new NbtList();
        unclaimedRewards.forEach(reward -> unclaimedRewardsTag.add(reward.toNbt(wrapperLookup)));
        historyTag.put(UNCLAIMED_REWARDS_TAG, unclaimedRewardsTag);
        NbtList fishAtlasTag = new NbtList();
        fishAtlas.forEach((s, atlasEntry) -> fishAtlasTag.add(atlasEntry.toNbt()));
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
            this.x = nbtCompound.getInt(X_TAG,0);
            this.z = nbtCompound.getInt(Z_TAG, 0);
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
    private static final String ISSUED_DATE = "issued_date";
    private static final String FISH_ATLAS_TAG = "fish_atlas_tag";

}
