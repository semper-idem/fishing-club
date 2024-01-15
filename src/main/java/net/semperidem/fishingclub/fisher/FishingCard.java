package net.semperidem.fishingclub.fisher;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.semperidem.fishingclub.client.screen.fishing_card.FishingCardScreenHandler;
import net.semperidem.fishingclub.fisher.level_reward.LevelReward;
import net.semperidem.fishingclub.fisher.level_reward.LevelRewardRule;
import net.semperidem.fishingclub.fisher.perks.FishingPerk;
import net.semperidem.fishingclub.fisher.perks.FishingPerks;
import net.semperidem.fishingclub.fisher.perks.spells.SpellInstance;
import net.semperidem.fishingclub.fisher.perks.spells.Spells;
import net.semperidem.fishingclub.fish.HookedFish;
import net.semperidem.fishingclub.item.fishing_rod.FishingRodPartController;
import net.semperidem.fishingclub.item.fishing_rod.FishingRodPartType;
import net.semperidem.fishingclub.network.ServerPacketSender;
import net.semperidem.fishingclub.registry.FStatusEffectRegistry;
import net.semperidem.fishingclub.util.InventoryUtil;

import java.util.*;


public class FishingCard {
    public static final String TAG = "fishing_card";

    private static final int BASE_EXP = 50;
    private static final float EXP_EXPONENT = 1.25f;

    private int level = 1;
    private int exp = 0;
    private int credit = 0;
    private int skillPoints = 0;
    private long lastFishCaughtTime = 0;
    private long firstFishOfTheDayCaughtTime = 0;
    private HashMap<String, FishingPerk> perks = new HashMap<>();
    private SimpleInventory fisherInventory = new SimpleInventory(FishingCardScreenHandler.SLOT_COUNT);
    private HashMap<FishingPerk, SpellInstance> spells = new HashMap<>();
    private ArrayList<Chunk> fishedChunks = new ArrayList<>();
    private ArrayList<UUID> linkedFishers = new ArrayList<>();
    private ItemStack lastUsedBait = ItemStack.EMPTY;
    private ItemStack sharedBait = ItemStack.EMPTY;
    private TeleportRequest lastTeleportRequest = new TeleportRequest("Jeb", 0);

    private PlayerEntity owner;

    public FishingCard(){
        initPerks();
    }
    public FishingCard(int level){
        this();
        this.level = level;
    }

    public FishingCard(PlayerEntity playerEntity, NbtCompound fisherTag) {
        this.owner = playerEntity;
        fromNbt(fisherTag);
    }
    public FishingCard(PlayerEntity playerEntity) {
        this.owner = playerEntity;
        NbtCompound playerCustomTag = new NbtCompound();
        playerEntity.writeCustomDataToNbt(playerCustomTag);
        if (!playerCustomTag.contains(TAG)) return;
        fromNbt(playerCustomTag.getCompound(TAG));
    }

    public FishingCard(NbtCompound fisherTag) {
        fromNbt(fisherTag);
    }

    public PlayerEntity getOwner(){
        return owner;
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
        setLinked(fisherTag);
        this.lastUsedBait = ItemStack.fromNbt(fisherTag.getCompound("last_used_bait"));
        setLastTeleportRequest(fisherTag);
    }

    private void setLastTeleportRequest(NbtCompound fisherTag){
        if (!fisherTag.contains("last_teleport_request")) return;
        NbtCompound lastTeleportRequestTag = fisherTag.getCompound("last_teleport_request");
        this.lastTeleportRequest = TeleportRequest.fromNbt(lastTeleportRequestTag);
    }

    private void setLinked(NbtCompound fisherTag){
        linkedFishers = new ArrayList<>();
        NbtList uuidListTag = fisherTag.getList("linked", NbtElement.STRING_TYPE);
        for(int i = 0; i < uuidListTag.size(); i++) {
            linkedFishers.add(UUID.fromString(uuidListTag.getString(i)));
        }
    }

    private void setChunks(NbtCompound fisherTag){
        fishedChunks.clear();
        NbtList chunkListTag = fisherTag.getList("fishedC_chunks", NbtElement.STRING_TYPE);
        for(int i = 0; i < chunkListTag.size(); i++) {
            fishedChunks.add(new Chunk(chunkListTag.getString(i)));
        }
    }

    public void setClientEntity(MinecraftClient client){
        this.owner = client.player;
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
        fisherTag.put("perks", getPerkListTag());
        fisherTag.put("spells", getSpellListTag());
        fisherTag.put("fished_chunks", getFishedChunksList());
        fisherTag.put("linked", getLinkedList());
        fisherTag.put("last_used_bait", lastUsedBait.writeNbt(new NbtCompound()));
        fisherTag.put("last_teleport_request", TeleportRequest.toNbt(lastTeleportRequest));
        return fisherTag;
    }

