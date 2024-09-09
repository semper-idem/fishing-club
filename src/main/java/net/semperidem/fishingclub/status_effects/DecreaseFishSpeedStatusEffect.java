package net.semperidem.fishingclub.status_effects;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

import java.awt.*;

public class DecreaseFishSpeedStatusEffect extends StatusEffect {
    public static final float SLOW_AMOUNT = 0.1f;

    public DecreaseFishSpeedStatusEffect() {
        super(StatusEffectCategory.BENEFICIAL, Color.CYAN.getRGB());
    }
}
