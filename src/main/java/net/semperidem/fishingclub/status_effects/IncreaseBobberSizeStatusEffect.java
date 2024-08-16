package net.semperidem.fishingclub.status_effects;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

import java.awt.*;

public class IncreaseBobberSizeStatusEffect extends StatusEffect {
    public IncreaseBobberSizeStatusEffect() {
        super(StatusEffectCategory.BENEFICIAL, Color.CYAN.getRGB());
    }
}
