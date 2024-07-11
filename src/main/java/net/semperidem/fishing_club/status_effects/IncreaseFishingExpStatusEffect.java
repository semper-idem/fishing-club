package net.semperidem.fishing_club.status_effects;

import net.minecraft.entity.effect.StatusEffect;

import static java.awt.Color.CYAN;
import static net.minecraft.entity.effect.StatusEffectCategory.BENEFICIAL;

public class IncreaseFishingExpStatusEffect extends StatusEffect {
    public IncreaseFishingExpStatusEffect() {
        super(BENEFICIAL, CYAN.getRGB());
    }
}
