package net.semperidem.fishingclub.status_effects;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

import java.awt.*;

public class OneTimeIncreaseFishQualityStatusEffect extends StatusEffect {
    public OneTimeIncreaseFishQualityStatusEffect() {
        super(StatusEffectCategory.BENEFICIAL, Color.CYAN.getRGB());
    }
}
