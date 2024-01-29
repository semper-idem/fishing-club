package net.semperidem.fishingclub.fisher;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.semperidem.fishingclub.fisher.perks.FishingPerk;
import net.semperidem.fishingclub.fisher.perks.FishingPerks;
import net.semperidem.fishingclub.fisher.perks.spells.SpellInstance;
import net.semperidem.fishingclub.network.ServerPacketSender;
import net.semperidem.fishingclub.util.InventoryUtil;

import java.util.ArrayList;
import java.util.UUID;

public class FishingCardSerializer {
    public static final String TAG = "fishing_card";
    private static final String LEVEL_TAG = "lvl";
    private static final String EXP_TAG = "xp";
    private static final String CREDIT_TAG = "c";
    private static final String SKILL_POINTS_TAG = "sp";
    private static final String LAST_CATCH_TIMESTAMP_TAG ="lc_ts";
    private static final String FIRST_CATCH_OF_THE_DAY_TIMESTAMP_TAG ="fcotd_ts";
    private static final String INVENTORY_TAG ="inv";
    public static final String PERKS_TAG ="perks";
    public static final String SPELLS_TAG ="spells";
    private static final String LAST_USED_BAIT ="lbait";
    private static final String LINKED_PLAYERS_TAG ="lp";

    public static FishingCard fromNbt(PlayerEntity owner, NbtCompound playerNbt){
        FishingCard fishingCard = new FishingCard(owner);
        if (playerNbt.contains(TAG)) {
            readNbt(fishingCard, playerNbt.getCompound(TAG));
        }
        return fishingCard;
    }

    private static void readNbt(FishingCard fishingCard, NbtCompound fisherTag) {
        fishingCard.level = fisherTag.getInt(LEVEL_TAG);
        fishingCard.exp = fisherTag.getInt(EXP_TAG);
        fishingCard.credit = fisherTag.getInt(CREDIT_TAG);
        fishingCard.skillPoints = fisherTag.getInt(SKILL_POINTS_TAG);
        fishingCard.lastFishCaughtTime = fisherTag.getLong(LAST_CATCH_TIMESTAMP_TAG);
        fishingCard.firstFishOfTheDayCaughtTime = fisherTag.getLong(FIRST_CATCH_OF_THE_DAY_TIMESTAMP_TAG);
        fishingCard.fisherInventory = InventoryUtil.readInventory(fisherTag.getCompound(INVENTORY_TAG));
        fishingCard.lastUsedBait = ItemStack.fromNbt(fisherTag.getCompound(LAST_USED_BAIT));
        setPerks(fisherTag, fishingCard);
        setSpells(fisherTag, fishingCard);

        fishingCard.chunkManager.readNbt(fisherTag);
        fishingCard.summonRequestManager.readNbt(fisherTag);
        fishingCard.linkingManager.readNbt(fisherTag);
    }

    public static NbtCompound toNbt(FishingCard fishingCard){
        NbtCompound fisherTag = new NbtCompound();
        fisherTag.putInt(LEVEL_TAG, fishingCard.level);
        fisherTag.putInt(EXP_TAG, fishingCard.exp);
        fisherTag.putInt(CREDIT_TAG, fishingCard.credit);
        fisherTag.putInt(SKILL_POINTS_TAG, fishingCard.skillPoints);
        fisherTag.putLong(LAST_CATCH_TIMESTAMP_TAG, fishingCard.lastFishCaughtTime);
        fisherTag.putLong(FIRST_CATCH_OF_THE_DAY_TIMESTAMP_TAG, fishingCard.firstFishOfTheDayCaughtTime);
        fisherTag.put(INVENTORY_TAG, InventoryUtil.writeInventory(fishingCard.fisherInventory));
        fisherTag.put(PERKS_TAG, getPerkListTag(fishingCard));
        fisherTag.put(SPELLS_TAG, getSpellListTag(fishingCard));
        fisherTag.put(LAST_USED_BAIT, fishingCard.lastUsedBait.writeNbt(new NbtCompound()));
        writeNbt(fishingCard, fisherTag);
        return fisherTag;
    }

    private static void writeNbt(FishingCard fishingCard, NbtCompound fisherTag) {
        fishingCard.chunkManager.writeNbt(fisherTag);
        fishingCard.summonRequestManager.writeNbt(fisherTag);
        fishingCard.linkingManager.writeNbt(fisherTag);
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
        ServerPacketSender.sendPerksUpdate(fishingCard);
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
        ServerPacketSender.sendSpellsUpdate(fishingCard);
    }

}
