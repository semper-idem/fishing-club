package net.semperidem.fishingclub.fisher;

import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.UUID;

public class FisherInfo {
    ServerPlayerEntity fisher;
    UUID fisherUUID;

    private static final int BASE_EXP = 50;
    private static final float EXP_EXPONENT = 1.25f;

    int level;
    int exp;
    HashMap<String, FishingPerk> perks;
    int fisherCredit;
    int skillPoints = 0;

    public FisherInfo(int level, int exp, String perkString, int credit, UUID fisherUUID) {
        this.level = level;
        this.exp = exp;
        this.perks = FisherInfos.getPlayerPerksFromString(perkString);
        this.fisherCredit = credit;
        this.fisherUUID = fisherUUID;
        this.fisher = FisherInfoDB.getPlayer(fisherUUID);
    }

    public FisherInfo(UUID fisherUUID) {
        this(1,0, "", 0, fisherUUID);
        initPerks();
    }

    public int getLevel() {
        return level;
    }

    public int getExp() {
        return exp;
    }

    public int getFisherCredit(){
        return fisherCredit;
    }

    private void initPerks(){
        addPerk(FishingPerks.ROOT_HOBBYIST);
        addPerk(FishingPerks.ROOT_OPPORTUNIST);
        addPerk(FishingPerks.ROOT_SOCIALIST);
    }

    public void addPerk(FishingPerk perk){
        if (availablePerk(perk) && skillPoints > 0) {
            this.perks.put(perk.name, perk);
            skillPoints--;
        }
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
        fisher.getWorld().spawnParticles(ParticleTypes.FIREWORK, fisher.getX(), fisher.getY(),  fisher.getZ(), 1,1,1,1,1);
        //serverWorld.spawnParticles(ParticleTypes.FIREWORK, this.getX(), m, this.getZ(), (int)(1.0f + this.getWidth() * 20.0f), this.getWidth(), 0.0, this.getWidth(), 0.2f);

    }

    public boolean removeCredit(int credit) {
        if (this.fisherCredit - credit < 0) {
            return false;
        }
        this.fisherCredit -= credit;
        return true;
    }

    public void addCredit(int credit) {
        this.fisherCredit += credit;
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



    @Override
    public String toString(){
        return "\n============[Fisher Info]============" +
                "\nLevel: " + level +
                "\nExperience: " + exp +
                "\nPerk Count: " + perks.size() +
                "\nCredit: " + fisherCredit +
                "\n============[Fisher Info]============";
    }
}
