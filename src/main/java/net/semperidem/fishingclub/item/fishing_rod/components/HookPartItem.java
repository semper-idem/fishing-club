package net.semperidem.fishingclub.item.fishing_rod.components;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class HookPartItem extends PartItem {
    ItemStat fishControl = ItemStat.BASE_T1;
    ItemStat biteFailChance = ItemStat.MULTIPLIER_T3;
    ItemStat autoHookChance = ItemStat.MULTIPLIER_T0;
    ItemStat treasureBonus = ItemStat.BASE_T1;
    ItemStat treasureRarityBonus = ItemStat.BASE_T1;
    ItemStat fishRarity = ItemStat.BASE_T1;
    ItemStat timeHookedMultiplier = ItemStat.MULTIPLIER_T3;
    ItemStat waitTimeReductionMultiplier = ItemStat.MULTIPLIER_T3;
    boolean sharp = false;
    float damage = 0;
    float reelDamage = 0;
    boolean sticky = false;
    final ArrayList<Consumer<LivingEntity>> onEntityHitEffects = new ArrayList<>();

    public HookPartItem(Settings settings) {
        super(settings);
        this.type = RodConfiguration.PartType.HOOK;
    }

    public HookPartItem weightClass(int weightClass) {
        this.weightClass = weightClass;
        return this;
    }

    public HookPartItem minOperatingTemperature(int minOperatingTemperature) {
        this.minOperatingTemperature = minOperatingTemperature;
        return this;
    }

    public HookPartItem maxOperatingTemperature(int maxOperatingTemperature) {
        this.maxOperatingTemperature = maxOperatingTemperature;
        return this;
    }

    public HookPartItem fishQuality(int fishQuality) {
        this.fishQuality = fishQuality;
        return this;
    }

    public HookPartItem addOnEntityHitEffects(Consumer<LivingEntity> livingEntityConsumer) {
        this.onEntityHitEffects.add(livingEntityConsumer);
        return this;
    }

    public HookPartItem biteFailChance(ItemStat biteFailChance) {
        this.biteFailChance = biteFailChance;
        return this;
    }

    public HookPartItem sticky(boolean sticky) {
        this.sticky = sticky;
        return this;
    }

    public HookPartItem fishControl(ItemStat fishControl) {
        this.fishControl = fishControl;
        return this;
    }

    public HookPartItem treasureBonus(ItemStat treasureBonus) {
        this.treasureBonus = treasureBonus;
        return this;
    }

    public HookPartItem treasureRarityBonus(ItemStat treasureRarityBonus) {
        this.treasureRarityBonus = treasureRarityBonus;
        return this;
    }

    public HookPartItem fishRarity(ItemStat fishRarity) {
        this.fishRarity = fishRarity;
        return this;
    }

    public HookPartItem waitTimeReductionMultiplier(ItemStat waitTimeReductionMultiplier) {
        this.waitTimeReductionMultiplier = waitTimeReductionMultiplier  ;
        return this;
    }

    public HookPartItem timeHookedMultiplier(ItemStat timeHookedMultiplier) {
        this.timeHookedMultiplier = timeHookedMultiplier;
        return this;
    }

    @Override
    void apply(RodConfiguration.AttributeComposite attributes) {
        attributes.fishControl += this.fishControl.value;
        attributes.baitFailChance = this.biteFailChance.value;
        attributes.timeHookedMultiplier *= this.timeHookedMultiplier.value;
        attributes.waitTimeReductionMultiplier *= this.waitTimeReductionMultiplier.value;
        attributes.fishRarity += this.fishRarity.value;
        attributes.treasureBonus += this.treasureBonus.value;
        attributes.treasureRarityBonus += this.treasureRarityBonus.value;
        super.apply(attributes);
    }

    public void onEntityHit(LivingEntity entity){
        this.onEntityHitEffects.forEach(o -> o.accept(entity));
    }

    public float getBiteFailChance() {
        return this.biteFailChance.value;
    }

    public float getAutoHookChance() {
        return this.autoHookChance.value;
    }

    public HookPartItem autoHookChance(ItemStat autoHookChance) {
        this.autoHookChance = autoHookChance;
        return this;
    }

    public boolean isSharp() {
        return this.sharp;
    }

    public HookPartItem sharp(boolean sharp) {
        this.sharp = sharp;
        return this;
    }

    public float getDamage() {
        return this.damage;
    }

    public HookPartItem damage(float damage) {
        this.damage = damage;
        return this;
    }

    public float getReelDamage() {
        return this.reelDamage;
    }

    public HookPartItem reelDamage(float reelDamage) {
        this.reelDamage = reelDamage;
        return this;
    }

    public boolean isSticky() {
        return this.sticky;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        if (!InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), InputUtil.GLFW_KEY_LEFT_SHIFT)) {
            tooltip.add(Text.of("§8Left Shift for additional info"));
            return;
        }
        if (this.sticky) {
            tooltip.add(Text.of("§aSticky"));
        }
        if (this.damage > 0) {
            tooltip.add(Text.of("§7Sharp [ >> §4" + (int)this.damage + "§7]"));
        }
        if (this.reelDamage > 0) {
            tooltip.add(Text.of("§7Serrated [ << §4" + (int)this.reelDamage + "§7]"));
        }
        for(Consumer<LivingEntity> onHit : onEntityHitEffects) {
            if (onHit == ON_HIT_POISON) {
                tooltip.add(Text.of("§2Poison I"));

            }
            if (onHit == ON_HIT_FIRE) {
                tooltip.add(Text.of("§4Flame I"));

            }
            if (onHit == ON_HIT_WITHER) {
                tooltip.add(Text.of("§0Wither I"));
            }
        }
        if (this.autoHookChance.value > 0) {
            tooltip.add(Text.of("§7Auto-hook [" + (int)(this.autoHookChance.value * 100) + "%]"));
        }
        super.appendTooltip(stack, context, tooltip, type);
    }
    public static final Consumer<LivingEntity> ON_HIT_POISON = targetEntity -> targetEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 200));
    public static final Consumer<LivingEntity> ON_HIT_WITHER = targetEntity -> targetEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 100));
    public static final Consumer<LivingEntity> ON_HIT_FIRE = targetEntity -> targetEntity.setOnFireForTicks(200);

}
