package net.semperidem.fishingclub.screen.old_card;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.semperidem.fishingclub.fisher.Card;
import net.semperidem.fishingclub.fisher.tradesecret.TradeSecrets;
import net.semperidem.fishingclub.network.payload.CardPayload;
import net.semperidem.fishingclub.network.payload.CardSellFishPayload;
import net.semperidem.fishingclub.registry.Items;
import net.semperidem.fishingclub.registry.ScreenHandlers;

public class OldFishingCardScreenHandler extends ScreenHandler {
    private final static int SLOT_COUNT = 5;
    private final static int SLOTS_PER_ROW = 9;
    private final static int SLOT_SIZE = 18;

    private final PlayerInventory playerInventory;
    public final boolean isClient;

    public Card card;
    public InstantSellSlot instantSellSlot;



    public OldFishingCardScreenHandler(int syncId, PlayerInventory playerInventory, CardPayload cardPayload) {//todo move from cca to vanila component
        this(syncId, playerInventory, Card.of(playerInventory.player));
    }

    public OldFishingCardScreenHandler(int syncId, PlayerInventory playerInventory, Card card) {
        super(ScreenHandlers.CARD, syncId);
        this.playerInventory = playerInventory;
        this.card = card;
        addFisherInventory();
        addInstantSellSlot();
        addPlayerInventorySlots();
        this.isClient = playerInventory.player.getWorld().isClient();
    }


    public boolean shouldRenderSellButton(){

        return instantSellSlot.hasStack() && instantSellSlot.isEnabled();
    }


    public void instantSell(){
        ClientPlayNetworking.send(new CardSellFishPayload());
    }

    private void addInstantSellSlot(){
        instantSellSlot = new InstantSellSlot(
                card, 4, 341, 226,
                this,
                card.knowsTradeSecret(TradeSecrets.INSTANT_FISH_CREDIT)
                );
        addSlot(instantSellSlot);
    }

    private void addFishingCardSlot(int index, int x, int y, boolean isUnlocked, Item itemBound) {
        addSlot(new UnlockableBoundSlot(card, index, x, y, this, isUnlocked, itemBound));
    }

    private void addFisherInventory(){
        addFishingCardSlot(0, 323, 166, true, net.minecraft.item.Items.FISHING_ROD);
        addFishingCardSlot(1, 341, 166, true, net.minecraft.item.Items.OAK_BOAT);
        addFishingCardSlot(2, 323, 184, true, Items.FISHING_NET);
        addFishingCardSlot(3, 341, 184, true, Items.FISHING_NET);
    }

    public void addPlayerInventorySlots(){
        for(int x = 0; x < SLOTS_PER_ROW; ++x) {
            addSlot(new TabSlot(playerInventory, x, 143 + x * SLOT_SIZE, 226, this));
        }
        for(int y = 0; y < 3; ++y) {
            for(int x = 0; x < 9; ++x) {
                addSlot(new TabSlot(playerInventory,
                        x + (y * SLOTS_PER_ROW) + SLOTS_PER_ROW,
                        143 + x * SLOT_SIZE,
                        166 + y * SLOT_SIZE,
                        this
                ));
            }
        }
    }

    public boolean canUse(PlayerEntity playerEntity) {
        return true;
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int index) {
        ItemStack itemStackCopy = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasStack()) {
            ItemStack itemStackInSlot = slot.getStack();
            itemStackCopy = itemStackInSlot.copy();
            if (index < SLOT_COUNT) {
                if (!this.insertItem(itemStackInSlot, SLOT_COUNT, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (!this.insertItem(itemStackInSlot, 0, SLOT_COUNT, false)) {
                    return ItemStack.EMPTY;
                }
            }
            if (itemStackInSlot.isEmpty()) {
                slot.setStackNoCallbacks(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
            if (itemStackInSlot.getCount() == itemStackCopy.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTakeItem(player, itemStackInSlot);
        }
        return itemStackCopy;
    }

}
