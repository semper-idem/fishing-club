package net.semperidem.fishingclub.fisher;

import java.util.ArrayList;

public class FishingPerk {
    String name;
    String label;
    String description;
    String detailedDescription;
    FishingPerk parent;
    ArrayList<FishingPerk> children = new ArrayList<>();

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
        this.description = description;
        return this;
    }

    FishingPerk withDetailedDesc(String detailedDescription){
        this.detailedDescription = detailedDescription;
        return this;
    }

    public String getLabel(){
        return label;
    }
    public String getDescription(){
        return description;
    }
    public String getDetailedDescription(){
        return detailedDescription;
    }
}
