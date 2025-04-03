package net.semperidem.fishingclub.screen.card;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.semperidem.fishingclub.fisher.Card;
import net.semperidem.fishingclub.fisher.CardInventory;
import net.semperidem.fishingclub.fisher.tradesecret.TradeSecret;
import net.semperidem.fishingclub.fisher.tradesecret.TradeSecrets;
import net.semperidem.fishingclub.network.payload.CardSellFishPayload;
import net.semperidem.fishingclub.registry.ScreenHandlers;
import net.semperidem.fishingclub.registry.Tags;

import static net.semperidem.fishingclub.screen.ScreenHandlerConstants.*;
import static net.semperidem.fishingclub.screen.ScreenHandlerConstants.SLOTS_PER_ROW;

public class CardScreenHandler extends ScreenHandler {
    public enum Tab {
        //Order of Tabs in the CardScreen depends on order of declaration of this enum
        STATS,
        SECRET,
        ATLAS,
        LEADERBOARD;

    }

    private Tab currentTab;
    public final Card card;
    private final PlayerInventory inventory;
    private static final int CARD_SLOTS = CardInventory.SLOT_COUNT - 1;
    private Slot sellSlot;

    public CardScreenHandler(int syncId, PlayerInventory inventory) {
        super(ScreenHandlers.CARD, syncId);
        this.card = Card.of(inventory.player);
        this.inventory = inventory;
        addCardInventory();
        addPlayerInventorySlots();
    }

    public void setCurrentTab(Tab tab){
       this.currentTab = tab;
    }

    public void sellFish() {
        if (cannotSell()) {
            return;
        }
        ClientPlayNetworking.send(new CardSellFishPayload());
    }

    public boolean cannotSell() {
        return !this.sellSlot.hasStack();
    }

    private void addCardInventory() {
        this.sellSlot = this.addSlot(new SecretSlot(TradeSecrets.INSTANT_FISH_CREDIT, Tags.FISH_ITEM, Tab.STATS, card, 0, 129, 19));
        this.addSlot(new LeveledSecretSlot(3, TradeSecrets.PLACE_IN_MY_HEART, ItemTags.BOATS, Tab.STATS, card, 1, 148, 57));
        this.addSlot(new LeveledSecretSlot(2, TradeSecrets.PLACE_IN_MY_HEART, Tags.CONTAINER, Tab.STATS, card, 2, 148, 38));
        this.addSlot(new LeveledSecretSlot(1, TradeSecrets.PLACE_IN_MY_HEART, Tags.CORE, Tab.STATS, card, 3, 148, 19));
    }

    class LeveledSecretSlot extends SecretSlot {
        int requiredLevel;

        public LeveledSecretSlot(int requiredLevel, TradeSecret enabledSecret, TagKey<Item> enabledTag, Tab enabledTab, Inventory inventory, int index, int x, int y) {
            super(enabledSecret, enabledTag, enabledTab, inventory, index, x, y);
            this.requiredLevel = requiredLevel;
        }

        @Override
        public boolean isEnabled() {
            return card.tradeSecretLevel(enabledSecret) >= requiredLevel && super.isEnabled();
        }
    }

    class SecretSlot extends TagSlot {
        final TradeSecret enabledSecret;

        public SecretSlot(TradeSecret enabledSecret, TagKey<Item> enabledTag, Tab enabledTab, Inventory inventory, int index, int x, int y) {
            super(enabledTag, enabledTab, inventory, index, x, y);
            this.enabledSecret = enabledSecret;
        }

        @Override
        public boolean isEnabled() {
            return card.hasRequiredPerk(enabledSecret) && super.isEnabled();
        }
    }

    class TagSlot extends TabbedSlot {
        TagKey<Item> enabledTag;


        public TagSlot(TagKey<Item> enabledTag, Tab enabledTab, Inventory inventory, int index, int x, int y) {
            super(enabledTab, inventory, index, x, y);
            this.enabledTag = enabledTag;
        }


        @Override
        public boolean canInsert(ItemStack stack) {

            return stack.isIn(enabledTag) && super.canInsert(stack);
        }
    }

    class TabbedSlot extends Slot {
        private final Tab enabledTab;

        public TabbedSlot(Tab enabledTab, Inventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
            this.enabledTab = enabledTab;
        }

        @Override
        public boolean isEnabled() {
            return currentTab == enabledTab;
        }
    }


    public void addPlayerInventorySlots(){
        for(int x = 0; x < ROW_COUNT; ++x) {
            for(int y = 0; y < SLOTS_PER_ROW; ++y) {
                this.addSlot(new TabbedSlot(
                        Tab.STATS,
                        inventory,
                        y + (x + 1) * SLOTS_PER_ROW,
                        INVENTORY_X_START + y * SLOT_SIZE,
                        INVENTORY_Y_START + x * SLOT_SIZE
                )
                );
            }
        }

        for(int x = 0; x < SLOTS_PER_ROW; ++x) {
            this.addSlot(new TabbedSlot(
                    Tab.STATS,
                    inventory,
                    x,
                    INVENTORY_X_START + x * SLOT_SIZE,
                    HOTBAR_Y_START
            )
            );
        }
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slotId) {
        Slot slot = this.slots.get(slotId);
        if (!slot.hasStack()) {
            return ItemStack.EMPTY;
        }
        ItemStack slotStack = slot.getStack();

        if (slotId >= CARD_SLOTS && this.insertItem(slotStack, 0, CARD_SLOTS, false)) {
                return ItemStack.EMPTY;
        }
        if (slotId < CARD_SLOTS && this.insertItem(slotStack, CARD_SLOTS, this.slots.size(), true)) {
            return ItemStack.EMPTY;
        }

        if (slotStack.isEmpty()) {
            slot.setStack(ItemStack.EMPTY, slotStack);
            return ItemStack.EMPTY;
        }

        slot.markDirty();
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }
}