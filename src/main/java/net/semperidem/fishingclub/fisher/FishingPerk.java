package net.semperidem.fishingclub.fisher;

import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.semperidem.fishingclub.FishingClub;

import java.util.ArrayList;

public class FishingPerk {
    String name;
    String label;
    ArrayList<Text> description;
    ArrayList<Text> detailedDescription;
    FishingPerk parent;
    ArrayList<FishingPerk> children = new ArrayList<>();
    Identifier icon;

    private FishingPerk(String name){
        this.name = name;
        this.label =  name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
        FishingPerks.ALL_PERKS.add(this);
    }
    private FishingPerk(String name, FishingPerk parent){
        this.name = name;
        this.parent = parent;
        this.parent.addChild(this);
        FishingPerks.ALL_USABLE_PERKS.add(this);
        FishingPerks.ALL_PERKS.add(this);
    }

    static FishingPerk createPerk(String name){
        return new FishingPerk(name);
    }
    static FishingPerk createPerk(String name, FishingPerk parent){
        return new FishingPerk(name, parent);
    }

    void addChild(FishingPerk fishingPerk){
        this.children.add(fishingPerk);
    }

    public ArrayList<FishingPerk> getChildren(){
        return this.children;
    }

    public boolean hasChildren(){
        return this.children.size() > 0;
    }

    public FishingPerk withIcon(String iconName){
        this.icon = new Identifier(FishingClub.MOD_ID, "textures/gui/skill/" + iconName);
        return this;
    }

    public Identifier getIcon(){
        return this.icon;
    }
    public boolean parentIsRoot(){
        if (this.parent == null) {
            return false;
        }
        return this.parent.parent != null;
    }

    public String getName(){
        return name;
    }

    FishingPerk withLabel(String label){
        this.label = label;
        return this;
    }
    FishingPerk withDescription(String description){
        this.description = splitLine(description);
        return this;
    }

    FishingPerk withDetailedDesc(String detailedDescription){
        this.detailedDescription = splitLine(detailedDescription);
        return this;
    }

    private ArrayList<Text> splitLine(String text){
        String[] lines = text.split("\n");
        ArrayList<Text> result = new ArrayList<>();
        for(String line : lines) {
                result.add(Text.of(line));
        }
        return result;
    }

    public String getLabel(){
        return label;
    }
    public ArrayList<Text> getDescription(){
        return description;
    }
    public ArrayList<Text> getDetailedDescription(){
        return detailedDescription;
    }
}
