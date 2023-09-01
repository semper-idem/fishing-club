package net.semperidem.fishingclub.fisher;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.server.network.ServerPlayerEntity;
import net.semperidem.fishingclub.client.game.fish.Fish;
import net.semperidem.fishingclub.client.screen.fisher_info.FisherInfoScreenHandler;
import net.semperidem.fishingclub.fisher.perks.FishingPerk;
import net.semperidem.fishingclub.fisher.perks.FishingPerks;
import net.semperidem.fishingclub.fisher.perks.spells.SpellInstance;
import net.semperidem.fishingclub.fisher.perks.spells.Spells;
import net.semperidem.fishingclub.network.ServerPacketSender;
import net.semperidem.fishingclub.registry.FStatusEffectRegistry;
import net.semperidem.fishingclub.util.InventoryUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;

public class FisherInfo {
    public static final String TAG = "fisher_info";

    private static final int BASE_EXP = 50;
    private static final float EXP_EXPONENT = 1.25f;

    private int level = 1;
    private int exp = 0;
    private int credit = 0;
    private int skillPoints = 0;
    private long lastFishCaughtTime = 0;
    private long firstFishOfTheDayCaughtTime = 0;
    private HashMap<String, FishingPerk> perks = new HashMap<>();
    private SimpleInventory fisherInventory = new SimpleInventory(FisherInfoScreenHandler.SLOT_COUNT);
    private HashMap<FishingPerk, SpellInstance> spells = new HashMap<>();
    private ArrayList<Chunk> fishedChunks = new ArrayList<>();

    private PlayerEntity fisher;

    public FisherInfo(){
        initPerks();
    }
    public FisherInfo(int level){
        this();
        this.level = level;
    }

    public FisherInfo(PlayerEntity playerEntity, NbtCompound fisherTag) {
        this.fisher = playerEntity;
        fromNbt(fisherTag);
    }
    public FisherInfo(PlayerEntity playerEntity) {
        this.fisher = playerEntity;
        NbtCompound playerCustomTag = new NbtCompound();
        playerEntity.writeCustomDataToNbt(playerCustomTag);
        if (!playerCustomTag.contains(TAG)) return;
        fromNbt(playerCustomTag.getCompound(TAG));
    }

    public FisherInfo(NbtCompound fisherTag) {
        fromNbt(fisherTag);
    }

    public PlayerEntity getFisher(){
        return fisher;
    }
    public void tick(){
        for(SpellInstance spellInstance : spells.values()) {
            spellInstance.tick();
        }
    }
    public void fromNbt(NbtCompound fisherTag){
        this.level = fisherTag.getInt("level");
        this.exp = fisherTag.getInt("exp");
        this.credit = fisherTag.getInt("credit");
        this.skillPoints = fisherTag.getInt("skill_points");
        this.lastFishCaughtTime = fisherTag.getLong("last_fish_caught_time");
        this.firstFishOfTheDayCaughtTime = fisherTag.getLong("ffotd_caught_time");
        this.fisherInventory = InventoryUtil.readInventory(fisherTag.getCompound("inventory"));
        setPerks(fisherTag);
        setSpells(fisherTag);
        setChunks(fisherTag);
    }

    private void setChunks(NbtCompound fisherTag){
        fishedChunks.clear();
        NbtList chunkListTag = fisherTag.getList("fishedC_chunks", NbtElement.STRING_TYPE);
        for(int i = 0; i < chunkListTag.size(); i++) {
            fishedChunks.add(new Chunk(chunkListTag.getString(i)));
        }
    }

    public void setClientEntity(MinecraftClient client){
        this.fisher = client.player;
    }

    private void setPerks(NbtCompound fisherTag){
        this.perks.clear();
        NbtList perkListTag = fisherTag.getList("perks", NbtElement.STRING_TYPE);
        if (perkListTag.size() == 0) {
            initPerks();
        } else {
            perkListTag.forEach(
                    nbtElement -> FishingPerks.getPerkFromName(nbtElement.asString()).ifPresent(
                            fishingPerk -> this.perks.put(fishingPerk.getName(), fishingPerk)));
        }
    }

    private void setSpells(NbtCompound fisherTag){
        this.spells.clear();
        NbtList spellListTag = fisherTag.getList("spells", NbtElement.COMPOUND_TYPE);
        for(int i = 0; i < spellListTag.size(); i++) {
            NbtCompound spellTag = spellListTag.getCompound(i);
            String perkName = spellTag.getString("key");
            int cooldown = spellTag.getInt("cooldown");
            long nextCast = spellTag.getLong("nextCast");
            Optional<FishingPerk> optionalPerk = FishingPerks.getPerkFromName(perkName);
            if (optionalPerk.isEmpty()) continue;
            FishingPerk fishingPerk = optionalPerk.get();
            SpellInstance spellInstance = SpellInstance.getSpellInstance(fishingPerk, cooldown, nextCast);
            spells.put(fishingPerk, spellInstance);
        }
    }

