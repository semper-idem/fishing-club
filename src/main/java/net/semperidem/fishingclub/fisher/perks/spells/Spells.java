package net.semperidem.fishingclub.fisher.perks.spells;

import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.semperidem.fishingclub.fisher.perks.FishingPerk;
import net.semperidem.fishingclub.fisher.perks.FishingPerks;
import net.semperidem.fishingclub.registry.FStatusEffectRegistry;

import java.util.HashMap;

public class Spells {
    static Spell SUMMON_RAIN;
    static Spell FISHING_SCHOOL;
    static Spell SLOWER_FISH;
    static Spell EXPERIENCE_BOOST;
    static Spell LUCKY_FISHING;
    static Spell FISHERMAN_LINK;
    static Spell DOUBLE_LINK;
    static Spell FISHERMAN_SUMMON;
    static Spell MAGIC_ROD_SUMMON;
    static Spell FREE_SHOP_SUMMON;
    static HashMap<FishingPerk, Spell> perkToSpell = new HashMap<>();

    private static void castAOEBuff(ServerPlayerEntity playerEntity, StatusEffect buff,int range, int duration){
        Box box = new Box(playerEntity.getBlockPos());
        box.expand(range);
        for(Entity entity : playerEntity.getEntityWorld().getOtherEntities(null, box)) {
            if (entity instanceof PlayerEntity) {
                playerEntity.addStatusEffect(new StatusEffectInstance(buff, duration));
            }
        }
    }

    static {
        SUMMON_RAIN = new Spell(FishingPerks.RAIN_SUMMON.getName(), FishingPerks.RAIN_SUMMON, 72000, playerEntity -> {
            if (!(playerEntity instanceof ServerPlayerEntity)) return;
            ((ServerWorld)playerEntity.world).setWeather(0, 6000, true, Math.random() < 0.1f);
        });
        FISHING_SCHOOL = new Spell(FishingPerks.FISHING_SCHOOL.getName(), FishingPerks.FISHING_SCHOOL, 18000, playerEntity -> {
            if (!(playerEntity instanceof ServerPlayerEntity serverPlayerEntity)) return;
            castAOEBuff(serverPlayerEntity, FStatusEffectRegistry.BOBBER_BUFF, 4, 6000);
        });
        SLOWER_FISH = new Spell(FishingPerks.SLOWER_FISH.getName(), FishingPerks.SLOWER_FISH, 18000, playerEntity -> {
            if (!(playerEntity instanceof ServerPlayerEntity serverPlayerEntity)) return;
            castAOEBuff(serverPlayerEntity, FStatusEffectRegistry.SLOW_FISH_BUFF, 4, 6000);
        });
        EXPERIENCE_BOOST = new Spell(FishingPerks.EXPERIENCE_BOOST.getName(), FishingPerks.EXPERIENCE_BOOST, 18000, playerEntity -> {
            if (!(playerEntity instanceof ServerPlayerEntity serverPlayerEntity)) return;
            castAOEBuff(serverPlayerEntity, FStatusEffectRegistry.EXP_BUFF, 4, 6000);
        });
        LUCKY_FISHING = new Spell(FishingPerks.LUCKY_FISHING.getName(), FishingPerks.LUCKY_FISHING, 18000, playerEntity -> {
            if (!(playerEntity instanceof ServerPlayerEntity serverPlayerEntity)) return;
            castAOEBuff(serverPlayerEntity, FStatusEffectRegistry.QUALITY_BUFF, 4, 6000);
        });
        FISHERMAN_LINK = new Spell(FishingPerks.FISHERMAN_LINK.getName(), FishingPerks.FISHERMAN_LINK, 600, playerEntity -> {
            if (!(playerEntity instanceof ServerPlayerEntity)) return;
        });
        DOUBLE_LINK = new Spell(FishingPerks.DOUBLE_LINK.getName(), FishingPerks.DOUBLE_LINK, 600, playerEntity -> {
            if (!(playerEntity instanceof ServerPlayerEntity)) return;
        });
        FISHERMAN_SUMMON = new Spell(FishingPerks.FISHERMAN_SUMMON.getName(), FishingPerks.FISHERMAN_SUMMON, 600, playerEntity -> {
            if (!(playerEntity instanceof ServerPlayerEntity)) return;
        });
        MAGIC_ROD_SUMMON = new Spell(FishingPerks.MAGIC_ROD_SUMMON.getName(), FishingPerks.MAGIC_ROD_SUMMON, 600, playerEntity -> {
            if (!(playerEntity instanceof ServerPlayerEntity)) return;
        });
        FREE_SHOP_SUMMON = new Spell(FishingPerks.FREE_SHOP_SUMMON.getName(), FishingPerks.FREE_SHOP_SUMMON, 600, playerEntity -> {
            if (!(playerEntity instanceof ServerPlayerEntity)) return;
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
