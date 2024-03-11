package net.semperidem.fishingclub.screen.member;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.semperidem.fishingclub.entity.FishermanEntity;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.network.ClientPacketSender;
import net.semperidem.fishingclub.registry.ScreenHandlerRegistry;

import static net.semperidem.fishingclub.client.screen.shop.ShopScreenUtil.SLOTS_PER_ROW;
import static net.semperidem.fishingclub.client.screen.shop.ShopScreenUtil.SLOT_SIZE;

public class MemberScreenHandler extends ScreenHandler {
    final static int ROW_COUNT = 6;
    final static int SLOT_COUNT =  ROW_COUNT *  SLOTS_PER_ROW;

    private final PlayerEntity player;
    FishermanEntity fishermanEntity;
    FishingCard fishingCard;
    String lastTossWinner;

    public MemberScreenHandler(int syncId, PlayerInventory playerInventory, FishingCard fishingCard, FishermanEntity fishermanEntity) {
        super(ScreenHandlerRegistry.MEMBER_SCREEN, syncId);
        this.player = playerInventory.player;
        this.fishingCard = fishingCard;
        this.fishermanEntity = fishermanEntity;
        addPlayerInventory(player.getInventory());
        addPlayerHotbar(player.getInventory());
    }

    public void updateCard(PacketByteBuf buf) {
        fishingCard = new FishingCard(player, buf.readNbt());
    }

    public FishingCard getCard() {
        return fishingCard;
    }

    public MemberScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, playerInventory, new FishingCard(playerInventory.player, buf.readNbt()), null);
    }

    private void addPlayerInventory(PlayerInventory playerInventory){
        for(int y = 0; y < 3; ++y) {
            for(int x = 0; x < 9; ++x) {
                addSlot(new Slot(playerInventory, x + y * SLOTS_PER_ROW + 9, 8 + x * SLOT_SIZE, 139 + y * SLOT_SIZE));
            }
        }
    }

    private void addPlayerHotbar(PlayerInventory playerInventory){
        for(int x = 0; x < SLOTS_PER_ROW; ++x) {
            addSlot(new Slot(playerInventory, x, 8 + x * SLOT_SIZE, 197));
        }
    }

    public boolean canUse(PlayerEntity playerEntity) {
        return true;
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        return this.slots.get(index).getStack();
    }

    public void tossCoin(int amount, String playerChoice) {
        ClientPacketSender.sendCoinTossRequest(amount, playerChoice);
    }

    public String getWinner() {
        return lastTossWinner;
    }

    public void setWinner(String serverWinner) {
        this.lastTossWinner = serverWinner;
    }
}
