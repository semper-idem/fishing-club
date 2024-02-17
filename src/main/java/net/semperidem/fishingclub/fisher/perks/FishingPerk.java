package net.semperidem.fishingclub.fisher.perks;

import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.semperidem.fishingclub.FishingClub;

import java.util.ArrayList;

import static net.semperidem.fishingclub.fisher.perks.FishingPerks.NAME_TO_PERK_MAP;
import static net.semperidem.fishingclub.fisher.perks.FishingPerks.SKILL_TREE;

public class FishingPerk {
    String name;
    String label;
    ArrayList<Text> description;
    ArrayList<Text> detailedDescription;
    FishingPerk parent;
    FishingPerk child;
    Path path;
    Identifier icon;
    PerkReward reward;


    FishingPerk(String name){
        this.name = name;
    }

    FishingPerk(String name, Path path){
        this.name = name;
        this.path = path;
        NAME_TO_PERK_MAP.put(name, this);
        if (!SKILL_TREE.containsKey(path)) {
            SKILL_TREE.put(path, new ArrayList<>());
        }
        SKILL_TREE.get(path).add(this);
    }
    FishingPerk(String name, FishingPerk parent){
        this.name = name;
        this.path = parent.path;
        this.parent = parent;
        parent.child = this;
        NAME_TO_PERK_MAP.put(name, this);
    }

    public void onEarn(PlayerEntity playerEntity){
        if (reward == null) return;
        if (!(playerEntity instanceof ServerPlayerEntity)) return;
        reward.onEarn(playerEntity);
    }

    public Path getPath() {
        return this.path;
    }

    public FishingPerk getChild(){
        return this.child;
    }

    public FishingPerk withIcon(String iconName){
        this.icon = new Identifier(FishingClub.MOD_ID, "textures/gui/skill/" + iconName);
        return this;
    }

    public FishingPerk withReward(PerkReward perkReward){
        this.reward = perkReward;
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
    public FishingPerk getParent(){
        return parent;
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

    static void grantAdvancement(PlayerEntity player, Identifier advancementIdentifier){
        Advancement advancement = player.getServer().getAdvancementLoader().get(advancementIdentifier);
        AdvancementProgress advancementProgress =  ((ServerPlayerEntity)player).getAdvancementTracker().getProgress(advancement);
        for(String criterion : advancementProgress.getUnobtainedCriteria()) {
            ((ServerPlayerEntity)player).getAdvancementTracker().grantCriterion(advancement, criterion);
        }
    }
}
