package net.semperidem.fishingclub.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.BundleItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ClickType;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.semperidem.fishingclub.fish.FishUtil;
import net.semperidem.fishingclub.fish.specimen.SpecimenComponent;
import net.semperidem.fishingclub.registry.Components;
import org.apache.commons.lang3.math.Fraction;

import java.util.List;

public class FishingNetItem extends BundleItem {
    public int stackCount;
    public FishingNetItem(Settings settings, int stackCount) {
        super(settings);
        this.stackCount = stackCount;
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if (!(entity instanceof WaterCreatureEntity waterCreatureEntity)) {
            return ActionResult.PASS;
        }
        if (user.getWorld().isClient) {
            return ActionResult.PASS;
        }
        ItemStack stackInHand = user.getStackInHand(hand);
        if (!(stackInHand.getItem() instanceof FishingNetItem fishingNetItem)) {
            return ActionResult.PASS;

        }
        ItemStack fishStack = SpecimenComponent.of(waterCreatureEntity).getOrDefault().asItemStack();
        if (fishingNetItem.insertStack(stackInHand, fishStack, user)) {
            entity.discard();
        }
        return ActionResult.SUCCESS;
    }

    public boolean canInsert(ItemStack fishingNetStack, ItemStack fishStack) {
        if (!(getContent(fishingNetStack) instanceof NetContentComponent netContentComponent)) {
            return false;
        }
        if (!FishUtil.isFish(fishStack)) {
            return false;
        }

        return new NetContentComponent.Builder(netContentComponent).getMaxAllowed(fishStack) > 0;
    }


    public boolean insertStack(ItemStack fishingNetStack, ItemStack fishStack, PlayerEntity player) {

        if (!(getContent(fishingNetStack) instanceof NetContentComponent netContentComponent)) {
            return false;
        }

        if (!FishUtil.isFish(fishStack)) {
            return false;
        }

        NetContentComponent.Builder builder = new NetContentComponent.Builder(netContentComponent);
        if (builder.add(fishStack) == 0) {
            return false;
        }

        setContent(fishingNetStack, builder.build(netContentComponent.stackCount()));
        this.playInsertSound(player);
        return true;

    }

    @Override
    public boolean onStackClicked(ItemStack stack, Slot slot, ClickType clickType, PlayerEntity player) {
        if (clickType != ClickType.RIGHT) {
            return false;
        } else {
            NetContentComponent bundleContentsComponent = getContent(stack);
            if (bundleContentsComponent == null) {
                return false;
            } else {
                ItemStack itemStack = slot.getStack();
                NetContentComponent.Builder builder = new NetContentComponent.Builder(bundleContentsComponent);
                if (itemStack.isEmpty()) {
                    ItemStack itemStack2 = builder.removeFirst();
                    if (itemStack2 != null) {
                        this.playRemoveOneSound(player);
                        ItemStack itemStack3 = slot.insertStack(itemStack2);
                        builder.add(itemStack3);
                    }
                } else if (itemStack.getItem().canBeNested() && FishUtil.isFish(itemStack)) {
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


    public NetContentComponent getContent(ItemStack stack) {
        return NetContentComponent.of(stack);
    }

    public void setContent(ItemStack stack, NetContentComponent bundleContentsComponent) {
        stack.set(Components.NET_CONTENT, bundleContentsComponent);
    }

    @Override
    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
        if (clickType == ClickType.RIGHT && slot.canTakePartial(player)) {
            NetContentComponent bundleContentsComponent = getContent(stack);
            if (bundleContentsComponent == null) {
                return false;
            } else {
                NetContentComponent.Builder builder = new NetContentComponent.Builder(bundleContentsComponent);
                if (otherStack.isEmpty()) {
                    ItemStack itemStack = builder.removeFirst();
                    if (itemStack != null) {
                        this.playRemoveOneSound(player);
                        cursorStackReference.set(itemStack);
                    }
                } else  if (FishUtil.isFish(otherStack)){
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
        NetContentComponent fishingNetContent = getContent(stack);
        return fishingNetContent.getOccupancy().compareTo(Fraction.ZERO) > 0;
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        NetContentComponent fishingNetContent = getContent(stack);
        return Math.min(1 + MathHelper.multiplyFraction(fishingNetContent.getOccupancy(), 12), 13);
    }

    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        NetContentComponent fishingNetContent = getContent(stack);
        if (fishingNetContent != null) {
            int i = MathHelper.multiplyFraction(fishingNetContent.getOccupancy(), fishingNetContent.stackCount() * 64);
            tooltip.add(Text.translatable("item.minecraft.bundle.fullness", i, fishingNetContent.stackCount() * 64).formatted(Formatting.GRAY));
        }
    }

}