    public Collection<SpellInstance> getSpells(){
        return this.spells.values();
    }

    public void writeNbt(NbtCompound playerTag){
        playerTag.put(TAG, toNbt());
    }

    public NbtCompound toNbt(){
        NbtCompound fisherTag = new NbtCompound();
        fisherTag.putInt("level", this.level);
        fisherTag.putInt("exp", this.exp);
        fisherTag.putInt("credit", this.credit);
        fisherTag.putInt("skill_points", this.skillPoints);
        fisherTag.putLong("last_fish_caught_time", this.lastFishCaughtTime);
        fisherTag.putLong("ffotd_caught_time", this.firstFishOfTheDayCaughtTime);
        fisherTag.put("inventory", InventoryUtil.writeInventory(this.fisherInventory));
        NbtList perkListTag = new NbtList();
        this.perks.forEach((fishingPerkName, fishingPerk) -> {
            perkListTag.add(NbtString.of(fishingPerkName));
        });
        fisherTag.put("perks", perkListTag);
        NbtList spellListTag = new NbtList();
        for(SpellInstance spellInstance : spells.values()) {
            NbtCompound spellTag = new NbtCompound();
            spellTag.putString("key", spellInstance.getKey());
            spellTag.putInt("cooldown", spellInstance.getCooldown());
            spellTag.putLong("nextCast", spellInstance.getNextCast());
            spellListTag.add(spellTag);
        }
        fisherTag.put("spells", spellListTag);

        NbtList fishedChunksTag = new NbtList();
        for(Chunk c : fishedChunks) {
            fishedChunksTag.add(NbtString.of(c.toString()));
        }
        fisherTag.put("fished_chunks", fishedChunksTag);

        return fisherTag;
    }

    public int getLevel() {
        return level;
    }

    public int getExp() {
        return exp;
    }

    public int getCredit(){
        return credit;
    }

    private void initPerks(){
        addRootPerk(FishingPerks.ROOT_HOBBYIST);
        addRootPerk(FishingPerks.ROOT_OPPORTUNIST);
        addRootPerk(FishingPerks.ROOT_SOCIALIST);
    }

    private void syncFisherInfo(){
        if (this.fisher == null) return;
        if (!(this.fisher instanceof ServerPlayerEntity)) return;
        NbtCompound playerCustomTag = new NbtCompound();
        this.fisher.writeCustomDataToNbt(playerCustomTag);
        playerCustomTag.put(TAG, toNbt());
        this.fisher.readCustomDataFromNbt(playerCustomTag);
        ServerPacketSender.sendFisherInfo((ServerPlayerEntity) this.fisher, this);
    }

    private void addRootPerk(FishingPerk perk){
        this.perks.put(perk.getName(), perk);
    }

    void addPerk(FishingPerk perk){
        if (availablePerk(perk) && hasSkillPoints()) {
            perk.onEarn(fisher);
            this.perks.put(perk.getName(), perk);
            if (Spells.perkHasSpell(perk)) {
                this.spells.put(perk, SpellInstance.getSpellInstance(perk, 0, fisher.world.getTime()));
            }
            skillPoints--;
            syncFisherInfo();
        }
    }

    public void addPerk(String perkName){
        FishingPerks.getPerkFromName(perkName).ifPresent(this::addPerk);
    }

    public boolean availablePerk(FishingPerk perk){
        return perk.getParent() == null || this.perks.containsKey(perk.getParent().getName());
    }

    private void addSkillPoint(){
        this.skillPoints++;
        syncFisherInfo();
    }

    public int getMinGrade(){
        int minGrade = 0;
        long worldTime = this.fisher.world.getTime();
        if (this.firstFishOfTheDayCaughtTime + 24000 < worldTime) {
            if (hasPerk(FishingPerks.FIRST_CATCH)) {
                minGrade++;
            }
            minGrade++;
        }
        if (this.fisher.hasStatusEffect(FStatusEffectRegistry.QUALITY_BUFF) && Math.random() > 0.25f) {
            minGrade++;
        } else if (this.fisher.hasStatusEffect(FStatusEffectRegistry.ONE_TIME_QUALITY_BUFF)){
            minGrade++;
        }

        if (hasPerk(FishingPerks.QUALITY_TIME_INCREMENT) && lastFishCaughtTime - worldTime > 24000) {
            int daysSinceLastFish = (int) ((lastFishCaughtTime - worldTime) / 24000f);
            while (daysSinceLastFish > 4) {
                minGrade++;
                daysSinceLastFish -= 4;
            }
            if (Math.random() < 0.25f * daysSinceLastFish) {
                minGrade++;
            }
        }
        return Math.min(4, minGrade);
    }

