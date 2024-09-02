package net.semperidem.fishingclub.fisher.perks.spells;

import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Box;
import net.semperidem.fishingclub.entity.FishermanEntity;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.fisher.perks.TradeSecret;
import net.semperidem.fishingclub.fisher.perks.TradeSecrets;
import net.semperidem.fishingclub.registry.FCStatusEffects;

import java.util.HashMap;
import java.util.List;

public class Spells {
    static Spell SUMMON_RAIN;
    static Spell FISHING_SCHOOL;
    static Spell SLOWER_FISH;
    static Spell EXPERIENCE_BOOST;
    static Spell LUCKY_FISHING;
    static Spell FISHERMAN_LINK;
    static Spell FISHERMAN_SUMMON_REQUEST;
    static Spell FISHERMAN_SUMMON_ACCEPT;
    static Spell MAGIC_ROD_SUMMON;
    static Spell FREE_SHOP_SUMMON;
    static HashMap<TradeSecret, Spell> perkToSpell = new HashMap<>();

    private static void castEffect(ServerPlayerEntity caster, StatusEffectInstance effect){
        Box aoe = new Box(caster.getBlockPos());
        aoe.expand(4);
        List<Entity> iterableEntities = caster.getEntityWorld().getOtherEntities(null, aoe);
        iterableEntities.add(caster);
        iterableEntities.stream().filter(o -> o instanceof ServerPlayerEntity).forEach(o -> ((ServerPlayerEntity) o).addStatusEffect(effect));
    }

    static {
        SUMMON_RAIN = new Spell(TradeSecrets.SUMMON_RAIN.name(), TradeSecrets.SUMMON_RAIN, 72000,   new Spell.Effect() {
            @Override
            public void cast(ServerPlayerEntity source) {
                if (Math.random() < 0.1f) {
                    source.getWorld().setThunderGradient(1);
                }
                source.getWorld().setRainGradient(1);
            }
        });
        FISHING_SCHOOL = new Spell(TradeSecrets.FISHING_SCHOOL.name(), TradeSecrets.FISHING_SCHOOL, 18000,   new Spell.Effect() {
            @Override
            public void cast(ServerPlayerEntity source) {
                castEffect(source, new StatusEffectInstance(FCStatusEffects.BOBBER_BUFF, 6000));
            }
        });
        SLOWER_FISH = new Spell(TradeSecrets.SLOWER_FISH.name(), TradeSecrets.SLOWER_FISH, 18000,  new Spell.Effect() {
            @Override
            public void cast(ServerPlayerEntity source) {
                castEffect(source, new StatusEffectInstance(FCStatusEffects.SLOW_FISH_BUFF, 6000));
            }
        });
        EXPERIENCE_BOOST = new Spell(TradeSecrets.EXPERIENCE_BOOST.name(), TradeSecrets.EXPERIENCE_BOOST, 18000,   new Spell.Effect() {
            @Override
            public void cast(ServerPlayerEntity source) {
                castEffect(source, new StatusEffectInstance(FCStatusEffects.EXP_BUFF,  6000));
            }
        });
        LUCKY_FISHING = new Spell(TradeSecrets.LUCKY_FISHING.name(), TradeSecrets.LUCKY_FISHING, 18000,  new Spell.Effect() {
            @Override
            public void cast(ServerPlayerEntity source){
                castEffect(source, new StatusEffectInstance(FCStatusEffects.QUALITY_BUFF,  6000));
            }
        });
        FISHERMAN_LINK = new Spell(TradeSecrets.FISHERMAN_LINK.name(), TradeSecrets.FISHERMAN_LINK, 6000, new Spell.Effect() {
            @Override
            public void targetedCast(ServerPlayerEntity source, Entity target) {
                FishingCard.of(source).linkTarget(target);
            }
        });
        FISHERMAN_SUMMON_REQUEST = new Spell(TradeSecrets.FISHERMAN_SUMMON.name() + " - Request", TradeSecrets.FISHERMAN_SUMMON, 72000,   new Spell.Effect() {
            @Override
            public void cast(ServerPlayerEntity source) {
                FishingCard.of(source).requestSummon();
            }
        });
        MAGIC_ROD_SUMMON = new Spell(TradeSecrets.MAGIC_ROD_SUMMON.name(), TradeSecrets.MAGIC_ROD_SUMMON, 600,   new Spell.Effect() {
            @Override
            public void cast(ServerPlayerEntity source){
                //todo reimplement since i've removed member rod and cloned rod
//                ItemStack mainHand = source.getStackInHand(Hand.MAIN_HAND);
//                ItemStack rodStack = FCItems.MEMBER_FISHING_ROD.getDefaultStack();
//                if (mainHand ) {
//                    rodStack = source.getStackInHand(Hand.MAIN_HAND);
//                }
//                ItemStack clonedStack = FCItems.CLONED_ROD.getDefaultStack();
//                clonedStack.applyComponentsFrom(rodStack.getComponents());
//                clonedStack.set(FCComponents.CREATION_TICK, source.getWorld().getTime());
//                source.giveItemStack(clonedStack);
            }
        });
        FREE_SHOP_SUMMON = new Spell(TradeSecrets.FREE_SHOP_SUMMON.name(), TradeSecrets.FREE_SHOP_SUMMON, 144000,   new Spell.Effect() {
            @Override
            public void cast(ServerPlayerEntity source){
                //TODO Make only on Derek per world
                //TODO Add condition to spawn only in water or near water
            }
        });
    }

    public static Spell getSpellFromPerk(TradeSecret tradeSecret){
        if (!perkHasSpell(tradeSecret)) return null;
        return perkToSpell.get(tradeSecret);
    }

    public static boolean perkHasSpell(TradeSecret tradeSecret){
        return perkToSpell.containsKey(tradeSecret);
    }
}
