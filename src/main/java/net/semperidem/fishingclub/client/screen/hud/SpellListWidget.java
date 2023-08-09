package net.semperidem.fishingclub.client.screen.hud;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.semperidem.fishingclub.fisher.FisherInfo;
import net.semperidem.fishingclub.fisher.perks.spells.SpellInstance;
import net.semperidem.fishingclub.registry.FKeybindingRegistry;

import java.awt.*;
import java.util.ArrayList;

public class SpellListWidget{
    public static SpellInstance selectedSpell;
    public static int selectedSpellIndex;
    public static ArrayList<SpellInstance> availableSpells = new ArrayList<>();
    private static FisherInfo fisherInfo;
    public static boolean pressed = false;

    public static void updateFisherInfo(FisherInfo newFisherInfo){
        if (pressed) return;
        fisherInfo = newFisherInfo;
        availableSpells.clear();
        availableSpells.addAll(fisherInfo.getSpells());
        if (availableSpells.size() > 0 && selectedSpell == null) {
            selectedSpell = availableSpells.get(0);
        }
    }

    public static void stickPress(FisherInfo newFisherInfo){
        updateFisherInfo(newFisherInfo);
        pressed = true;
    }
    public void render(MatrixStack matrices, float tickDelta){
        if (!FKeybindingRegistry.SPELL_SELECT_KB.isPressed()){
            pressed = false;
            return;
        }
        if(selectedSpell == null) return;
        renderSpell(matrices, prevSpell(selectedSpellIndex), 100, 20, Color.WHITE.getRGB());
        renderSpell(matrices, selectedSpell, 100, 40, Color.RED.getRGB());
        renderSpell(matrices, nextSpell(selectedSpellIndex), 100, 60, Color.WHITE.getRGB());
    }


    private void renderSpell(MatrixStack matrixStack, SpellInstance spell, int x, int y, int color){
        String text = spell.getKey() + " " + getCooldownString(spell);
        MinecraftClient.getInstance().textRenderer.drawWithShadow(matrixStack, text , x,y, color);

    }

    private int getCooldown(SpellInstance spellInstance) {
        return (int) Math.max(0, spellInstance.getNextCast() - MinecraftClient.getInstance().world.getTime());
    }

    private String getCooldownString(SpellInstance spellInstance){
        int cdTimer = getCooldown(spellInstance);
        String result = "CD: [";
        int cdSeconds = cdTimer / 20;
        int hours = cdSeconds / 3600;
        int minutes = (cdSeconds % 3600) / 60;
        int seconds = cdSeconds % 60;
        if (hours > 0) {
            result += String.format("%02d:%02d:%02d", hours, minutes, seconds) + "]";
        } else if (minutes > 0){
            result += String.format("%02d:%02d", minutes, seconds) + "]";
        } else if (seconds > 0){
            result += String.format("%02d", seconds) + "]";
        } else {
            result = "";
        }
        return result;
    }

    private SpellInstance prevSpell(int selectedSpellIndex){
        int prevSpellIndex = selectedSpellIndex - 1;
        if (prevSpellIndex < 0){
            prevSpellIndex = availableSpells.size() - 1;
        }
        return availableSpells.get(prevSpellIndex);
    }

    private SpellInstance nextSpell(int selectedSpellIndex){
        int nextSpellIndex = selectedSpellIndex + 1;
        if (nextSpellIndex == availableSpells.size()){
            nextSpellIndex = 0;
        }
        return availableSpells.get(nextSpellIndex);

    }

    public static void scrollSpells(int scrollAmount){
        if (availableSpells.size() == 0) return;
        int index = availableSpells.indexOf(selectedSpell) + (int)Math.signum(scrollAmount);
        if (index >= availableSpells.size()) {
            index = 0;
        }
        if (index < 0) {
            index = availableSpells.size() - 1;
        }
        selectedSpell = availableSpells.get(index);
        selectedSpellIndex = index;
    }
}