    private NbtList getLinkedList(){
        NbtList linkedListTag = new NbtList();
        for(UUID linkedUUID : linkedFishers) {
            linkedListTag.add(NbtString.of(linkedUUID.toString()));
        }
        return linkedListTag;
    }

    private NbtList getFishedChunksList(){
        NbtList fishedChunksTag = new NbtList();
        if (fishedChunks.size() == 0) return fishedChunksTag;
        for(Chunk c : fishedChunks) {
            fishedChunksTag.add(NbtString.of(c.toString()));
        }
        return fishedChunksTag;
    }

    private NbtList getSpellListTag(){
        NbtList spellListTag = new NbtList();
        for(SpellInstance spellInstance : spells.values()) {
            NbtCompound spellTag = new NbtCompound();
            spellTag.putString("key", spellInstance.getKey());
            spellTag.putInt("cooldown", spellInstance.getCooldown());
            spellTag.putLong("nextCast", spellInstance.getNextCast());
            spellListTag.add(spellTag);
        }
        return spellListTag;
    }

    private NbtList getPerkListTag(){
        NbtList perkListTag = new NbtList();
        this.perks.forEach((fishingPerkName, fishingPerk) -> {
            perkListTag.add(NbtString.of(fishingPerkName));
        });
        return perkListTag;
    }

