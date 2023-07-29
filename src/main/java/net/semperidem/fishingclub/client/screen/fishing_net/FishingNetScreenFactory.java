package net.semperidem.fishingclub.client.screen.fishing_net;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

public class FishingNetScreenFactory implements ExtendedScreenHandlerFactory {
    ItemStack fishingNetStack;
    public FishingNetScreenFactory(ItemStack fishingNetStack){
        this.fishingNetStack = fishingNetStack;
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity serverPlayerEntity, PacketByteBuf packetByteBuf) {
        packetByteBuf.writeItemStack(fishingNetStack);
    }

    @Override
    public Text getDisplayName() {
        return Text.of(fishingNetStack.getItem().getTranslationKey());
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new FishingNetScreenHandler(syncId, inv, fishingNetStack);
    }
}
