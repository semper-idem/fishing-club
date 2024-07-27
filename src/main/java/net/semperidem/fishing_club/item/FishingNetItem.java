package net.semperidem.fishing_club.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.BundleItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import net.semperidem.fishing_club.fish.FishUtil;
import net.semperidem.fishing_club.registry.FCComponents;
import org.apache.commons.lang3.math.Fraction;

import java.util.List;

public class FishingNetItem extends BundleItem {
    public int stackCount;
    public FishingNetItem(Settings settings, int stackCount) {
        super(settings);
        this.stackCount = stackCount;
    }

    public boolean canInsert(ItemStack fishingNetStack, ItemStack fishStack) {
        if (!(getContent(fishingNetStack) instanceof FishingNetContentComponent fishingNetContentComponent)) {
            return false;
        }
        if (!fishStack.isOf(FishUtil.DEFAULT_FISH_ITEM)) {
            return false;
        }

        return new FishingNetContentComponent.Builder(fishingNetContentComponent).getMaxAllowed(fishStack) > 0;
    }


    public boolean insertStack(ItemStack fishingNetStack, ItemStack fishStack, PlayerEntity player) {

        if (!(getContent(fishingNetStack) instanceof FishingNetContentComponent fishingNetContentComponent)) {
            return false;
        }

        if (!fishStack.isOf(FishUtil.DEFAULT_FISH_ITEM)) {
            return false;
        }

        FishingNetContentComponent.Builder builder = new FishingNetContentComponent.Builder(fishingNetContentComponent);
        if (builder.add(fishStack) == 0) {
            return false;
        }

        setContent(fishingNetStack, builder.build(fishingNetContentComponent.stackCount()));
        this.playInsertSound(player);
        return true;

    }

    @Override
    public boolean onStackClicked(ItemStack stack, Slot slot, ClickType clickType, PlayerEntity player) {
        if (clickType != ClickType.RIGHT) {
            return false;
        } else {
            FishingNetContentComponent bundleContentsComponent = getContent(stack);
            if (bundleContentsComponent == null) {
                return false;
            } else {
                ItemStack itemStack = slot.getStack();
                FishingNetContentComponent.Builder builder = new FishingNetContentComponent.Builder(bundleContentsComponent);
                if (itemStack.isEmpty()) {
                    ItemStack itemStack2 = builder.removeFirst();
                    if (itemStack2 != null) {
                        this.playRemoveOneSound(player);
                        ItemStack itemStack3 = slot.insertStack(itemStack2);
                        builder.add(itemStack3);
                    }
                } else if (itemStack.getItem().canBeNested() && itemStack.isOf(FishUtil.DEFAULT_FISH_ITEM)) {
                    int i = builder.add(slot, player);
                    if (i > 0) {
                        this.playInsertSound(player);
                    }
                }

                setContent(stack, builder.build(bundleContentsComponent.stackCount()));
                return true;
            }
        }
    }


    public FishingNetContentComponent getContent(ItemStack stack) {
        return FishingNetContentComponent.of(stack);
    }

    public void setContent(ItemStack stack, FishingNetContentComponent bundleContentsComponent) {
        stack.set(FCComponents.FISHING_NET_CONTENT, bundleContentsComponent);
    }

    @Override
    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
        if (clickType == ClickType.RIGHT && slot.canTakePartial(player)) {
            FishingNetContentComponent bundleContentsComponent = getContent(stack);
            if (bundleContentsComponent == null) {
                return false;
            } else {
                FishingNetContentComponent.Builder builder = new FishingNetContentComponent.Builder(bundleContentsComponent);
                if (otherStack.isEmpty()) {
                    ItemStack itemStack = builder.removeFirst();
                    if (itemStack != null) {
                        this.playRemoveOneSound(player);
                        cursorStackReference.set(itemStack);
                    }
                } else  if (otherStack.isOf(FishUtil.DEFAULT_FISH_ITEM)){
                    int i = builder.add(otherStack);
                    if (i > 0) {
                        this.playInsertSound(player);
                    }
                }

                setContent(stack, builder.build(bundleContentsComponent.stackCount()));
                return true;
            }
        } else {
            return false;
        }
    }



    private void playRemoveOneSound(Entity entity) {
        entity.playSound(SoundEvents.ITEM_BUNDLE_REMOVE_ONE, 0.8F, 0.8F + entity.getWorld().getRandom().nextFloat() * 0.4F);
    }

    private void playInsertSound(Entity entity) {
        entity.playSound(SoundEvents.ITEM_BUNDLE_INSERT, 0.8F, 0.8F + entity.getWorld().getRandom().nextFloat() * 0.4F);
    }

    private void playDropContentsSound(Entity entity) {
        entity.playSound(SoundEvents.ITEM_BUNDLE_DROP_CONTENTS, 0.8F, 0.8F + entity.getWorld().getRandom().nextFloat() * 0.4F);
    }


    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        FishingNetContentComponent fishingNetContent = getContent(stack);
        return fishingNetContent.getOccupancy().compareTo(Fraction.ZERO) > 0;
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        FishingNetContentComponent fishingNetContent = getContent(stack);
        return Math.min(1 + MathHelper.multiplyFraction(fishingNetContent.getOccupancy(), 12), 13);
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        FishingNetContentComponent fishingNetContent = getContent(stack);
        if (fishingNetContent != null) {
            int i = MathHelper.multiplyFraction(fishingNetContent.getOccupancy(), fishingNetContent.stackCount() * 64);
            tooltip.add(Text.translatable("item.minecraft.bundle.fullness", i, fishingNetContent.stackCount() * 64).formatted(Formatting.GRAY));
        }
    }

}
