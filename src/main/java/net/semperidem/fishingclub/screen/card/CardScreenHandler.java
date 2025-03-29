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
import net.semperidem.fishingclub.fisher.tradesecret.TradeSecret;
import net.semperidem.fishingclub.fisher.tradesecret.TradeSecrets;
import net.semperidem.fishingclub.network.payload.SellFishDirectPayload;
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
    private Slot sellSlot;

    public CardScreenHandler(int syncId, PlayerInventory inventory) {
        super(ScreenHandlers.CARD, syncId);
        this.card = Card.of(inventory.player);
        this.inventory = inventory;
        addPlayerInventorySlots();
        addCardInventory();
    }

    public void setCurrentTab(Tab tab){
       this.currentTab = tab;
    }

    public void sellFish() {
        if (cannotSell()) {
            return;
        }
        ClientPlayNetworking.send(new SellFishDirectPayload(this.sellSlot.getStack()));
    }

    public boolean cannotSell() {
        return !this.sellSlot.hasStack();
    }

    private void addCardInventory() {
        this.addSlot(new TagSlot(Tags.ROD_CORE, Tab.STATS, card, 0, 148, 19));
        this.addSlot(new TagSlot(Tags.CONTAINER, Tab.STATS, card, 1, 148, 38));
        this.addSlot(new TagSlot(ItemTags.BOATS, Tab.STATS, card, 2, 148, 57));
        this.sellSlot = this.addSlot(new SecretSlot(TradeSecrets.INSTANT_FISH_CREDIT, Tags.FISH_ITEM, Tab.STATS, card, 3, 127, 57));
    }


    class SecretSlot extends TagSlot {
        private final TradeSecret enabledSecret;

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
        }}

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
