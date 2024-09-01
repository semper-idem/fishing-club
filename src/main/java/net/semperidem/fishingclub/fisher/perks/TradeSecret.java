package net.semperidem.fishingclub.fisher.perks;

import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.semperidem.fishingclub.FishingClub;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.semperidem.fishingclub.fisher.perks.TradeSecrets.*;

public class TradeSecret {
    public final int id;
    Text label;
    Text shortDescription;
    Text longDescription;
    TradeSecret parent;
    List<TradeSecret> children = new ArrayList<>();
    Identifier texture;
    int maxLevel = 0;
    float[] levelValues;
    int[] costPerLevel;

    private TradeSecret(int id) {
        this.id = id;
    }

    static Builder builder() {
        return new Builder();
    }

    static class Builder {
        private final int id;
        private String name;
        private TradeSecret parent;
        private float[] levelValues;
        private int[] costPerLevel;

        Builder() {
            id = ID_TO_SKILL.size();
        }

        TradeSecret build() {
            TradeSecret tradeSecret = new TradeSecret(this.id);
            ID_TO_SKILL.put(this.id, tradeSecret);

            tradeSecret.label = Text.translatable(this.name);
            tradeSecret.texture = FishingClub.identifier("textures/gui/skill/" + this.name);
            tradeSecret.shortDescription = Text.translatable(this.name + ".short_description");
            tradeSecret.longDescription = Text.translatable(this.name + ".long_description");
            tradeSecret.maxLevel = this.levelValues == null ? 0 : this.levelValues.length;
            tradeSecret.levelValues = this.levelValues;
            tradeSecret.costPerLevel = this.costPerLevel;

            this.parent.children.add(tradeSecret);
            tradeSecret.parent = this.parent;
            return tradeSecret;
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
    public Text getLongDescription(){
        return this.longDescription;
    }

}
