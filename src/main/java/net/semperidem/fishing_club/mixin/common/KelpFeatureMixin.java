package net.semperidem.fishing_club.mixin.common;


import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.block.BlockState;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.KelpFeature;
import net.minecraft.world.gen.feature.util.FeatureContext;
import net.semperidem.fishing_club.registry.FCBlocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(KelpFeature.class)
public class KelpFeatureMixin {

	@Unique
	private float rarity = 0;


	@Inject(
		method = "generate",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/util/math/random/Random;nextInt(I)I"
		)
	)
	private void getBlockState(FeatureContext<DefaultFeatureConfig> context, CallbackInfoReturnable<Boolean> cir,
							   @Local(ordinal = 0) LocalRef<BlockState> kelp,
							   @Local(ordinal = 1) LocalRef<BlockState> plant
	) {
		rarity = context.getRandom().nextFloat();
		RegistryEntry<Biome> biome = context.getWorld().getBiome(context.getOrigin());
		biome.getKey().ifPresent(biomeKey -> {
			if (biomeKey.toString().contains("warm") && this.rarity < 0.15f) {
				kelp.set(FCBlocks.NUTRITIOUS_KELP.getDefaultState());
				plant.set(FCBlocks.NUTRITIOUS_KELP_PLANT.getDefaultState());
			}
		});
		if (biome.isIn(BiomeTags.IS_DEEP_OCEAN) && this.rarity < 0.05f) {
			kelp.set(FCBlocks.ENERGY_DENSE_KELP.getDefaultState());
			plant.set(FCBlocks.ENERGY_DENSE_KELP_PLANT.getDefaultState());
		}
	}
}



