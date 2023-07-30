package net.semperidem.fishingclub.registry;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.semperidem.fishingclub.network.ClientPacketSender;
import org.lwjgl.glfw.GLFW;

import static net.semperidem.fishingclub.FishingClub.MOD_ID;

public class FKeybindingRegistry {
    private static KeyBinding FISHER_INFO_SCREEN_KB;

    public static void registerClient(){

        FISHER_INFO_SCREEN_KB = registerKeybinding("fisher_info_screen", "misc", GLFW.GLFW_KEY_F);

        ClientTickEvents.END_CLIENT_TICK.register(openFisherInfoScreen());
    }

    private static KeyBinding registerKeybinding(String keyTitle, String keyCategory, int key){
        return KeyBindingHelper.registerKeyBinding(
                new KeyBinding(
                        "key." + MOD_ID + "." + keyTitle,
                        InputUtil.Type.KEYSYM,
                        key,
                        "category."+ MOD_ID +"." + keyCategory
                )
        );
    }

    private static ClientTickEvents.EndTick openFisherInfoScreen(){
        return client -> {
            while (FISHER_INFO_SCREEN_KB.wasPressed()) {
                ClientPacketSender.sendOpenFisherInfoScreen();
            }
        };
    }
}
