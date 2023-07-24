package net.semperidem.fishingclub.item;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class FishingNetItem extends Item {

    public FishingNetItem(Settings settings) {
        super(settings);
    }

    public static void openScreen(PlayerEntity player, ItemStack backpackItemStack) {
        if (player.world != null && !player.world.isClient) {
            player.openHandledScreen(new ExtendedScreenHandlerFactory() {
                @Override
                public void writeScreenOpeningData(ServerPlayerEntity serverPlayerEntity, PacketByteBuf packetByteBuf) {
                    packetByteBuf.writeItemStack(backpackItemStack);
                }

                @Override
                public Text getDisplayName() {
                    return Text.of(backpackItemStack.getItem().getTranslationKey());
                }

                @Override
                public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
                    //TODO create new screen handler :DD
                    return GenericContainerScreenHandler.createGeneric9x3(syncId, inv, read(backpackItemStack, new SimpleInventory(27)));
                }
            });
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        openScreen(user, user.getMainHandStack());
        return TypedActionResult.success(user.getMainHandStack());
    }

    public static SimpleInventory read(ItemStack backpackStack, SimpleInventory inventory){
        NbtList backpackContentTag = backpackStack.getOrCreateNbt().getList("content", NbtCompound.COMPOUND_TYPE);
        inventory.clear();
        backpackContentTag.forEach(e -> {
            NbtCompound stackTag = (NbtCompound) e;
            int slot = stackTag.getInt("slot");
            ItemStack stack = ItemStack.fromNbt(stackTag.getCompound("stack"));
            inventory.setStack(slot, stack);
        });
        return inventory;
    }


    public ItemStack write(ItemStack backpackStack, SimpleInventory inventory){
        NbtList backpackContentTag = new NbtList();
        for(int slot = 0; slot < inventory.size(); slot++) {
            ItemStack stack = inventory.getStack(slot);
            if (!stack.isEmpty()) {
                NbtCompound stackTag = new NbtCompound();
                stackTag.putInt("slot", slot);
                stackTag.put("stack", stack.writeNbt(new NbtCompound()));
            }
        }
        backpackStack.getOrCreateNbt().put("content", backpackContentTag);
        return backpackStack;
    }
}
