package net.semperidem.fishingclub.screen.fishing_post;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.Property;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.semperidem.fishingclub.fish.FishUtil;
import net.semperidem.fishingclub.fish.specimen.SpecimenData;
import net.semperidem.fishingclub.fisher.Card;
import net.semperidem.fishingclub.fisher.level_reward.LevelUpEffect;
import net.semperidem.fishingclub.network.payload.FishingPostPayload;
import net.semperidem.fishingclub.registry.ScreenHandlers;

import java.util.List;

public class FishingPostScreenHandler extends ScreenHandler {
    PlayerInventory inventory;
    public Property stage = Property.create();
    public static final int LAST_STAGE = 5;
    public int ticks = 0;
    private static final int MIN_TICKS = 5;
    private static final int MAX_TICKS = 20;
    private static final int TICKS_PER_SLOW = 4;
    private StatusEffectInstance previousSlow;
    private List<ItemStack> rewards;
    private ItemStack mainStack;
    private ItemStack fishStack;
    private Slot mainSlot;
    private Slot fishSlot;
    private SpecimenData fish;
    private Card card;
    private int exp;

    public FishingPostScreenHandler(int syncId, PlayerInventory playerInventory, FishingPostPayload payload) {
        super(ScreenHandlers.FISHING_GAME_POST_SCREEN, syncId);
        this.inventory = playerInventory;
        this.stage.set(1);
        this.addProperty(this.stage);
        this.fish = payload.fish();
        this.rewards = payload.rewards();
        this.card = Card.of(playerInventory.player);
        StatusEffectInstance slowness = this.inventory.player.getStatusEffect(StatusEffects.SLOWNESS);
        if (slowness != null) {
            this.previousSlow = new StatusEffectInstance(slowness);
        }
        this.fishStack = FishUtil.fishCaught(this.inventory.player, fish);
        this.exp = payload.exp();
        int j;
        int k;
        for(j = 0; j < 3; ++j) {
            for(k = 0; k < 9; ++k) {
                this.addSlot(new Slot(playerInventory, k + j * 9 + 9, 0, 0));
            }
        }
        this.mainStack = this.inventory.getMainStacks().getFirst();
        for(j = 9; j >= 0; j--) {
            Slot slot = new Slot(playerInventory, j, 0,0);
            this.addSlot(slot);
            if (slot.getStack().equals(mainStack)) {
                this.mainSlot = slot;
            }
        }
        for (Slot slot : this.slots) {
            if (slot.getStack().getComponents().equals(fishStack.getComponents())) {
                fishSlot = slot;
            }
        }
        if (mainSlot != null && fishSlot != null) {
            this.fishStack = fishSlot.getStack();
            fishSlot.setStack(mainStack);
            mainSlot.setStack(fishStack);
        }
//        ItemStack mainStack = mainSlott.takeStack(1);
//        fishStack = this.slots.get(fishSlot).takeStack(1);
        
        //this.replaceMainHand(payload.fish().asItemStack());
        if (this.inventory.player instanceof ServerPlayerEntity serverPlayer) {
            switch (this.fish.quality()) {
                case 3: LevelUpEffect.COMMON_EFFECT.executeFor(serverPlayer);
                case 4: LevelUpEffect.UNCOMMON_EFFECT.executeFor(serverPlayer);
                case 5: LevelUpEffect.RARE_EFFECT.executeFor(serverPlayer);
            }
        }
    }

    public int exp() {
        return this.exp;
    }

    public Card fishingCard() {
        return this.card;
    }

    public void onClosed(PlayerEntity player) {
        if (!player.getWorld().isClient && this.fishSlot != null && this.mainSlot != null) {
            this.fishSlot.setStack(fishStack);
            this.mainSlot.setStack(mainStack);
            this.rewards.stream().filter(reward -> !this.inventory.insertStack(reward)).forEach(rewardWithNoSlot -> this.inventory.player.dropItem(rewardWithNoSlot, true));
        }
        super.onClosed(player);
    }

    public SpecimenData fish() {
        return this.fish;
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
        this.stage.set(this.stage.get() == LAST_STAGE ? LAST_STAGE + 1 : LAST_STAGE);
        this.updateToClient();
    }

    @Override
    public void sendContentUpdates() {
        ticks++;
        if (this.ticks > MAX_TICKS && !this.isLastStage()) {
            this.stage.set(this.stage.get() + 1);
            this.ticks = 0;
            this.updateToClient();
        }
        this.inventory.player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, MAX_TICKS, this.stage.get(),false, false));
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
        this.inventory.player.removeStatusEffect(StatusEffects.SLOWNESS);
        if (this.previousSlow != null) {
            this.inventory.player.addStatusEffect(this.previousSlow);
        }
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