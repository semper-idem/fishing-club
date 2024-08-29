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
    private ItemStat biteFailChance = ItemStat.MULTIPLIER_T3;
    private ItemStat autoHookChance = ItemStat.MULTIPLIER_T0;
    private boolean sharp = false;
    private float damage = 0;
    private float reelDamage = 0;
    private boolean sticky = false;
    private final ArrayList<Consumer<LivingEntity>> onEntityHitEffects = new ArrayList<>();
    private ItemStat treasureBonus = ItemStat.BASE_T1;
    private ItemStat treasureRarityBonus = ItemStat.BASE_T1;
    private ItemStat fishRarity = ItemStat.BASE_T1;
    private ItemStat fishRarityMultiplier = ItemStat.MULTIPLIER_T3;
    private ItemStat timeHookedMultiplier = ItemStat.MULTIPLIER_T3;
    private ItemStat waitTimeReductionMultiplier = ItemStat.MULTIPLIER_T3;
    ItemStat fishControl = ItemStat.BASE_T1;
    ItemStat fishControlMultiplier = ItemStat.MULTIPLIER_T3;


    public static final Consumer<LivingEntity> ON_HIT_POISON = targetEntity -> {
        targetEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 200));
    };
    public static final Consumer<LivingEntity> ON_HIT_WITHER = targetEntity -> {
        targetEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 100));
    };
    public static final Consumer<LivingEntity> ON_HIT_FIRE = targetEntity -> {
        targetEntity.setOnFireForTicks(200);
    };


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
        for(Consumer<LivingEntity> onhit : onEntityHitEffects) {
            if (onhit == ON_HIT_POISON) {
                tooltip.add(Text.of("§2Poison I"));

            }
            if (onhit == ON_HIT_FIRE) {
                tooltip.add(Text.of("§4Flame I"));

            }
            if (onhit == ON_HIT_WITHER) {
                tooltip.add(Text.of("§0Wither I"));
            }
        }
        if (this.autoHookChance.value > 0) {
            tooltip.add(Text.of("§7Auto-hook [" + (int)(this.autoHookChance.value * 100) + "%]"));
        }
        super.appendTooltip(stack, context, tooltip, type);
    }

    public HookPartItem(Settings settings) {

        super(settings);
        this.partType = RodConfiguration.PartType.HOOK;
        this.destroyOnBreak = true;
        setDamageMultiplier(DamageSource.CAST, 0);
        setDamageMultiplier(DamageSource.BITE, 1);
        setDamageMultiplier(DamageSource.REEL_FISH, 2);
        setDamageMultiplier(DamageSource.REEL_ENTITY, 5);
        setDamageMultiplier(DamageSource.REEL_GROUND, 1);
    }

    public HookPartItem fishQuality(int fishQuality) {
        this.fishQuality = fishQuality;
        return this;
    }

    public HookPartItem addOnEntityHitEffects(Consumer<LivingEntity> livingEntityConsumer) {
        this.onEntityHitEffects.add(livingEntityConsumer);
        return this;
    }

    public void onEntityHit(LivingEntity entity){
        this.onEntityHitEffects.forEach(o -> o.accept(entity));
    }


    public float getBiteFailChance() {
        return this.biteFailChance.value;
    }

    public HookPartItem biteFailChance(ItemStat biteFailChance) {
        this.biteFailChance = biteFailChance;
        return this;
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

    public HookPartItem sticky(boolean sticky) {
        this.sticky = sticky;
        return this;
    }

    public HookPartItem fishControl(ItemStat fishControl) {
        this.fishControl = fishControl;
        return this;
    }

    public HookPartItem fishControlMultiplier(ItemStat fishControlMultiplier) {
        this.fishControlMultiplier = fishControlMultiplier;
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

    public HookPartItem fishRarityMultiplier(ItemStat fishRarityMultiplier) {

        this.fishRarityMultiplier = fishRarityMultiplier;
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
    void applyComponent(RodConfiguration.AttributeComposite configuration) {

        configuration.fishControl += this.fishControl.value;
        configuration.fishControlMultiplier *= this.fishControlMultiplier.value;
        configuration.baitFailChance = this.biteFailChance.value;
        configuration.timeHookedMultiplier *= this.timeHookedMultiplier.value;
        configuration.waitTimeReductionMultiplier *= this.waitTimeReductionMultiplier.value;
        configuration.fishRarity += this.fishRarity.value;
        configuration.fishRarityMultiplier += this.fishRarityMultiplier.value;
        configuration.treasureBonus += this.treasureBonus.value;
        configuration.treasureRarityBonus += this.treasureRarityBonus.value;
        super.applyComponent(configuration);
    }
}