    public void resetCooldown(){
        for(SpellInstance spellInstance : spells.values()) {
            spellInstance.resetCooldown();
        }
        syncFisherInfo();
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


    public void setSharedBait(ItemStack baitToShare){
        this.sharedBait = baitToShare;
    }

    public ItemStack getSharedBait(){
        return sharedBait;
    }
    public ItemStack getLastUsedBait(){
        return lastUsedBait;
    }

    private void initPerks(){
        addRootPerk(FishingPerks.ROOT_HOBBYIST);
        addRootPerk(FishingPerks.ROOT_OPPORTUNIST);
        addRootPerk(FishingPerks.ROOT_SOCIALIST);
    }

    public void setTeleportRequest(UUID summonerUUID, long worldTime){
        lastTeleportRequest = new TeleportRequest(summonerUUID.toString(), worldTime);
    }

    public TeleportRequest getLastTeleportRequest(){
        return lastTeleportRequest;
    }

    public boolean canAcceptTeleport(long currentWorldTime){
        return currentWorldTime - lastTeleportRequest.requestTick < 600; //30s
    }

    private void syncFisherInfo(){
        if (this.owner == null) return;
        if (!(this.owner instanceof ServerPlayerEntity serverFisher)) return;
        NbtCompound playerCustomTag = new NbtCompound();
        this.owner.writeCustomDataToNbt(playerCustomTag);
        playerCustomTag.put(TAG, toNbt());
        this.owner.readCustomDataFromNbt(playerCustomTag);
        ServerPacketSender.sendFisherInfo(serverFisher, this);
    }

    private void addRootPerk(FishingPerk perk){
        this.perks.put(perk.getName(), perk);
    }

    void addPerk(FishingPerk perk){
        if (availablePerk(perk) && hasSkillPoints()) {
            perk.onEarn(owner);
            this.perks.put(perk.getName(), perk);
            if (Spells.perkHasSpell(perk)) {
                this.spells.put(perk, SpellInstance.getSpellInstance(perk, 0, owner.world.getTime()));
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
    public void addSkillPoints(int amount){
        this.skillPoints+= amount;
        syncFisherInfo();
    }

    public boolean isFishingFromBoat(){
        return owner.getVehicle() instanceof BoatEntity;
    }
    public int getMinGrade(){
        int minGrade = 0;
        long worldTime = this.owner.world.getTime();
        if (this.firstFishOfTheDayCaughtTime + 24000 < worldTime) {
            if (hasPerk(FishingPerks.FIRST_CATCH)) {
                minGrade++;
            }
            minGrade++;
        }
        if (this.owner.hasStatusEffect(FStatusEffectRegistry.QUALITY_BUFF) && Math.random() > 0.25f) {
            minGrade++;
        } else if (this.owner.hasStatusEffect(FStatusEffectRegistry.ONE_TIME_QUALITY_BUFF)){
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

    public void useSpell(FishingPerk fishingPerk, Entity target){
        if (!perks.containsKey(fishingPerk.getName())) return;
        SpellInstance spellInstance = spells.get(fishingPerk);
        spellInstance.use((ServerPlayerEntity) owner, target);
        spells.put(fishingPerk, spellInstance);
        syncFisherInfo();
    }


    public SpellInstance getSpell(FishingPerk fishingPerk) {
        if (!perks.containsKey(fishingPerk.getName())) return null;
        return spells.get(fishingPerk);
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

    void fishCaught(HookedFish fish, int boostedExp){
        long worldTime = owner.world.getTime();
        setLastFishCaughtTime(worldTime);
        firstFishOfTheDayCaught(worldTime);
        fishCaughtInChunk(fish);
        processOneTimeBuff(fish);
        prolongStatusEffects();
        grantExperience(boostedExp);
        if (fish.caughtUsing == null || fish.caughtUsing == Items.AIR.getDefaultStack()) return;
        lastUsedBait = FishingRodPartController.getPart(fish.caughtUsing, FishingRodPartType.BAIT);
    }

    private void prolongStatusEffects(){
        if (!hasPerk(FishingPerks.SHARED_BUFFS)) {
            return;
        }

        if (!this.owner.hasVehicle()) {
            return;
        }

        if (!(this.owner.getVehicle() instanceof BoatEntity boatEntity)) {
            return;
        }

        ImmutableList<Entity> passengers = (ImmutableList<Entity>) boatEntity.getPassengerList();
        if (passengers.size() <= 1) {
            return;
        }
        for(Entity passenger : passengers) {
            if(!(passenger instanceof PlayerEntity playerPassenger)) continue;
            playerPassenger.getStatusEffects().forEach(
                    sei -> {
                        sei.upgrade(new StatusEffectInstance(sei.getEffectType(), sei.getDuration() + 200, sei.getAmplifier()));
                    });
        }

    }

    private void firstFishOfTheDayCaught(long worldTime){
        if (worldTime < firstFishOfTheDayCaughtTime + 24000) {
            return;
        }
        setFirstFishOfTheDayCaughtTime(worldTime);
        if (hasPerk(FishingPerks.FREQUENT_CATCH_FIRST_CATCH)) {
            owner.addStatusEffect(new StatusEffectInstance(FStatusEffectRegistry.FREQUENCY_BUFF,120));
        }
        if (hasPerk(FishingPerks.QUALITY_INCREASE_FIRST_CATCH)) {
            owner.addStatusEffect(new StatusEffectInstance(FStatusEffectRegistry.QUALITY_BUFF,120));
        }
    }

    private void fishCaughtInChunk(HookedFish fish){
        if (!caughtInChunk(fish.caughtIn)) {
            fishedChunks.add(fish.caughtIn);
        }
    }

    private void processOneTimeBuff(HookedFish fish){
        if (!fish.oneTimeBuffed) {
            return;
        }
        if (!owner.hasStatusEffect(FStatusEffectRegistry.ONE_TIME_QUALITY_BUFF)) {
            return;
        }
        decreaseOneTimeBuff();
    }

    private void decreaseOneTimeBuff(){
        StatusEffectInstance sei = owner.getStatusEffect(FStatusEffectRegistry.ONE_TIME_QUALITY_BUFF);
        int effectPower = sei.getAmplifier();
        if (effectPower == 0) {
            owner.removeStatusEffect(FStatusEffectRegistry.ONE_TIME_QUALITY_BUFF);
        } else {
            sei.upgrade(new StatusEffectInstance(FStatusEffectRegistry.ONE_TIME_QUALITY_BUFF,sei.getDuration(), effectPower - 1));
        }
    }

    public void grantExperience(double gainedXP){
        if (gainedXP == 0) return;
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
        if (owner == null) return;
        for(LevelReward reward : LevelRewardRule.getRewardForLevel(this.level)){
            reward.grant(this);
        }

        boolean isMilestone = this.level % 5 == 0;
        boolean isHundred = this.level % 100 == 0;
        double x = owner.getX();
        double y = owner.getY();
        double z = owner.getZ();
        ServerWorld world = (ServerWorld) owner.getWorld();

        if (isHundred) {
            hundredthLevelUpEffect(world, x, y, z);
        } else if(isMilestone) {
            fifthLevelUpEffect(world, x, y, z);
        } else {
            levelUpEffect(world, x, y, z);
        }


        if (Math.random() < 0.01) { //FUNNY :DDD
           world.playSound(owner, x, y, z, SoundEvents.ENTITY_RAVAGER_ROAR, SoundCategory.PLAYERS, 1f, 0.4f, 0L);
        }
    }

    private void hundredthLevelUpEffect(ServerWorld world, double x, double y, double z){
        world.spawnParticles(ParticleTypes.DRAGON_BREATH, x, y + 2, z, 200,0,0,0,0.1);
        world.spawnParticles(ParticleTypes.SOUL_FIRE_FLAME, x, y + 2, z, 25,0,0,0,0.1);
        world.spawnParticles(ParticleTypes.FIREWORK, x, y + 2, z, 100,0,0,0,0.1);
        world.spawnParticles(ParticleTypes.SCRAPE, x, y + 2, z, 100,0.5,0.5,0.5,0.1);
        world.playSound(null, x, y, z, SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 0.6f, 0.7f, 0L);
        world.playSound(null, x, y, z, SoundEvents.ENTITY_FIREWORK_ROCKET_LARGE_BLAST, SoundCategory.PLAYERS, 0.5f, 0.2f, 0L);

    }

    private void fifthLevelUpEffect(ServerWorld world, double x, double y, double z){
        world.spawnParticles(ParticleTypes.DRAGON_BREATH, x, y + 2, z, 100,0,0,0,0.1);
        world.spawnParticles(ParticleTypes.FIREWORK, x, y + 2, z, 100,0,0,0,0.1);
        world.spawnParticles(ParticleTypes.SCRAPE, x, y + 2, z, 100,0.5,0.5,0.5,0.1);
        world.playSound(null, x, y, z, SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 0.5f, 0.7f, 0L);
        world.playSound(null, x, y, z, this.level == 100 ? SoundEvents.ENTITY_FIREWORK_ROCKET_LARGE_BLAST : SoundEvents.ENTITY_FIREWORK_ROCKET_BLAST, SoundCategory.PLAYERS, 0.3f, 0.2f, 0L);
    }

    private void levelUpEffect(ServerWorld world, double x, double y, double z){
        world.spawnParticles(ParticleTypes.FIREWORK, x, y + 2, z, 25,0,0,0,0.1);
        world.spawnParticles(ParticleTypes.SCRAPE, x, y + 2, z, 25,0.5,0.5,0.5,0.1);
        world.playSound(null, x, y, z, SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 0.25f, 0.7f, 0L);
        world.playSound(null, x, y, z, this.level == 100 ? SoundEvents.ENTITY_FIREWORK_ROCKET_LARGE_BLAST : SoundEvents.ENTITY_FIREWORK_ROCKET_BLAST, SoundCategory.PLAYERS, 0.25f, 0.2f, 0L);
    }

    public boolean addCredit(int credit) {
        if (this.credit + credit < 0) {
            return false;
        }

        this.credit += credit;
        syncFisherInfo();
        return true;
    }

    public void setCredit(int credit) {
        this.credit = credit;
        syncFisherInfo();
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

    public FishingCard getHarpoonFisherInfo(){
        FishingCard hFishingCard = new FishingCard();
        hFishingCard.owner = this.owner;
        hFishingCard.perks = this.perks;
        hFishingCard.fishedChunks = this.fishedChunks;
        return hFishingCard;
    }


    public ArrayList<UUID> getLinkedFishers(){
        return linkedFishers;
    }

    public boolean isLinked(UUID uuid){
        return linkedFishers.contains(uuid);
    }

    public void unlinkFisher(UUID uuid){
        if (!linkedFishers.contains(uuid)) return;
        linkedFishers.remove(uuid);
    }
    public void linkedFisher(UUID linkUUID){
        int allowedSize = hasPerk(FishingPerks.FISHERMAN_LINK) ? hasPerk(FishingPerks.DOUBLE_LINK) ? 2 : 1 : 0;
        if (allowedSize == 0) {
            return;
        }
        if (linkedFishers.size() < allowedSize) {
            linkedFishers.add(linkUUID);
        } else {
            linkedFishers.remove(0);
            linkedFishers.add(linkUUID);
        }
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

        public Chunk(String chunkString){
            String[] chunkData = chunkString.split(";");
            x = Integer.parseInt(chunkData[0]);
            z = Integer.parseInt(chunkData[1]);
        }

        public String toString(){
            return x + ";" + z;
        }
    }

    public static class TeleportRequest{
        public final String summonerUUID;
        public final long requestTick;


        TeleportRequest(String summonerUUID, long worldTime){
            this.summonerUUID = summonerUUID;
            this.requestTick = worldTime;
        }

        public static TeleportRequest fromNbt(NbtCompound teleportRequestTag){
            return new TeleportRequest(teleportRequestTag.getString("summonerUUID"), teleportRequestTag.getLong("request_tick"));
        }

        public static NbtCompound toNbt(TeleportRequest teleportRequest){
            NbtCompound self = new NbtCompound();
            self.putString("summonerUUID", teleportRequest.summonerUUID);
            self.putLong("request_tick", teleportRequest.requestTick);
            return self;
        }
    }
}
