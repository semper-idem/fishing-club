package net.semperidem.fishingclub.fish.specimen;

import com.mojang.serialization.Dynamic;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.registry.RegistryWrapper;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

public abstract class AbstractSpecimenComponent implements AutoSyncedComponent {
	SpecimenData data;

	public SpecimenData get() {
		return this.data;
	}

	public SpecimenData getOrDefault() {
		return this.data == null ? SpecimenData.DEFAULT : this.data;
	}

	public void set(SpecimenData data) {
		this.data = data;
	}

	@Override
	public void applySyncPacket(RegistryByteBuf buf) {
		AutoSyncedComponent.super.applySyncPacket(buf);
	}

	@Override
	public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		if (!tag.contains(KEY)) {
			return;
		}
		SpecimenData.CODEC.parse(new Dynamic<>(NbtOps.INSTANCE, tag.get(KEY))).resultOrPartial().ifPresent(this::set);
	}

	@Override
	public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		if (data == null) {
			return;
		}
		SpecimenData.CODEC.encodeStart(NbtOps.INSTANCE, this.data).resultOrPartial().ifPresent(dataTag -> tag.put(KEY, dataTag));
	}

	public static final String KEY = "specimen_component_nbt";
}
