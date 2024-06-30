package net.semperidem.fishingclub.screen.dialog;

import java.util.HashSet;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.semperidem.fishingclub.network.payload.DialogPayload;
import org.jetbrains.annotations.Nullable;

public class DialogScreenHandlerFactory implements ExtendedScreenHandlerFactory<DialogPayload> {
    private final HashSet<DialogKey> openingKeys;

    public DialogScreenHandlerFactory(HashSet<DialogKey> openingKeys) {
        this.openingKeys = openingKeys;
    }

    @Override
    public Text getDisplayName() {
        return Text.empty();
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new DialogScreenHandler(syncId, inv, DialogUtil.getStringFromKeys(openingKeys));
    }

    @Override
    public DialogPayload getScreenOpeningData(ServerPlayerEntity player) {
        return new DialogPayload(DialogUtil.getStringFromKeys(openingKeys));
    }
}
