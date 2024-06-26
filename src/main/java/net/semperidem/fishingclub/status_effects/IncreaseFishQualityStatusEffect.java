package net.semperidem.fishingclub.status_effects;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

import static java.awt.Color.CYAN;
import static net.minecraft.entity.effect.StatusEffectCategory.BENEFICIAL;

public class IncreaseFishQualityStatusEffect extends StatusEffect {
    public IncreaseFishQualityStatusEffect() {
        super(BENEFICIAL, CYAN.getRGB());
    }
}
