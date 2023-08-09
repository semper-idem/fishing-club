package net.semperidem.fishingclub.registry;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.semperidem.fishingclub.client.screen.hud.SpellListWidget;
import net.semperidem.fishingclub.fisher.FisherInfoManager;
import net.semperidem.fishingclub.network.ClientPacketSender;
import org.lwjgl.glfw.GLFW;

import static net.semperidem.fishingclub.FishingClub.MOD_ID;

public class FKeybindingRegistry {
    private final static KeyBinding FISHER_INFO_SCREEN_KB = registerKeybinding("fisher_info_screen", "misc", GLFW.GLFW_KEY_F);
    public final static KeyBinding CAST_SPELL_KB = registerKeybinding("cast_spell", "misc", GLFW.GLFW_KEY_N);
    public final static KeyBinding SPELL_SELECT_KB = registerKeybinding("spell_select", "misc", GLFW.GLFW_KEY_M);

    public static void registerClient(){

        ClientTickEvents.END_CLIENT_TICK.register(openFisherInfoScreen());
        ClientTickEvents.END_CLIENT_TICK.register(castSpell());
        ClientTickEvents.END_CLIENT_TICK.register(openSpellSelect());
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
    private static ClientTickEvents.EndTick castSpell(){
        return client -> {
            while (CAST_SPELL_KB.wasPressed()) {
                if (SpellListWidget.selectedSpell != null) {
                    ClientPacketSender.castSpell(SpellListWidget.selectedSpell.getKey());
                };
            }
        };
    }
    private static ClientTickEvents.EndTick openSpellSelect(){
        return client -> {
            if (SPELL_SELECT_KB.wasPressed()) {
                SpellListWidget.stickPress(FisherInfoManager.getFisher(client.player));
            }

        };
    }
}
