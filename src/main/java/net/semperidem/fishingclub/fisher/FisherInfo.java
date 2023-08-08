package net.semperidem.fishingclub.fisher;

import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.semperidem.fishingclub.fisher.perks.FishingPerk;
import net.semperidem.fishingclub.fisher.perks.FishingPerks;
import net.semperidem.fishingclub.fisher.perks.spells.SpellInstance;
import net.semperidem.fishingclub.fisher.perks.spells.Spells;
import net.semperidem.fishingclub.util.InventoryUtil;

import java.util.HashMap;
import java.util.Optional;

public class FisherInfo {
    public static TrackedData<NbtCompound> TRACKED_DATA = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.NBT_COMPOUND);
    public static final String TAG = "fisher_info";

    private static final int BASE_EXP = 50;
    private static final float EXP_EXPONENT = 1.25f;

    private int level = 1;
    private int exp = 0;
    private int credit = 0;
    private int skillPoints = 0;
    private HashMap<String, FishingPerk> perks = new HashMap<>();
    private SimpleInventory fisherInventory = new SimpleInventory(4);
    private HashMap<FishingPerk, SpellInstance> spells = new HashMap<>();

    private PlayerEntity fisher;

    public FisherInfo(){
        initPerks();
    }
    public FisherInfo(int level){
        this();
        this.level = level;
    }

    public FisherInfo(PlayerEntity playerEntity) {
        this.fisher = playerEntity;
        if (!trackerInitialized(playerEntity)) return;

        NbtCompound fisherTag = playerEntity.getDataTracker().get(TRACKED_DATA);
        this.level = fisherTag.getInt("level");
        this.exp = fisherTag.getInt("exp");
        this.credit = fisherTag.getInt("credit");
        this.skillPoints = fisherTag.getInt("skill_points");
        this.fisherInventory = InventoryUtil.readInventory(fisherTag.getCompound("inventory"));
        setPerks(fisherTag);
        setSpells(fisherTag);
    }

    private boolean trackerInitialized(PlayerEntity playerEntity){
        final boolean[] result = {false};
        playerEntity.getDataTracker().getAllEntries().forEach(trackedData -> {
            if (trackedData.getData() == TRACKED_DATA) result[0] = true;
        });
        return result[0];
    }

    private void setPerks(NbtCompound fisherTag){
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
        NbtList spellListTag = fisherTag.getList("spells", NbtElement.COMPOUND_TYPE);
        for(int i = 0; i < spellListTag.size(); i++) {
            NbtCompound spellTag = spellListTag.getCompound(i);
            String perkName = spellTag.getString("key");
            long nextCast = spellTag.getLong("nextCast");
            Optional<FishingPerk> optionalPerk = FishingPerks.getPerkFromName(perkName);
            if (optionalPerk.isEmpty()) continue;
            FishingPerk fishingPerk = optionalPerk.get();
            SpellInstance spellInstance = SpellInstance.getSpellInstance(fishingPerk, nextCast);
            spells.put(fishingPerk, spellInstance);
        }
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
            spellTag.putLong("nextCast", spellInstance.getNextCast());
            spellListTag.add(spellTag);
        }
        fisherTag.put("spells", spellListTag);
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

    private void updateDataTracker(){
        if (this.fisher == null) return;
        this.fisher.getDataTracker().set(TRACKED_DATA, toNbt());
    }

    private void addRootPerk(FishingPerk perk){
        this.perks.put(perk.getName(), perk);
    }

    void addPerk(FishingPerk perk){
        if (availablePerk(perk) && hasSkillPoints()) {
            perk.onEarn(fisher);
            this.perks.put(perk.getName(), perk);
            if (Spells.perkHasSpell(perk)) {
                this.spells.put(perk, SpellInstance.getSpellInstance(perk, 0));
            }
            skillPoints--;
            updateDataTracker();
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
        updateDataTracker();
    }

    public int getSkillPoints(){
        return this.skillPoints;
    }

    public boolean hasSkillPoints(){
        return this.skillPoints > 0;
    }

    void setSkillPoints(int skillPoints){
        this.skillPoints = skillPoints;
        updateDataTracker();
    }


    void removePerk(String perkName){
        if (!this.perks.containsKey(perkName)) return;
        this.perks.remove(perkName);
        this.skillPoints++;
        updateDataTracker();
    }

    public int nextLevelXP(){
        return (int) Math.floor(BASE_EXP * Math.pow(level, EXP_EXPONENT));
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
        updateDataTracker();
    }


    private void onLevelUpBehaviour(){
        addSkillPoint();
        if (fisher == null){
            return;
        }
        //fisher.spawnParticles(ParticleTypes.FIREWORK, fisher.getX(), fisher.getY(),  fisher.getZ(), 1,1,1,1,1);
        //serverWorld.spawnParticles(ParticleTypes.FIREWORK, this.getX(), m, this.getZ(), (int)(1.0f + this.getWidth() * 20.0f), this.getWidth(), 0.0, this.getWidth(), 0.2f);

    }

    boolean addCredit(int credit) {
        if (this.credit + credit < 0) {
            return false;
        }

        this.credit += credit;
        updateDataTracker();
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



    @Override
    public String toString(){
        return "\n============[Fisher Info]============" +
                "\nLevel: " + level +
                "\nExperience: " + exp +
                "\nPerk Count: " + perks.size() +
                "\nCredit: " + credit +
                "\n============[Fisher Info]============";
    }
}
