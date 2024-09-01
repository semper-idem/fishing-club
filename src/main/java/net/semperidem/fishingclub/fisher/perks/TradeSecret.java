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
    List<Text> shortDescription;
    List<Text> longDescription;
    TradeSecret parent;
    List<TradeSecret> children = new ArrayList<>();
    Identifier texture;
    int maxLevel = 0;
    int[] levelValues;

    private TradeSecret(int id) {
        this.id = id;
    }

    static Builder builder() {
        return new Builder();
    }

    static class Builder {
        private final int id;
        private String name;
        private String shortDescription;
        private String longDescription;
        private TradeSecret parent;
        private int[] levelValues;

        Builder() {
            id = ID_TO_SKILL.size();
        }

        TradeSecret build() {
            TradeSecret tradeSecret = new TradeSecret(this.id);
            ID_TO_SKILL.put(this.id, tradeSecret);

            tradeSecret.label = Text.of(this.name);
            tradeSecret.texture = FishingClub.identifier("textures/gui/skill/" + this.name);
            tradeSecret.shortDescription = Arrays.stream(this.shortDescription.split("\n")).map(Text::of).toList();
            tradeSecret.longDescription = Arrays.stream(this.longDescription.split("\n")).map(Text::of).toList();
            tradeSecret.maxLevel = this.levelValues == null ? 0 : this.levelValues.length;
            tradeSecret.levelValues = this.levelValues;

            this.parent.children.add(tradeSecret);
            tradeSecret.parent = this.parent;
            return tradeSecret;
        }

        Builder levelValues(int... levelValues) {
            this.levelValues = levelValues;
            return this;
        }
        Builder longDescription(String longDescription) {
            this.longDescription = longDescription;
            return this;
        }

        Builder shortDescription(String shortDescription) {
            this.shortDescription = shortDescription;
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
    public List<Text> getShortDescription(){
        return this.shortDescription;
    }
    public List<Text> getLongDescription(){
        return this.longDescription;
    }

}
