package net.semperidem.fishingclub.fisher.managers;

import net.minecraft.nbt.NbtCompound;
import net.semperidem.fishingclub.fish.specimen.SpecimenData;

public class SpeciesStatistics {
    private final String name;
    private int count;
    private int topGradeCount;
    private float bestLength = 0;
    private float worstLength = 0;
    private float bestWeight = 0;
    private float worstWeight;

    SpeciesStatistics(String name) {
        this.name = name;
    }
    SpeciesStatistics(NbtCompound nbt) {
        this.name = nbt.getString(NAME);
        this.count = nbt.getInt(COUNT);
        this.topGradeCount = nbt.getInt(TOP_GRADE_COUNT);
        this.bestLength = nbt.getFloat(BEST_LENGTH);
        this.worstLength = nbt.getFloat(WORSE_LENGTH);
        this.bestWeight = nbt.getFloat(BEST_WEIGHT);
        this.worstWeight = nbt.getFloat(WORST_WEIGHT);
    }

    void record(SpecimenData fish) {
        count++;

        if (fish.quality() == 5) {
            topGradeCount++;
        }
        if (fish.length() > bestLength) {
            bestLength = fish.length();
        }
        if (fish.length() < worstLength || worstLength == 0) {
            worstLength = fish.length();
        }
        if (fish.weight() > bestWeight) {
            bestWeight = fish.weight();
        }
        if (fish.weight() < worstWeight || worstWeight == 0) {
            worstWeight = fish.weight();
        }
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    public int getTopGradeCount() {
        return topGradeCount;
    }

    public float getBestLength() {
        return bestLength;
    }

    public float getWorstLength() {
        return worstLength;
    }

    public float getBestWeight() {
        return bestWeight;
    }

    public float getWorstWeight() {
        return worstWeight;
    }

    NbtCompound toNbt() {
        NbtCompound nbt = new NbtCompound();
        nbt.putString(NAME, this.name);
        nbt.putFloat(BEST_LENGTH, this.bestLength);
        nbt.putFloat(WORSE_LENGTH, this.worstLength);
        nbt.putFloat(BEST_WEIGHT, this.bestWeight);
        nbt.putFloat(WORST_WEIGHT, this.worstWeight);
        nbt.putInt(COUNT, this.count);
        nbt.putInt(TOP_GRADE_COUNT, this.topGradeCount);
        return nbt;
    }


    private static final String NAME = "label";
    private static final String BEST_LENGTH = "best_length";
    private static final String WORSE_LENGTH = "worse_length";
    private static final String BEST_WEIGHT = "best_weight";
    private static final String WORST_WEIGHT = "worst_weight";
    private static final String COUNT = "COUNT";
    private static final String TOP_GRADE_COUNT = "TOP_GRADE_COUNT";
}