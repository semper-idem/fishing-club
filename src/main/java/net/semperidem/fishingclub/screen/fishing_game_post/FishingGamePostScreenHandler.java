package net.semperidem.fishingclub.screen.fishing_game_post;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.Property;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.semperidem.fishingclub.network.payload.FishingGamePostS2CPayload;
import net.semperidem.fishingclub.registry.FCScreenHandlers;

public class FishingGamePostScreenHandler extends ScreenHandler {
    SimpleInventory temporaryInventory = new SimpleInventory(2);
    PlayerInventory inventory;
    public int mainHandSlot;
    public Property stage = Property.create();
    private static final int LAST_STAGE = 2;
    public int ticks = 0;
    private static final int MIN_TICKS = 5;

    public FishingGamePostScreenHandler(int syncId, PlayerInventory playerInventory, FishingGamePostS2CPayload payload) {
        super(FCScreenHandlers.FISHING_GAME_POST_SCREEN, syncId);
        this.inventory = playerInventory;
        this.mainHandSlot = this.inventory.getSlotWithStack(this.inventory.player.getMainHandStack());
        this.temporaryInventory.setStack(0, this.inventory.player.getMainHandStack());
        this.temporaryInventory.setStack(1, this.inventory.player.getOffHandStack());
        this.inventory.setStack(this.mainHandSlot, payload.fish().asItemStack());
        this.inventory.setStack(40, ItemStack.EMPTY);
        this.addSlot(new Slot(this.inventory, mainHandSlot, 20, 20));
        this.addSlot(new Slot(this.inventory, 40, 20, 40));
        this.addSlot(new Slot(this.temporaryInventory, 0, 0, 20));
        this.addSlot(new Slot(this.temporaryInventory, 1, 0, 40));
        this.addProperty(this.stage);

    }

    public void nextStage(boolean canUse) {
        if (this.ticks < MIN_TICKS) {
            return;
        }
        if (canUse) {
            this.stage.set(LAST_STAGE + 1);
            this.close();
            return;
        }
        this.stage.set(this.stage.get() + 1);
        this.ticks = 0;
        this.updateToClient();
    }

    @Override
    public void sendContentUpdates() {
        ticks++;
        if (this.isDone()) {
            this.close();
        }
    }

    public boolean isDone() {
        return this.stage.get() > LAST_STAGE;
    }

    public boolean isLastStage() {
        return this.stage.get() == LAST_STAGE;
    }

    private void close() {
        this.inventory.setStack(this.mainHandSlot, this.temporaryInventory.getStack(0));
        this.inventory.setStack(40, this.temporaryInventory.getStack(1));
        updateToClient();
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.stage.get() <= LAST_STAGE;
    }
}