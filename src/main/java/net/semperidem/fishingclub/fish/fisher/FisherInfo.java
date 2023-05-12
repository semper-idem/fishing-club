package net.semperidem.fishingclub.fish.fisher;

import java.util.ArrayList;
import java.util.Objects;

public class FisherInfo {

    private static final int BASE_XP = 50;
    private static final float XP_EXPONENT = 1.25f;


    int level = 1;
    int exp = 0;
    ArrayList<FishingPerk> perks = new ArrayList<>();
    int credit;

    public FisherInfo(int level, int exp, String perkString, int credit) {
        this.level = level;
        this.exp = exp;
        this.perks = FisherInfos.getPlayerPerksFromString(perkString);
        this.credit = credit;
    }

    public FisherInfo(int level, int exp, String perkString) {
        this.level = level;
        this.exp = exp;
        this.perks = FisherInfos.getPlayerPerksFromString(perkString);
    }

    public FisherInfo(int level, int exp) {
        this.level = level;
        this.exp = exp;
        initPerks();
    }

    public FisherInfo() {
    }

    public int getLevel() {
        return level;
    }

    public int getExp() {
        return exp;
    }

    private void initPerks(){
        this.perks.add(FishingPerks.ROOT_HOBBYIST);
        this.perks.add(FishingPerks.ROOT_OPPORTUNIST);
        this.perks.add(FishingPerks.ROOT_SOCIALIST);
    }



    private int nextLevelXP(){
        return (int) Math.floor(BASE_XP * Math.pow(level, XP_EXPONENT));
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
        return "[Fisher Info] Level:" + level + "  Exp:" + exp;
    }
}
