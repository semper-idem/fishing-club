package net.semperidem.fishingclub.fisher.perks.spells;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.fisher.perks.FishingPerk;
import net.semperidem.fishingclub.fisher.perks.FishingPerks;
import net.semperidem.fishingclub.registry.EntityTypeRegistry;
import net.semperidem.fishingclub.registry.ItemRegistry;
import net.semperidem.fishingclub.registry.StatusEffectRegistry;

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
    static Spell SHARED_BAIT;
    static Spell MAGIC_ROD_SUMMON;
    static Spell FREE_SHOP_SUMMON;
    static HashMap<FishingPerk, Spell> perkToSpell = new HashMap<>();

    private static void castEffect(ServerPlayerEntity caster,int range, StatusEffectInstance effect){
        Box aoe = new Box(caster.getBlockPos());
        aoe.expand(range);
        List<Entity> iterableEntities = caster.getEntityWorld().getOtherEntities(null, aoe);
        iterableEntities.add(caster);
        iterableEntities.stream().filter(o -> o instanceof ServerPlayerEntity).forEach(o -> ((ServerPlayerEntity) o).addStatusEffect(effect));
    }

    static {
        SUMMON_RAIN = new Spell(FishingPerks.RAIN_SUMMON.getName(), FishingPerks.RAIN_SUMMON, 72000,   new Spell.Effect() {
            @Override
            public void cast(ServerPlayerEntity source) {
                if (Math.random() < 0.1f) {
                    source.getWorld().setThunderGradient(1);
                }
                source.getWorld().setRainGradient(1);
            }
        });
        FISHING_SCHOOL = new Spell(FishingPerks.FISHING_SCHOOL.getName(), FishingPerks.FISHING_SCHOOL, 18000,   new Spell.Effect() {
            @Override
            public void cast(ServerPlayerEntity source) {
                castEffect(source, 4, new StatusEffectInstance(StatusEffectRegistry.BOBBER_BUFF, 6000));
            }
        });
        SLOWER_FISH = new Spell(FishingPerks.SLOWER_FISH.getName(), FishingPerks.SLOWER_FISH, 18000,  new Spell.Effect() {
            @Override
            public void cast(ServerPlayerEntity source) {
                castEffect(source, 4, new StatusEffectInstance(StatusEffectRegistry.SLOW_FISH_BUFF, 6000));
            }
        });
        EXPERIENCE_BOOST = new Spell(FishingPerks.EXPERIENCE_BOOST.getName(), FishingPerks.EXPERIENCE_BOOST, 18000,   new Spell.Effect() {
            @Override
            public void cast(ServerPlayerEntity source) {
                castEffect(source, 4, new StatusEffectInstance(StatusEffectRegistry.EXP_BUFF,  6000));
            }
        });
        LUCKY_FISHING = new Spell(FishingPerks.LUCKY_FISHING.getName(), FishingPerks.LUCKY_FISHING, 18000,  new Spell.Effect() {
            @Override
            public void cast(ServerPlayerEntity source){
                castEffect(source, 4, new StatusEffectInstance(StatusEffectRegistry.QUALITY_BUFF,  6000));
            }
        });
        FISHERMAN_LINK = new Spell(FishingPerks.FISHERMAN_LINK.getName(), FishingPerks.FISHERMAN_LINK, 6000, new Spell.Effect() {
            @Override
            public void targetedCast(ServerPlayerEntity source, Entity target) {
                FishingCard.of(source).linkTarget(target);
            }
        });
        SHARED_BAIT = new Spell(FishingPerks.SHARED_BAIT.getName(), FishingPerks.SHARED_BAIT, 6000,   new Spell.Effect() {
            @Override
            public void cast(ServerPlayerEntity source) {
                FishingCard.of(source).shareBait();
            }
        });
        FISHERMAN_SUMMON_REQUEST = new Spell(FishingPerks.FISHERMAN_SUMMON.getName() + " - Request", FishingPerks.FISHERMAN_SUMMON, 72000,   new Spell.Effect() {
            @Override
            public void cast(ServerPlayerEntity source) {
                FishingCard.of(source).requestSummon();;
            }
        });
        MAGIC_ROD_SUMMON = new Spell(FishingPerks.MAGIC_ROD_SUMMON.getName(), FishingPerks.MAGIC_ROD_SUMMON, 600,   new Spell.Effect() {
            @Override
            public void cast(ServerPlayerEntity source){
                ItemStack mainHand = source.getStackInHand(Hand.MAIN_HAND);
                ItemStack rodStack = ItemRegistry.MEMBER_FISHING_ROD.getDefaultStack();
                if (mainHand.getItem().equals(ItemRegistry.MEMBER_FISHING_ROD)) {
                    rodStack = source.getStackInHand(Hand.MAIN_HAND);
                }
                ItemStack clonedStack = ItemRegistry.CLONED_ROD.getDefaultStack();
                NbtCompound clonedNbt = rodStack.getNbt();
                clonedNbt.putLong("creation_tick", source.getWorld().getTime());
                source.giveItemStack(clonedStack);
            }
        });
        FREE_SHOP_SUMMON = new Spell(FishingPerks.FREE_SHOP_SUMMON.getName(), FishingPerks.FREE_SHOP_SUMMON, 144000,   new Spell.Effect() {
            @Override
            public void cast(ServerPlayerEntity source){
                //TODO Make only on Derek per world
                //TODO Add condition to spawn only in water or near water
                //ServerWorld world, @Nullable NbtCompound itemNbt, @Nullable Text name, @Nullable PlayerEntity player, BlockPos pos, SpawnReason spawnReason, boolean alignPosition, boolean invertY) {
                EntityTypeRegistry.DEREK_ENTITY.spawn(source.getWorld(), null, null, null, source.getBlockPos(), SpawnReason.MOB_SUMMONED, true, false);
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
