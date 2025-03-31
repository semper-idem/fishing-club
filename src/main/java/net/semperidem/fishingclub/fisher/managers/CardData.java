package net.semperidem.fishingclub.fisher.managers;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.registry.RegistryWrapper;
import net.semperidem.fishingclub.fisher.Card;

import static net.semperidem.fishingclub.fisher.Card.CARD;

public abstract class CardData {
    Card trackedFor;
    public CardData(Card trackedFor) {
        this.trackedFor = trackedFor;
    }

    void sync() {
        CARD.sync(trackedFor.holder(), (buf, recipient) -> this.writeSyncPacket(buf));
    }

    void writeSyncPacket(RegistryByteBuf buf){
        NbtCompound tag = new NbtCompound();
        this.writeNbt(tag, buf.getRegistryManager());
        buf.writeNbt(tag);
    };

    abstract void readNbt(NbtCompound nbtCompound, RegistryWrapper.WrapperLookup wrapperLookup);
    abstract void writeNbt(NbtCompound nbtCompound, RegistryWrapper.WrapperLookup wrapperLookup);
}
