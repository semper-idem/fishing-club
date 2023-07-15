package net.semperidem.fishingclub.client.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.semperidem.fishingclub.FishingClub;
import org.jetbrains.annotations.Nullable;

public class FisherWorkbenchScreenHandler extends ScreenHandler {
    public FisherWorkbenchScreenHandler(int syncId, PlayerInventory inventory) {
        super(FishingClub.FISHER_WORKBENCH_SCREEN_HANDLER, syncId);
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        return null;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }
}
