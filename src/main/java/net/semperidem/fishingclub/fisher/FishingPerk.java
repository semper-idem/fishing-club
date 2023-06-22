package net.semperidem.fishingclub.fisher;

public class FishingPerk {
    String name;
    String label;
    String description;
    String detailedDescription;
    FishingPerk parent;

    private FishingPerk(String name){
        this.name = name;
        FishingPerks.ALL_PERKS.add(this);
    }
    private FishingPerk(String name, FishingPerk parent){
        this.name = name;
        this.parent = parent;
        FishingPerks.ALL_USABLE_PERKS.add(this);
        FishingPerks.ALL_PERKS.add(this);
    }

    static FishingPerk createRootPerk(String name){
        return new FishingPerk(name);
    }
    static FishingPerk createRootPerk(String name, FishingPerk parent){
        return new FishingPerk(name, parent);
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
}
