package net.semperidem.fishingclub.screen.dialog;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.semperidem.fishingclub.entity.FishermanEntity;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;

public class DialogScreenHandlerFactory implements ExtendedScreenHandlerFactory {
    HashSet<DialogKey> openingKeys;
    FishermanEntity fishermanEntity;

    public DialogScreenHandlerFactory(HashSet<DialogKey> openingKeys, FishermanEntity fishermanEntity) {
        this.openingKeys = openingKeys;
        this.fishermanEntity = fishermanEntity;
    }
    @Override
    public Text getDisplayName() {
        return Text.empty();
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new DialogScreenHandler(syncId, inv, DialogUtil.getStringFromKeys(openingKeys), fishermanEntity);
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeString(DialogUtil.getStringFromKeys(openingKeys));
    }
}
