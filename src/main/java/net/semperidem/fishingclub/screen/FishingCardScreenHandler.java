package net.semperidem.fishingclub.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.semperidem.fishingclub.fish.FishUtil;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.fisher.FishingCardSerializer;
import net.semperidem.fishingclub.fisher.perks.FishingPerk;
import net.semperidem.fishingclub.fisher.perks.FishingPerks;
import net.semperidem.fishingclub.registry.FItemRegistry;
import net.semperidem.fishingclub.registry.FScreenHandlerRegistry;

import java.util.HashMap;

import static net.semperidem.fishingclub.client.screen.shop.ShopScreenUtil.SLOTS_PER_ROW;
import static net.semperidem.fishingclub.client.screen.shop.ShopScreenUtil.SLOT_SIZE;

public class FishingCardScreenHandler extends ScreenHandler {
    private final static int SLOT_COUNT = 5;

    private final PlayerInventory playerInventory;

    public FishingCard fishingCard;
    public FishingPerk rootPerk;
    public InstantSellSlot instantSellSlot;

    private final HashMap<String, FishingCardTab> tabs = new HashMap<>();
    public final FishingCardTab generalTab;
    public final FishingCardTab hobbyistTab;
    public final FishingCardTab opportunistTab;
    public final FishingCardTab socialistTab;
    private FishingCardTab activeTab;



    public FishingCardScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, playerInventory, FishingCardSerializer.fromNbt(playerInventory.player, buf.readNbt()));
    }

    public FishingCardScreenHandler(int syncId, PlayerInventory playerInventory, FishingCard fishingCard) {
        super(FScreenHandlerRegistry.FISHING_CARD_SCREEN, syncId);
        this.playerInventory = playerInventory;
        this.fishingCard = fishingCard;

        generalTab = new FishingCardTab("General");
        hobbyistTab = new FishingCardTab("Hobbyist");
        opportunistTab = new FishingCardTab("Opportunist");
        socialistTab = new FishingCardTab("Socialist");
        tabs.put("General", generalTab);
        tabs.put("Hobbyist", hobbyistTab);
        tabs.put("Opportunist", opportunistTab);
        tabs.put("Socialist", socialistTab);
        activeTab = generalTab;

        addFisherInventory();
        addInstantSellSlot();
        addPlayerInventorySlots();


    }

    public FishingCardTab getActiveTab(){
        return activeTab;
    }

    public void setActiveTab(String tabName){
        this.activeTab = tabs.get(tabName);
    }

    public boolean shouldRenderSellButton(){
        return instantSellSlot.hasStack() && instantSellSlot.isEnabled();
    }


    public void instantSell(){
        instantSellSlot.setStack(ItemStack.EMPTY);
    }

    private void addInstantSellSlot(){
        instantSellSlot = new InstantSellSlot(
                fishingCard, 4, 323, 226,
                this, generalTab,
                fishingCard.hasPerk(FishingPerks.INSTANT_FISH_CREDIT),
                FishUtil.FISH_ITEM);
        addSlot(instantSellSlot);
    }

    private void addFishingCardSlot(int index, int x, int y, boolean isUnlocked, Item itemBound) {
        addSlot(new UnlockableBoundSlot(fishingCard, index, x, y, this, generalTab, isUnlocked, itemBound));
    }

    private void addFisherInventory(){
        addFishingCardSlot(0, 25, 199, fishingCard.hasPerk(FishingPerks.FISHING_ROD_SLOT), Items.FISHING_ROD);
        addFishingCardSlot(1, 55, 199, fishingCard.hasPerk(FishingPerks.BOAT_SLOT), Items.OAK_BOAT);
        addFishingCardSlot(2, 25, 229, fishingCard.hasPerk(FishingPerks.NET_SLOT_UNLOCK),FItemRegistry.FISHING_NET);
        addFishingCardSlot(3, 55, 229, fishingCard.hasPerk(FishingPerks.NET_SLOT_UNLOCK), FItemRegistry.FISHING_NET);
    }

    public void addPlayerInventorySlots(){
        for(int x = 0; x < SLOTS_PER_ROW; ++x) {
            addSlot(new TabSlot(playerInventory, x, 157 + x * SLOT_SIZE, 226, this, generalTab));
        }
        for(int y = 0; y < 3; ++y) {
            for(int x = 0; x < 9; ++x) {
                addSlot(new TabSlot(playerInventory,
                        x + (y * SLOTS_PER_ROW) + SLOTS_PER_ROW,
                        157 + x * SLOT_SIZE,
                        166 + y * SLOT_SIZE,
                        this, generalTab
                ));
            }
        }
    }

    public boolean canUse(PlayerEntity playerEntity) {
        return true;
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
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
                slot.setStack(ItemStack.EMPTY);
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
