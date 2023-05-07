package net.semperidem.fishingclub.fish.fishingskill;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

import java.util.UUID;

public class FishingSkill {
    private static final String FISHING_NBT_TAG = "fishing_skills";

    public int level = 1;
    public int exp = 0;

    public FishingSkill(int level, int exp) {
        this.level = level;
        this.exp = exp;
    }

    public FishingSkill() {
        this.level = 1;
        this.exp = 0;
    }


    public void writeNBT(NbtCompound playerTag){
        NbtCompound fishingTag = new NbtCompound();
        fishingTag.putInt("level", this.level);
        fishingTag.putInt("exp", this.exp);
        playerTag.put(FISHING_NBT_TAG, fishingTag);
    }

    public void readNBT(NbtCompound playerTag){
        if (playerTag.contains(FISHING_NBT_TAG, 10)) {
            NbtCompound fishingTag = playerTag.getCompound(FISHING_NBT_TAG);
            this.level = fishingTag.getInt("level");
            this.exp = fishingTag.getInt("exp");
        }
    }

    public void grantExperience(double gainedXP){
        this.exp += gainedXP;
        while (this.exp > level * 100) {
            this.exp -= level * 100;
            this.level++;
        }
    }

    @Override
    public String toString(){
        return "Fishing Skill/ Level:" + level + "  Exp:" + exp;
    }
}
