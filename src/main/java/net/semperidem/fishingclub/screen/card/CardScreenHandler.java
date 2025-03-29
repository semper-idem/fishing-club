package net.semperidem.fishingclub.screen.card;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.semperidem.fishingclub.fisher.Card;
import net.semperidem.fishingclub.registry.ScreenHandlers;

public class CardScreenHandler extends ScreenHandler {
    private final Card card;


    protected CardScreenHandler(int syncId, PlayerInventory inventory) {
        super(ScreenHandlers.CARD, syncId);
        this.card = Card.of(inventory.player);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {

        //TODO
        //If from player inventory - Try to fill slot or else nothing
        //If from card inventory - try to put into player inventory or else do nothing

        return null;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }
}
