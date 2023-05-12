package net.semperidem.fishingclub.fish.fishingskill;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;

import java.util.ArrayList;
import java.util.Objects;

public class FishingSkill {
    private static final String FISHING_NBT_TAG = "fishing_skills";
    private static final int baseXP = 50;
    private static final float exponent = 1.25f;

    public int level = 1;
    public int exp = 0;

    ArrayList<FishingPerk> perks = new ArrayList<>();

    public FishingSkill(int level, int exp) {
        this.level = level;
        this.exp = exp;
        initPerks();
    }
    public FishingSkill(int level, int exp, String perkString) {
        this.level = level;
        this.exp = exp;
        this.perks = FishingSkillManager.getPlayerPerksFromString(perkString);
    }
    public FishingSkill() {
    }


    private void initPerks(){
        this.perks.add(FishingPerks.ROOT_HOBBYIST);
        this.perks.add(FishingPerks.ROOT_OPPORTUNIST);
        this.perks.add(FishingPerks.ROOT_SOCIALIST);
    }

    public void writeNBT(NbtCompound playerTag){
        NbtCompound fishingTag = new NbtCompound();
        fishingTag.putInt("level", this.level);
        fishingTag.putInt("exp", this.exp);
        NbtList perksList = new NbtList();
        for(FishingPerk perk : perks) {
            perksList.add(NbtString.of(perk.name));
        }
        fishingTag.put("perks", perksList);
        fishingTag.putInt("perk_count", perksList.size());
        playerTag.put(FISHING_NBT_TAG, fishingTag);
    }

    public void readNBT(NbtCompound playerTag){
        if (playerTag.contains(FISHING_NBT_TAG, 10)) {
            NbtCompound fishingTag = playerTag.getCompound(FISHING_NBT_TAG);
            this.level = fishingTag.getInt("level");
            this.exp = fishingTag.getInt("exp");
            int perkCount = fishingTag.getInt("perk_count");
            NbtList perkList = fishingTag.getList("perks", perkCount);
            for(int i = 0; i < perkCount; i++) {
                FishingPerks.getPerkFromName(perkList.getString(i)).ifPresent(fishingPerk -> perks.add(fishingPerk));
            }
        }
    }


    private int nextLevelXP(){
        return (int) Math.floor(baseXP * Math.pow(level, exponent));
    }

    public void grantExperience(double gainedXP){
        this.exp += gainedXP;
        float nextLevelXP = nextLevelXP();
        while (this.exp > nextLevelXP) {
            this.exp -= nextLevelXP;
            this.level++;
            nextLevelXP = nextLevelXP();
        }
    }

    public ArrayList<FishingPerk> getPerks(){
        return new ArrayList<>(perks);
    }

    public boolean hasPerk(FishingPerk perk) {
        return perks.contains(perk);
    }

    public void grantPerk(String perkName){
        if (Objects.equals(perkName, "all")) {
            perks.addAll(FishingPerks.ALL_PERKS);
        }
    }

    @Override
    public String toString(){
        return "[Fishing Skill] Level:" + level + "  Exp:" + exp;
    }
}
