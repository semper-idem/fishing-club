package net.semperidem.fishingclub.fisher;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.semperidem.fishingclub.util.InventoryUtil;

import java.util.HashMap;

public class FisherInfo {
    private static final String FISHER_INFO_TAG_NAME = "fisher_info";

    private static final int BASE_EXP = 50;
    private static final float EXP_EXPONENT = 1.25f;

    private int level = 1;
    private int exp = 0;
    private int credit = 0;
    private int skillPoints = 0;
    private HashMap<String, FishingPerk> perks = new HashMap<>();
    private SimpleInventory fisherInventory = new SimpleInventory(4);

    private PlayerEntity fisher;

    public FisherInfo(){
        initPerks();
    }

    public FisherInfo(PlayerEntity playerEntity, NbtCompound playerTag) {
        if (!playerTag.contains(FISHER_INFO_TAG_NAME)) {
            return;
        }
        this.fisher = playerEntity;

        NbtCompound fisherTag = playerTag.getCompound(FISHER_INFO_TAG_NAME);
        this.level = fisherTag.getInt("level");
        this.exp = fisherTag.getInt("exp");
        this.credit = fisherTag.getInt("credit");
        this.skillPoints = fisherTag.getInt("skill_points");
        this.fisherInventory = InventoryUtil.readInventory(fisherTag.getCompound("inventory"));
        NbtList perkListTag = fisherTag.getList("perks", NbtElement.STRING_TYPE);
        if (perkListTag.size() == 0) {
            initPerks();
        } else {
            perkListTag.forEach(
                    nbtElement -> FishingPerks.getPerkFromName(nbtElement.asString()).ifPresent(
                            fishingPerk -> this.perks.put(fishingPerk.name, fishingPerk)));
        }
        FisherInfos.updateFisher(playerEntity.getUuid(), this);
    }

    public void writeNbt(NbtCompound playerTag){
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
        playerTag.put(FISHER_INFO_TAG_NAME, fisherTag);
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

    public void addRootPerk(FishingPerk perk){
        this.perks.put(perk.name, perk);
    }

    public void addPerk(FishingPerk perk){
        if (availablePerk(perk) && hasSkillPoints()) {
            this.perks.put(perk.name, perk);
            skillPoints--;
        }
    }

    public void addPerk(String perkName){
        FishingPerks.getPerkFromName(perkName).ifPresent(this::addPerk);
    }

    public boolean availablePerk(FishingPerk perk){
        return perk.parent == null || this.perks.containsKey(perk.parent.name);
    }

    private void addSkillPoint(){
        this.skillPoints++;
    }

    public int getSkillPoints(){
        return this.skillPoints;
    }

    public boolean hasSkillPoints(){
        return this.skillPoints > 0;
    }
    public void setSkillPoints(int skillPoints){
        this.skillPoints = skillPoints;
    }

    public void removePerk(String perkName){
        if (!this.perks.containsKey(perkName)) return;
        this.perks.remove(perkName);
        this.skillPoints++;
    }

    public int nextLevelXP(){
        return (int) Math.floor(BASE_EXP * Math.pow(level, EXP_EXPONENT));
    }

    public void grantExperience(double gainedXP){
        this.exp += gainedXP;
        float nextLevelXP = nextLevelXP();
        while (this.exp >= nextLevelXP) {
            this.exp -= nextLevelXP;
            this.level++;
            onLevelUpBehaviour();
            nextLevelXP = nextLevelXP();

        }
    }

    public void onLevelUpBehaviour(){
        addSkillPoint();
        if (fisher == null){
            return;
        }
        //fisher.spawnParticles(ParticleTypes.FIREWORK, fisher.getX(), fisher.getY(),  fisher.getZ(), 1,1,1,1,1);
        //serverWorld.spawnParticles(ParticleTypes.FIREWORK, this.getX(), m, this.getZ(), (int)(1.0f + this.getWidth() * 20.0f), this.getWidth(), 0.0, this.getWidth(), 0.2f);

    }

    public boolean removeCredit(int credit) {
        if (this.credit - credit < 0) {
            return false;
        }
        this.credit -= credit;
        return true;
    }

    public boolean addCredit(int credit) {
        if (this.credit + credit < 0) {
            return false;
        }

        this.credit += credit;
        return true;
    }

//
//    public void grantPerk(String perkName){
//        if (Objects.equals(perkName, "all")) {
//            perks.addAll(FishingPerks.ALL_PERKS);
//        }
//    }

    public HashMap<String, FishingPerk> getPerks(){
        return perks;
    }

    public boolean hasPerk(FishingPerk perk) {
        return perks.containsKey(perk.name);
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
