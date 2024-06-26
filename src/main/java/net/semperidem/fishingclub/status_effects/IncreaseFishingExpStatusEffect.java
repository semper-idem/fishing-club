package net.semperidem.fishingclub.status_effects;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

import static java.awt.Color.CYAN;
import static net.minecraft.entity.effect.StatusEffectCategory.BENEFICIAL;

public class IncreaseFishingExpStatusEffect extends StatusEffect {
    public IncreaseFishingExpStatusEffect() {
        super(BENEFICIAL, CYAN.getRGB());
    }
}
