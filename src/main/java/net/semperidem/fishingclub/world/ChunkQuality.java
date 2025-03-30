package net.semperidem.fishingclub.world;

import net.minecraft.block.Block;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.fish.Species;
import net.semperidem.fishingclub.registry.Blocks;
import org.ladysnake.cca.api.v3.chunk.ChunkComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.chunk.ChunkComponentInitializer;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicReference;

public class ChunkQuality implements ChunkComponentInitializer, ServerTickingComponent, AutoSyncedComponent {
	private static final String CHUNK_QUALITY_KEY = "chunk_quality";
	private static final String BASE_KEY = "base";
	private static final String CEILING_KEY = "ceiling";
	private static final String CURRENT_KEY = "current";

	public static final ComponentKey<ChunkQuality> CHUNK_QUALITY = ComponentRegistry.getOrCreate(FishingClub.identifier(CHUNK_QUALITY_KEY), ChunkQuality.class);

	private final HashMap<FloraInfluence, Integer> floraToCount = new HashMap<>();

	private static final int regenInterval = 20;
 	private static final int syncInterval = 20 * 600;
	private static final double BASE_REGEN = 0.0001D;
	int syncTick;
	boolean needsRecalculation = false;
	boolean isDirty;
	int regenTick;
	double regen = BASE_REGEN;
	double base;
	double ceiling;
	double current;
	Chunk chunk;

	public ChunkQuality(){}

	public ChunkQuality(Chunk chunk) {
		this.chunk = chunk;
	}

	public void init(ServerWorld world) {
		ChunkPos pos = this.chunk.getPos();
		DensityFunction.UnblendedNoisePos unblendedNoisePos = new DensityFunction.UnblendedNoisePos(pos.x, 0, pos.z);
		double randomBase = Math.abs(world.getChunkManager().getNoiseConfig().getNoiseRouter().vegetation().sample(unblendedNoisePos)) * 5;
		double floraValue = getFloraValue();
		this.base = randomBase + floraValue * 0.5f;
		this.ceiling = base + floraValue;
		this.current = ceiling;
		if (chunk == null) {
			return;
		}
		CHUNK_QUALITY.sync(chunk);
	}

	private double getFloraValue() {
		AtomicReference<Double> floraValue = new AtomicReference<>(0D);
		HashMap<FloraInfluence, Integer> floraToCount = new HashMap<>();
		this.chunk.forEachBlockMatchingPredicate(blockState -> {
				FloraInfluence floraInfluence = FloraInfluence.getOrNull(blockState.getBlock());
				if (floraInfluence == null) {
					return false;
				}
				floraToCount.put(floraInfluence, floraToCount.getOrDefault(floraInfluence, 0) + 1);
				return true;
			}, ((blockPos, blockState) -> {
				//Should never be null
				FloraInfluence floraInfluence = FloraInfluence.getOrNull(blockState.getBlock());
				int count = floraToCount.get(floraInfluence);
				double newValue = (float)MathHelper.clamp(
						floraValue.get() + (count > 1 ? floraInfluence.value * 0.1 : floraInfluence.value),
						0,
						floraInfluence.value * 2
				);
				floraValue.set(newValue);
			})
		);
		return floraValue.get();
	}

	public void needsRecalculation() {
		this.needsRecalculation = true;
	}

	private void tickRegen() {
		if (this.ceiling == this.current) {
			return;
		}
		if (this.regenTick < regenInterval) {

			this.regenTick++;
			return;
		}
		this.isDirty = true;
		this.regenTick = 0;
		this.setCurrent(this.current + regen);
	}

	private void setCurrent(double newCurrent) {
		this.current = MathHelper.clamp(newCurrent, 0, this.ceiling);
	}
	private void tickSync() {
		if (this.chunk == null) {
			return;
		}

		if (this.syncTick < syncInterval) {
			this.syncTick++;
			return;
		}

		if (!this.isDirty) {
			return;
		}

		this.syncTick = 0;
		this.chunk.markNeedsSaving();

		if (this.needsRecalculation) {

			this.ceiling = this.base + this.getFloraValue();
			this.needsRecalculation = false;
		}
		if (chunk == null) {
			return;
		}
		CHUNK_QUALITY.sync(chunk);
	}



	@Override
	public void serverTick() {
		this.tickRegen();
		this.tickSync();
	}

	public void influence(PlayerInfluence influence) {
		this.setCurrent(current + influence.value);
	}

	public double getBase() {
		return this.base;
	}

	public double getCeiling() {
		return this.ceiling;
	}

	public double getValue() {
		return this.current;
	}
	@Override
	public void registerChunkComponentFactories(ChunkComponentFactoryRegistry registry) {
		registry.register(CHUNK_QUALITY, ChunkQuality::new);
	}

	@Override
	public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		this.base = tag.getDouble(BASE_KEY, 0);
		this.ceiling = tag.getDouble(CEILING_KEY, 0);
		this.current = tag.getDouble(CURRENT_KEY, 0);
	}

	@Override
	public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		tag.putDouble(BASE_KEY, this.base);
		tag.putDouble(CEILING_KEY, this.ceiling);
		tag.putDouble(CURRENT_KEY, this.current);
	}

	public static boolean isAboveMinimumQuality(Species<?> species, WorldAccess world, BlockPos pos) {
		return CHUNK_QUALITY.get(world.getChunk(pos)).getValue() >= Math.log(species.rarity()) * 0.5;
	}

	public enum PlayerInfluence {

		FLUID(-0.05),
		FISH_CAUGHT(-0.01),
		FISH_RELEASE(0.01),
		EXPLOSION(-0.03),
		BLOCK(-0.01);

		public final double value;
		PlayerInfluence(double value) {
			this.value = value;
		}
	}

	public enum FloraInfluence {
		KELP(0.1,
			net.minecraft.block.Blocks.KELP_PLANT,
			Blocks.NUTRITIOUS_KELP_PLANT,
			Blocks.ENERGY_DENSE_KELP_PLANT
		),
		SEA_GRASS(
			0.1,
			net.minecraft.block.Blocks.SEAGRASS,
			net.minecraft.block.Blocks.TALL_SEAGRASS
		),

		PICKLES(
			0.2,
			net.minecraft.block.Blocks.SEAGRASS,
			net.minecraft.block.Blocks.TALL_SEAGRASS
		),

		CORAL(
			0.2,
			net.minecraft.block.Blocks.BRAIN_CORAL,
			net.minecraft.block.Blocks.TUBE_CORAL,
			net.minecraft.block.Blocks.FIRE_CORAL,
			net.minecraft.block.Blocks.BUBBLE_CORAL
			);

		final HashSet<Block> blocks = new HashSet<>();
		final double value;

		FloraInfluence(double value, Block...blocks) {
			this.value = value;
			this.blocks.addAll(Arrays.stream(blocks).toList());
		}

		public static FloraInfluence getOrNull(Block block) {
			return Arrays.stream(FloraInfluence.values()).filter(o -> o.blocks.contains(block)).findFirst().orElse(null);
		}

		public static double getValue(Block block) {
			return Arrays.stream(FloraInfluence.values()).filter(o -> o.blocks.contains(block)).findFirst().map(o -> o.value).orElse(0D);
		}
	}
}
