package net.semperidem.fishingclub.registry;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.semperidem.fishingclub.client.screen.hud.TradeSecretCastScreen;
import net.semperidem.fishingclub.network.payload.SpellCastPayload;
import net.semperidem.fishingclub.network.payload.SpellCastWithTargetPayload;
import org.lwjgl.glfw.GLFW;

import java.util.UUID;

import static net.semperidem.fishingclub.FishingClub.MOD_ID;

public class FCKeybindings {
    public final static KeyBinding MULTIPLY_CART_ACTION_1 = registerKeybinding("multiply_cart_action_1", "shop", GLFW.GLFW_KEY_LEFT_SHIFT);
    public final static KeyBinding MULTIPLY_CART_ACTION_2 = registerKeybinding("multiply_cart_action_2", "shop", GLFW.GLFW_KEY_LEFT_ALT);

    public static void registerClient() {
    }

    private static void registerAction(ClientTickEvents.EndTick endTickAction) {
        ClientTickEvents.END_CLIENT_TICK.register(endTickAction);
    }

    private static KeyBinding registerKeybinding(String keyTitle, String keyCategory, int keyCode) {
        return KeyBindingHelper.registerKeyBinding(
                new KeyBinding(
                        "key." + MOD_ID + "." + keyTitle,
                        InputUtil.Type.KEYSYM,
                        keyCode,
                        "category." + MOD_ID + "." + keyCategory
                )
        );
    }

}
