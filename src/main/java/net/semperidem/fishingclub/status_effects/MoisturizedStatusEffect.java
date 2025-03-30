package net.semperidem.fishingclub.status_effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerEntity;

import static java.awt.Color.GREEN;
import static net.minecraft.entity.effect.StatusEffectCategory.BENEFICIAL;

public class MoisturizedStatusEffect  extends StatusEffect {
	public MoisturizedStatusEffect() {
		super(BENEFICIAL, GREEN.getRGB());
	}


	public boolean applyUpdateEffect(LivingEntity entity, int amplifier) {
		if (entity.getHealth() < entity.getMaxHealth()) {
			entity.heal(1.0F);
		}
		if (!entity.getWorld().isClient && entity instanceof PlayerEntity playerEntity) {
			playerEntity.getHungerManager().add(amplifier + 1, 1.0F);
		}
		return true;
	}

	@Override
	public boolean canApplyUpdateEffect(int duration, int amplifier) {
		int i = 50 >> amplifier;
		return i == 0 || duration % i == 0;
	}
}
