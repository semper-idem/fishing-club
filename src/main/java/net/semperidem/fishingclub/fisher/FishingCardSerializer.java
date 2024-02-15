package net.semperidem.fishingclub.fisher;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.semperidem.fishingclub.fisher.perks.FishingPerk;
import net.semperidem.fishingclub.fisher.perks.FishingPerks;
import net.semperidem.fishingclub.fisher.perks.spells.SpellInstance;

public class FishingCardSerializer {
    public static final String TAG = "fishing_card";
    private static final String LEVEL_TAG = "lvl";
    private static final String EXP_TAG = "xp";
    private static final String CREDIT_TAG = "c";
    private static final String SKILL_POINTS_TAG = "sp";
    private static final String INVENTORY_TAG ="inv";
    public static final String PERKS_TAG ="perks";
    public static final String SPELLS_TAG ="spells";

    public static FishingCard fromNbt(PlayerEntity owner, NbtCompound nbt){
        FishingCard fishingCard = new FishingCard(owner);
        readNbt(fishingCard, nbt.contains(TAG) ? nbt.getCompound(TAG) : nbt);
        return fishingCard;
    }

    private static void readNbt(FishingCard fishingCard, NbtCompound fisherTag) {
        fishingCard.level = fisherTag.getInt(LEVEL_TAG);
        fishingCard.exp = fisherTag.getInt(EXP_TAG);
        fishingCard.credit = fisherTag.getInt(CREDIT_TAG);
        fishingCard.skillPoints = fisherTag.getInt(SKILL_POINTS_TAG);
        setPerks(fisherTag, fishingCard);
        setSpells(fisherTag, fishingCard);

        fishingCard.historyManager.readNbt(fisherTag);
        fishingCard.summonRequestManager.readNbt(fisherTag);
        fishingCard.linkingManager.readNbt(fisherTag);
        Inventories.readNbt(fisherTag, fishingCard.inventory);
    }

    public static NbtCompound toNbt(FishingCard fishingCard){
        NbtCompound fisherTag = new NbtCompound();
        fisherTag.putInt(LEVEL_TAG, fishingCard.level);
        fisherTag.putInt(EXP_TAG, fishingCard.exp);
        fisherTag.putInt(CREDIT_TAG, fishingCard.credit);
        fisherTag.putInt(SKILL_POINTS_TAG, fishingCard.skillPoints);
        fisherTag.put(PERKS_TAG, getPerkListTag(fishingCard));
        fisherTag.put(SPELLS_TAG, getSpellListTag(fishingCard));;
        writeNbt(fishingCard, fisherTag);
        return fisherTag;
    }

    private static void writeNbt(FishingCard fishingCard, NbtCompound fisherTag) {
        fishingCard.historyManager.writeNbt(fisherTag);
        fishingCard.summonRequestManager.writeNbt(fisherTag);
        fishingCard.linkingManager.writeNbt(fisherTag);
        Inventories.writeNbt(fisherTag, fishingCard.inventory);
    }



    public static NbtList getSpellListTag(FishingCard fishingCard){
        NbtList spellListTag = new NbtList();
        for(SpellInstance spellInstance : fishingCard.spells.values()) {
            spellListTag.add(SpellInstance.toNbt(spellInstance));
        }
        return spellListTag;
    }

    public static NbtList getPerkListTag(FishingCard fishingCard){
        NbtList perkListTag = new NbtList();
        fishingCard.perks.forEach((fishingPerkName, fishingPerk) -> {
            perkListTag.add(NbtString.of(fishingPerkName));
        });
        return perkListTag;
    }

    private static void setPerks(NbtCompound fisherTag, FishingCard fishingCard){
        fishingCard.perks.clear();
        NbtList perkListTag = fisherTag.getList(PERKS_TAG, NbtElement.STRING_TYPE);
        if (perkListTag.isEmpty()) {
            initPerks(fishingCard);
        } else {
            perkListTag.forEach(
                    nbtElement -> FishingPerks.getPerkFromName(nbtElement.asString()).ifPresent(
                            fishingPerk -> fishingCard.perks.put(fishingPerk.getName(), fishingPerk)));
        }
    }
    private static void initPerks(FishingCard fishingCard){
        addRootPerk(fishingCard, FishingPerks.ROOT_HOBBYIST);
        addRootPerk(fishingCard, FishingPerks.ROOT_OPPORTUNIST);
        addRootPerk(fishingCard, FishingPerks.ROOT_SOCIALIST);
    }
    private static void addRootPerk(FishingCard fishingCard, FishingPerk perk){
        fishingCard.perks.put(perk.getName(), perk);
    }
    private static void setSpells(NbtCompound fisherTag, FishingCard fishingCard){
        fishingCard.spells.clear();
        NbtList spellListTag = fisherTag.getList(SPELLS_TAG, NbtElement.COMPOUND_TYPE);
        for(int i = 0; i < spellListTag.size(); i++) {
            SpellInstance spellInstance = SpellInstance.fromNbt(spellListTag.getCompound(i));
            fishingCard.spells.put(spellInstance.getPerk(), spellInstance);
        }
    }

}
