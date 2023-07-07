package net.semperidem.fishingclub.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.client.screen.FisherInfoScreen;
import net.semperidem.fishingclub.client.screen.shop.ShopScreenUtil;
import net.semperidem.fishingclub.network.ClientPacketSender;
import org.lwjgl.glfw.GLFW;

import static net.semperidem.fishingclub.FishingClub.MOD_ID;

public class FishingClubClient implements ClientModInitializer {
    private static KeyBinding infoScreenKeybind;
    private static KeyBinding shopSellScreenKeybind;
    /**
     * Runs the mod initializer on the client environment.
     */
    @Override
    public void onInitializeClient() {
        ShopScreenUtil.registerClient();
        infoScreenKeybind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key." + MOD_ID + ".fisher_info_screen", // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_F, // The keycode of the key
                "category."+ MOD_ID +".fishing_club_category" // The translation key of the keybinding's category.
        ));
        shopSellScreenKeybind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key." + MOD_ID + ".fisher_shop_screen", // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_L, // The keycode of the key
                "category."+ MOD_ID +".fishing_club_category" // The translation key of the keybinding's category.
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (infoScreenKeybind.wasPressed()) {
                client.setScreen(new FisherInfoScreen(Text.of("Fisher Info")));
            }

            while (shopSellScreenKeybind.wasPressed()) {
                ClientPacketSender.sendOpenSellShopRequest();
            }
        });

        ModelPredicateProviderRegistry.register(FishingClub.CUSTOM_FISHING_ROD, new Identifier("casting"), (itemStack, clientWorld, livingEntity, clamp) -> {
            if (livingEntity == null) {
                return 0.0F;
            }
            if (!(livingEntity instanceof PlayerEntity)) {
                return 0.0F;
            }
            return ((PlayerEntity)livingEntity).fishHook != null ? 1.0F : 0.0F;
        });
    }
}
