package net.semperidem.fishingclub.fisher.perks;

import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.semperidem.fishingclub.FishingClub;

import java.util.ArrayList;
import java.util.List;

import static net.semperidem.fishingclub.fisher.perks.TradeSecrets.*;

public class TradeSecret {
    String name;
    Text label;
    Text shortDescription;
    List<Text> longDescription;
    TradeSecret parent;
    List<TradeSecret> children = new ArrayList<>();
    Identifier texture;
    int maxLevel = 1;
    float[] levelValues;
    int[] costPerLevel;

    public String name() {
        return this.name;
    }

    public List<TradeSecret> getChildren(){
        return this.children;
    }

    public Identifier getTexture(){
        return this.texture;
    }

    public TradeSecret getParent(){
        return parent;
    }

    public Text getLabel(){
        return label;
    }

    public Text getShortDescription(){
        return this.shortDescription;
    }

    public List<Text> getLongDescription(){
        return this.longDescription;
    }

    public int maxLevel() {
        return this.maxLevel;
    }

    public int cost(int level) {
        if (this.costPerLevel == null || this.costPerLevel.length < level) {
            return 1;
        }
        return costPerLevel[level];
    }

    static Builder builder() {
        return new Builder();
    }

    static class Builder {
        private String name;
        private TradeSecret parent;
        private float[] levelValues;
        private int[] costPerLevel;

        TradeSecret build() {
            TradeSecret tradeSecret = new TradeSecret();
            NAME_TO_SKILL.put(this.name, tradeSecret);

            tradeSecret.name = this.name;
            tradeSecret.label = Text.translatable(this.name);
            tradeSecret.texture = FishingClub.identifier("textures/gui/skill/" + this.name);
            tradeSecret.shortDescription = Text.translatable(this.name + ".short_description");
            tradeSecret.maxLevel = this.levelValues == null ? 0 : this.levelValues.length;
            tradeSecret.levelValues = this.levelValues;
            tradeSecret.costPerLevel = this.costPerLevel;

            this.parent.children.add(tradeSecret);
            tradeSecret.parent = this.parent;

            tradeSecret.longDescription = this.createLongDescriptions(tradeSecret);

            return tradeSecret;
        }

        private ArrayList<Text> createLongDescriptions(TradeSecret tradeSecret) {
            ArrayList<Text> leveledLongDescriptions = new ArrayList<>();
            for(int i = 0; i < tradeSecret.maxLevel; i++) {
                leveledLongDescriptions.add(Text.translatable(this.name + ".long_description." + i));
            }
            if (leveledLongDescriptions.isEmpty()) {
                leveledLongDescriptions.add(Text.translatable(this.name + ".long_description.0"));
            }
            return leveledLongDescriptions;
        }

        Builder costPerLevel(int... costPerLevel) {
            this.costPerLevel = costPerLevel;
            return this;
        }

        Builder levelValues(float... levelValues) {
            this.levelValues = levelValues;
            return this;
        }

        Builder name(String name) {
            this.name = name;
            return this;
        }

        Builder parent(TradeSecret parent) {
            this.parent = parent;
            return this;
        }

    }


}
