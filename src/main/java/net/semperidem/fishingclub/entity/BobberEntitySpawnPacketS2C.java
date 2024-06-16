package net.semperidem.fishingclub.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.semperidem.fishingclub.registry.ItemRegistry;

public class BobberEntitySpawnPacketS2C extends EntitySpawnS2CPacket {
    ItemStack fishingRod;

    public BobberEntitySpawnPacketS2C(Entity entity, int entityId, ItemStack fishingRod) {
        super(entity, entityId);
        this.fishingRod = fishingRod;
    }


    public BobberEntitySpawnPacketS2C(PacketByteBuf buf) {
        super(buf);
        fishingRod = ItemStack.fromNbt(buf.readNbt());
    }

    @Override
    public void write(PacketByteBuf buf) {
        super.write(buf);
        buf.writeNbt(fishingRod.writeNbt(new NbtCompound()));
    }
}
