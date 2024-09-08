package net.semperidem.fishingclub.registry;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.semperidem.fishingclub.client.screen.hud.SpellListWidget;
import net.semperidem.fishingclub.network.payload.SpellCastPayload;
import net.semperidem.fishingclub.network.payload.SpellCastWithTargetPayload;
import org.lwjgl.glfw.GLFW;

import java.util.UUID;

import static net.semperidem.fishingclub.FishingClub.MOD_ID;

public class FCKeybindings {
    public final static KeyBinding CAST_SPELL = registerKeybinding("cast_spell", "spell", GLFW.GLFW_KEY_N);
    public final static KeyBinding SPELL_SELECT = registerKeybinding("spell_select", "spell", GLFW.GLFW_KEY_M);
    public final static KeyBinding MULTIPLY_CART_ACTION_1 = registerKeybinding("multiply_cart_action_1", "shop", GLFW.GLFW_KEY_LEFT_SHIFT);
    public final static KeyBinding MULTIPLY_CART_ACTION_2 = registerKeybinding("multiply_cart_action_2", "shop", GLFW.GLFW_KEY_LEFT_ALT);
    public final static KeyBinding REEL = registerKeybinding("reel", "game", GLFW.GLFW_KEY_SPACE);//useless
    public final static KeyBinding PULL = registerKeybinding("pull", "game", GLFW.GLFW_KEY_ENTER);

    public static void registerClient() {
        registerAction(FCKeybindings::spellCast);
        registerAction(FCKeybindings::spellSelect);
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

    private static void spellCast(MinecraftClient client) {
        if (!CAST_SPELL.wasPressed()) {
            return;
        }
        if (SpellListWidget.selectedInstance == null) {
            return;
        }
        if (client.player == null) {
            return;
        }
        HitResult hitResult = client.player.raycast(5, 0, false);
        UUID targetUUID = null;
        if (hitResult instanceof EntityHitResult entityHitResult) {
            targetUUID = entityHitResult.getEntity().getUuid();
        }
        if (hitResult.getType() == HitResult.Type.ENTITY) {
            ClientPlayNetworking.send(new SpellCastWithTargetPayload(SpellListWidget.selectedInstance.name(), targetUUID));
            return;
        }
        ClientPlayNetworking.send(new SpellCastPayload(SpellListWidget.selectedInstance.name()));
    }

    private static void spellSelect(MinecraftClient client) {
        if (SPELL_SELECT.wasPressed()) {
            SpellListWidget.stickPress();
        }
    }
}