    public int getSkillPoints(){
        return this.skillPoints;
    }

    public boolean hasSkillPoints(){
        return this.skillPoints > 0;
    }

    void setSkillPoints(int skillPoints){
        this.skillPoints = skillPoints;
        syncFisherInfo();
    }

    public void useSpell(FishingPerk fishingPerk){
        if (!perks.containsKey(fishingPerk.getName())) return;
        SpellInstance spellInstance = spells.get(fishingPerk);
        spellInstance.use(fisher);
        spells.put(fishingPerk, spellInstance);
        syncFisherInfo();
    }

    void removePerk(String perkName){
        if (!this.perks.containsKey(perkName)) return;
        this.perks.remove(perkName);
        this.skillPoints++;
        syncFisherInfo();
    }

    public int nextLevelXP(){
        return (int) Math.floor(BASE_EXP * Math.pow(level, EXP_EXPONENT));
    }

    public void setFirstFishOfTheDayCaughtTime(long time) {
        this.firstFishOfTheDayCaughtTime = time;
    }

    public void setLastFishCaughtTime(long time){
        this.lastFishCaughtTime = time;
    }

    void fishCaught(Fish fish, int boostedExp){
        long worldTime = fisher.world.getTime();
        setLastFishCaughtTime(worldTime);
        if (worldTime >= firstFishOfTheDayCaughtTime + 24000) {
            setFirstFishOfTheDayCaughtTime(worldTime);
            if (hasPerk(FishingPerks.FREQUENT_CATCH_FIRST_CATCH)) {
                fisher.addStatusEffect(new StatusEffectInstance(FStatusEffectRegistry.FREQUENCY_BUFF,120));
            }
            if (hasPerk(FishingPerks.QUALITY_INCREASE_FIRST_CATCH)) {
                fisher.addStatusEffect(new StatusEffectInstance(FStatusEffectRegistry.QUALITY_BUFF,120));
            }
        }
        if (!caughtInChunk(fish.caughtIn)) {
            fishedChunks.add(fish.caughtIn);
        }
        grantExperience(boostedExp);
    }

    void grantExperience(double gainedXP){
        this.exp += gainedXP;
        float nextLevelXP = nextLevelXP();
        while (this.exp >= nextLevelXP) {
            this.exp -= nextLevelXP;
            this.level++;
            onLevelUpBehaviour();
            nextLevelXP = nextLevelXP();
        }
        syncFisherInfo();
    }


    private void onLevelUpBehaviour(){
        addSkillPoint();
        if (fisher == null){
            return;
        }
        //fisher.spawnParticles(ParticleTypes.FIREWORK, fisher.getX(), fisher.getY(),  fisher.getZ(), 1,1,1,1,1);
        //serverWorld.spawnParticles(ParticleTypes.FIREWORK, this.getX(), m, this.getZ(), (int)(1.0f + this.getWidth() * 20.0f), this.getWidth(), 0.0, this.getWidth(), 0.2f);

    }

    public boolean addCredit(int credit) {
        if (this.credit + credit < 0) {
            return false;
        }

        this.credit += credit;
        syncFisherInfo();
        return true;
    }

    public HashMap<String, FishingPerk> getPerks(){
        return perks;
    }

    public boolean hasPerk(FishingPerk perk) {
        return perks.containsKey(perk.getName());
    }

    public SimpleInventory getFisherInventory(){
        return this.fisherInventory;
    }

    public boolean caughtInChunk(Chunk chunk){
        for(Chunk caughtChunk : fishedChunks) {
            boolean chunkMatch = caughtChunk.x == chunk.x && caughtChunk.z == chunk.z;
            if (chunkMatch) {
                return true;
            }
        }
        return false;
    }

    public FisherInfo getHarpoonFisherInfo(){
        FisherInfo hFisherInfo = new FisherInfo();
        hFisherInfo.fisher = this.fisher;
        hFisherInfo.perks = this.perks;
        hFisherInfo.fishedChunks = this.fishedChunks;
        return hFisherInfo;
    }

    @Override
    public String toString(){
        return "\n============[Fisher Info]============" +
                "\nLevel: " + level +
                "\nExperience: " + exp +
                "\nPerk Count: " + perks.size() +
                "\nCredit: " + credit +
                "\n============[Fisher Info]============";
    }

    public static class Chunk{
        int x;
        int z;

        public Chunk(int x, int z) {
            this.x = x;
            this.z = z;
        }

        Chunk(String chunkString){
            String[] chunkData = chunkString.split(";");
            x = Integer.parseInt(chunkData[0]);
            z = Integer.parseInt(chunkData[1]);
        }

        public String toString(){
            return x + ";" + z;
        }
    }
}
