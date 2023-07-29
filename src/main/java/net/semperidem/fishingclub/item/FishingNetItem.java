package net.semperidem.fishingclub.item;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.semperidem.fishingclub.client.screen.fishing_net.FishingNetScreenHandler;
import org.jetbrains.annotations.Nullable;

public class FishingNetItem extends Item {

    public FishingNetItem(Settings settings) {
        super(settings);
    }

    public static void openScreen(PlayerEntity player, ItemStack fishingNetStack) {
        if (player.world != null && !player.world.isClient) {
            player.openHandledScreen(new ExtendedScreenHandlerFactory() {
                @Override
                public void writeScreenOpeningData(ServerPlayerEntity serverPlayerEntity, PacketByteBuf packetByteBuf) {
                    packetByteBuf.writeItemStack(fishingNetStack);
                }

                @Override
                public Text getDisplayName() {
                    return Text.of(fishingNetStack.getItem().getTranslationKey());
                }

                @Override
                public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
                    return new FishingNetScreenHandler(syncId, inv, fishingNetStack);
                }
            });
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        openScreen(user, user.getMainHandStack());
        return TypedActionResult.success(user.getMainHandStack());
    }
}
