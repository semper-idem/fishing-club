package net.semperidem.fishing_club.screen.member;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.semperidem.fishing_club.fisher.FishingCard;
import net.semperidem.fishing_club.network.payload.CoinFlipPayload;
import net.semperidem.fishing_club.network.payload.MemberPayload;
import net.semperidem.fishing_club.registry.FCScreenHandlers;

import java.util.ArrayList;
import java.util.Objects;

public class MemberScreenHandler extends ScreenHandler {
    final static int ROW_COUNT = 6;
    final static int SLOT_COUNT =  ROW_COUNT *  9;

    private PlayerEntity player;
    FishingCard fishingCard;
    private final ArrayList<String> coinFlipHistory = new ArrayList<>();
    private String lastCoinFlipSide = "";

    String capeHolder = "";
    int minCapePrice = 0;

    public MemberScreenHandler(int syncId, PlayerInventory playerInventory, FishingCard fishingCard) {
        super(FCScreenHandlers.MEMBER_SCREEN, syncId);
        this.player = playerInventory.player;
        this.fishingCard = fishingCard;
    }

    public MemberScreenHandler(int syncId, PlayerInventory playerInventory, MemberPayload payload) {
        super(FCScreenHandlers.MEMBER_SCREEN, syncId);
        this.player = playerInventory.player;
        this.fishingCard = FishingCard.of(player);
    }

    public int getMoonPhaseDiscount() {
        return player.getWorld().getMoonPhase();
    }

    public ArrayList<ItemStack> getFishes() {
        ArrayList<ItemStack> fishes = new ArrayList<>();
        for(ItemStack itemStack : player.getInventory().main) {
            if (itemStack.isEmpty()) {
                continue;
            }
            fishes.add(itemStack);
        }
        return fishes;
    }

    public int getMinCapePrice(){
        return minCapePrice;
    }

    public String getCapeHolder() {
        return this.capeHolder;
    }


    public FishingCard getCard() {
        return fishingCard;
    }


    public boolean canUse(PlayerEntity playerEntity) {
        return true;
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int index) {
        return this.slots.get(index).getStack();
    }

    public void tossCoin(int amount, String playerChoice) {
        this.lastCoinFlipSide = playerChoice;
        this.coinFlipHistory.add("Picked;" + playerChoice + ";Bet:;" + amount + "$");
        ClientPlayNetworking.send(new CoinFlipPayload(amount));
    }

    public ArrayList<String> getCoinFlipHistory() {
        return this.coinFlipHistory;
    }

    public void appendCoinFlipResult(int resultAmount) {
        this.coinFlipHistory.add(
                "Was;" + (resultAmount > 0 ?
                        "§6" + this.lastCoinFlipSide + ";Won;" :
                        "§8" + this.getOpposite() + ";Lost;"
                ) + resultAmount +"$"
        );
    }

    private String getOpposite() {
        return Objects.equals(this.lastCoinFlipSide, "Heads") ? "Tails" : "Heads";
    }
}
