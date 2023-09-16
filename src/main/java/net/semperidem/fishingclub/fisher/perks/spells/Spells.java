package net.semperidem.fishingclub.fisher.perks.spells;

import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;
import net.semperidem.fishingclub.fisher.FisherInfo;
import net.semperidem.fishingclub.fisher.FisherInfoManager;
import net.semperidem.fishingclub.fisher.perks.FishingPerk;
import net.semperidem.fishingclub.fisher.perks.FishingPerks;
import net.semperidem.fishingclub.registry.FStatusEffectRegistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Spells {
    static Spell SUMMON_RAIN;
    static Spell FISHING_SCHOOL;
    static Spell SLOWER_FISH;
    static Spell EXPERIENCE_BOOST;
    static Spell LUCKY_FISHING;
    static Spell FISHERMAN_LINK;
    static Spell FISHERMAN_SUMMON;
    static Spell MAGIC_ROD_SUMMON;
    static Spell FREE_SHOP_SUMMON;
    static HashMap<FishingPerk, Spell> perkToSpell = new HashMap<>();

    private static final int MAX_LINKED_FISHER_BUFFS = 8;

    private static void castAOEBuff(ServerPlayerEntity playerEntity, StatusEffect buff,int range, int duration){
        Box box = new Box(playerEntity.getBlockPos());
        box.expand(range);
        ArrayList<UUID> buffedLinks = new ArrayList<>();
        int linkedCount = 0;
        for(Entity entity : playerEntity.getEntityWorld().getOtherEntities(null, box)) {
            if (!(entity instanceof PlayerEntity)) continue;
            playerEntity.addStatusEffect(new StatusEffectInstance(buff, duration));
            FisherInfo fisherInfo = FisherInfoManager.getFisher(playerEntity);
            if (!fisherInfo.hasPerk(FishingPerks.FISHERMAN_LINK)) continue;
            if (linkedCount >= MAX_LINKED_FISHER_BUFFS) continue;
            linkedCount++;
            for(UUID linkedFisher : FisherInfoManager.getFisher(playerEntity).getLinkedFishers()) {
                if (buffedLinks.contains(linkedFisher)) continue;
                for(ServerPlayerEntity linkedFisherPlayer : playerEntity.getWorld().getPlayers()) {
                    if (linkedFisherPlayer.getUuid().equals(linkedFisher)) continue;
                    linkedFisherPlayer.addStatusEffect(new StatusEffectInstance(buff, duration));
                    buffedLinks.add(linkedFisher);
                    //MAYBE REFACTOR HERE IDK :P n^3 big bad
                }
            }
        }
    }

    static {
        SUMMON_RAIN = new Spell(FishingPerks.RAIN_SUMMON.getName(), FishingPerks.RAIN_SUMMON, 72000,   new Spell.Effect() {
            @Override
            public void cast(ServerPlayerEntity source) {
                (source.getWorld()).setWeather(0, 6000, true, Math.random() < 0.1f);
            }
        });
        FISHING_SCHOOL = new Spell(FishingPerks.FISHING_SCHOOL.getName(), FishingPerks.FISHING_SCHOOL, 18000,   new Spell.Effect() {
            @Override
            public void cast(ServerPlayerEntity source) {
                castAOEBuff(source, FStatusEffectRegistry.BOBBER_BUFF, 4, 6000);
            }
        });
        SLOWER_FISH = new Spell(FishingPerks.SLOWER_FISH.getName(), FishingPerks.SLOWER_FISH, 18000,  new Spell.Effect() {
            @Override
            public void cast(ServerPlayerEntity source) {
                castAOEBuff(source, FStatusEffectRegistry.SLOW_FISH_BUFF, 4, 6000);
            }
        });
        EXPERIENCE_BOOST = new Spell(FishingPerks.EXPERIENCE_BOOST.getName(), FishingPerks.EXPERIENCE_BOOST, 18000,   new Spell.Effect() {
            @Override
            public void cast(ServerPlayerEntity source) {
                castAOEBuff(source, FStatusEffectRegistry.EXP_BUFF, 4, 6000);
            }
        });
        LUCKY_FISHING = new Spell(FishingPerks.LUCKY_FISHING.getName(), FishingPerks.LUCKY_FISHING, 18000,  new Spell.Effect() {
            @Override
            public void cast(ServerPlayerEntity source){
                castAOEBuff(source, FStatusEffectRegistry.QUALITY_BUFF, 4, 6000);
            }
        });
        FISHERMAN_LINK = new Spell(FishingPerks.FISHERMAN_LINK.getName(), FishingPerks.FISHERMAN_LINK, 600, new Spell.Effect() {
            @Override
            public void targetedCast(ServerPlayerEntity source, Entity target) {
                if (!(target instanceof PlayerEntity playerTarget)) return;
                FisherInfo fisherInfo = FisherInfoManager.getFisher(source);
                UUID targetUUID = target.getUuid();
                if (fisherInfo.isLinked(target.getUuid())) {
                    fisherInfo.unlinkFisher(targetUUID);
                    source.sendMessage(Text.of("[Fishing Club] Unlinked from: " + playerTarget.getDisplayName().getString()), true);
                    playerTarget.sendMessage(Text.of("[Fishing Club]" + source.getDisplayName().getString() + " has unlinked from you"), true);
                } else {
                    FisherInfoManager.getFisher(source).linkedFisher(target.getUuid());
                    source.sendMessage(Text.of("[Fishing Club] Linked to: " + playerTarget.getDisplayName().getString()), true);
                    playerTarget.sendMessage(Text.of("[Fishing Club]" + source.getDisplayName().getString() + " has linked to you"), true);
                }
            }
        });
        FISHERMAN_SUMMON = new Spell(FishingPerks.FISHERMAN_SUMMON.getName(), FishingPerks.FISHERMAN_SUMMON, 600,   new Spell.Effect() {
            @Override
            public void cast(ServerPlayerEntity source) {
            }
        });
        MAGIC_ROD_SUMMON = new Spell(FishingPerks.MAGIC_ROD_SUMMON.getName(), FishingPerks.MAGIC_ROD_SUMMON, 600,   new Spell.Effect() {
            @Override
            public void cast(ServerPlayerEntity source){

            }
        });
        FREE_SHOP_SUMMON = new Spell(FishingPerks.FREE_SHOP_SUMMON.getName(), FishingPerks.FREE_SHOP_SUMMON, 600,   new Spell.Effect() {
            @Override
            public void cast(ServerPlayerEntity source){

            }
        });
    }

    public static Spell getSpellFromPerk(FishingPerk fishingPerk){
        if (!perkHasSpell(fishingPerk)) return null;
        return perkToSpell.get(fishingPerk);
    }

    public static boolean perkHasSpell(FishingPerk fishingPerk){
        return perkToSpell.containsKey(fishingPerk);
    }
}
