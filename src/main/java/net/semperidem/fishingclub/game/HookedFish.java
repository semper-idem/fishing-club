package net.semperidem.fishingclub.game;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.semperidem.fishingclub.fish.Fish;
import net.semperidem.fishingclub.fish.Species;
import net.semperidem.fishingclub.fish.SpeciesLibrary;

public class HookedFish {
    public final Species species;
    public final int level;
    public final float damage;
    public final ItemStack caughtUsing;

    private HookedFish(Species species, int level, float damage, ItemStack caughtUsing){
        this.species = species;
        this.level = level;
        this.damage = damage;
        this.caughtUsing = caughtUsing;
    }

    public static HookedFish fromPacket(PacketByteBuf buf) {
        return new HookedFish(
                SpeciesLibrary.ALL_FISH_TYPES.get(buf.readString()),
                buf.readInt(),
                buf.readFloat(),
                buf.readItemStack()
        );
    }

    public static PacketByteBuf fromFish(Fish fish){
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeString(fish.getSpecies().name);
        buf.writeInt(fish.level);
        buf.writeFloat(fish.damage);
        buf.writeItemStack(fish.caughtUsing);
        return buf;
    }
}
