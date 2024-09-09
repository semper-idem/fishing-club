package net.semperidem.fishingclub.status_effects;

import net.minecraft.entity.effect.StatusEffect;

import static java.awt.Color.CYAN;
import static net.minecraft.entity.effect.StatusEffectCategory.BENEFICIAL;

public class IncreaseFishingExpStatusEffect extends StatusEffect {
    public static final float BONUS_PER_AMPLIFIER = 0.1f;
    public IncreaseFishingExpStatusEffect() {
        super(BENEFICIAL, CYAN.getRGB());
    }
}
