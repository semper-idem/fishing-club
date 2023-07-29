package net.semperidem.fishingclub.client.screen.fisher_info;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.semperidem.fishingclub.fisher.FisherInfos;
import org.jetbrains.annotations.Nullable;

public class FisherInfoScreenFactory implements ExtendedScreenHandlerFactory {
    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeNbt(player.writeNbt(new NbtCompound()));
    }
    @Override
    public Text getDisplayName() {
        return Text.translatable("Fisher Info");
    }
    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new FisherInfoScreenHandler(syncId, inv, FisherInfos.getFisher(player));
    }
}
