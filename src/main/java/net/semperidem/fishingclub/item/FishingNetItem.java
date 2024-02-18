package net.semperidem.fishingclub.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.semperidem.fishingclub.client.screen.fishing_net.FishingNetScreenFactory;

public class FishingNetItem extends Item {
    public int size;
    private static final String INVENTORY_TAG = "Items";

    public FishingNetItem(Settings settings) {
        super(settings);
        this.size = 27;
    }

    public static void openScreen(PlayerEntity player, ItemStack fishingNetStack) {
        if (player.world != null && !player.world.isClient) {
            player.openHandledScreen(new FishingNetScreenFactory(fishingNetStack));
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        openScreen(user, user.getMainHandStack());
        return TypedActionResult.success(user.getMainHandStack());
    }

    public static SimpleInventory readFishingNetInventory(ItemStack fishingNetStack){
        int size = 1;
        if (fishingNetStack.getItem() instanceof FishingNetItem netItem) {
            size = netItem.size;
        }
        SimpleInventory inventory = new SimpleInventory(size){
            @Override
            public void markDirty() {
                fishingNetStack.setNbt(writeFishingNetInventory(this));
                super.markDirty();
            }
        };
        inventory.readNbtList(fishingNetStack.getOrCreateNbt().getList(INVENTORY_TAG, NbtElement.COMPOUND_TYPE));
        return inventory;
    }

    public static NbtCompound writeFishingNetInventory(SimpleInventory inventory){
        NbtCompound inventoryTag = new NbtCompound();
        inventoryTag.put(INVENTORY_TAG, inventory.toNbtList());
        return inventoryTag;
    }
}
