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

import java.util.List;

public class FishingNetItem extends BundleItem {
    public int size;
    public FishingNetItem(Settings settings) {
        super(settings);
    }
//overide component builder maxAllowedOcc method and change Fraction from ONE to X
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
                    this.playRemoveOneSound(player);
                    ItemStack itemStack2 = builder.removeFirst();
                    if (itemStack2 != null) {
                        ItemStack itemStack3 = slot.insertStack(itemStack2);
                        builder.add(itemStack3);
                    }
                } else if (itemStack.getItem().canBeNested() && itemStack.isOf(FishUtil.FISH_ITEM)) {
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
        return stack.getOrDefault(FCComponents.FISHING_NET_CONTENT, FishingNetContentComponent.DEFAULT);
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
                } else  if (otherStack.isOf(FishUtil.FISH_ITEM)){
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
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        FishingNetContentComponent bundleContentsComponent = getContent(stack);
        if (bundleContentsComponent != null) {
            int i = MathHelper.multiplyFraction(bundleContentsComponent.getOccupancy(), bundleContentsComponent.stackCount() * 64);
            tooltip.add(Text.translatable("item.minecraft.bundle.fullness", i, bundleContentsComponent.stackCount() * 64).formatted(Formatting.GRAY));
        }
    }

}
