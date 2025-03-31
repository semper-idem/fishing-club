package net.semperidem.fishingclub.fish.specimen;

import com.mojang.serialization.Dynamic;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.RegistryWrapper;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

import java.util.Optional;

public abstract class AbstractSpecimenComponent implements AutoSyncedComponent {
	private SpecimenData data;


	public SpecimenData getOrDefault() {
		return Optional.ofNullable(data).orElse(SpecimenData.DEFAULT);
	}

	public boolean isEmpty() {
		return data == null;
	}

	public void empty(){
		this.data = null;
	}

	public void set(SpecimenData data) {
		this.data = data;
	}

	@Override
	public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		SpecimenData
				.CODEC
				.parse(new Dynamic<>(NbtOps.INSTANCE, tag.get(TAG_KEY)))
				.result()
				.ifPresent(this::set);
	}

	@Override
	public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		SpecimenData
				.CODEC
				.encodeStart(NbtOps.INSTANCE, Optional.ofNullable(data).orElse(SpecimenData.DEFAULT))
				.result()
				.ifPresent(dataTag -> tag.put(TAG_KEY, dataTag));
	}

	public static final String TAG_KEY = "specimen";
}
