package net.semperidem.fishingclub.client.screen.hud;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
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
    float xPercent = 0.55f;
    float yPercent = 0.55f;
    private static long animationTimeStart = 0;
    float minScale = 0.85f;
    private static final long animationTimeInMilis = 250;
    private static final int spaceBetween = 16;
    private static final int entryWidth = 150;

    public static void updateFisherInfo(FisherInfo newFisherInfo){
        fisherInfo = newFisherInfo;
        availableSpells.clear();
        availableSpells.addAll(fisherInfo.getSpells());
        if (availableSpells.size() > 0 && selectedSpell == null) {
            selectedSpell = availableSpells.get(0);
        }
    }

    public static void stickPress(FisherInfo newFisherInfo){
        if (pressed) return;
        updateFisherInfo(newFisherInfo);
        pressed = true;
    }
    public void render(MatrixStack matrices, float tickDelta){
        if(selectedSpell == null) return;
        if (!FKeybindingRegistry.SPELL_SELECT_KB.isPressed()){
            pressed = false;
            return;
        }
        int x = (int) (MinecraftClient.getInstance().getWindow().getScaledWidth() * xPercent);
        int y = (int) (MinecraftClient.getInstance().getWindow().getScaledHeight() * yPercent);
        renderSpell(matrices, prevSpell(selectedSpellIndex), x, y - spaceBetween, Color.WHITE.getRGB(), 0x66000000);
        renderSpell(matrices, selectedSpell, x, y, Color.WHITE.getRGB(), 0x99000000);
        renderSpell(matrices, nextSpell(selectedSpellIndex), x, y + spaceBetween, Color.WHITE.getRGB(), 0x66000000);
        matrices.pop();
        animationTimeStart++;
    }


    private void renderSpell(MatrixStack matrixStack, SpellInstance spell, int x, int y, int color, int bgColor){
        String spellName = spell.getLabel();
        String spellCd = getCooldownString(spell);
        int spellCdLen = MinecraftClient.getInstance().textRenderer.getWidth(spellCd);
        DrawableHelper.fill(matrixStack, x - 4, y - 4, x + entryWidth, y + 12, bgColor);
        MinecraftClient.getInstance().textRenderer.drawWithShadow(matrixStack, spellName , x,y, color);
        MinecraftClient.getInstance().textRenderer.drawWithShadow(matrixStack, spellCd , x + entryWidth - spellCdLen - 4,y, color);

    }

    private int getCooldown(SpellInstance spellInstance) {
        return (int) Math.max(0, spellInstance.getNextCast() - MinecraftClient.getInstance().world.getTime());
    }

    private String getCooldownString(SpellInstance spellInstance){
        int cdTimer = getCooldown(spellInstance);
        String result = "";
        int cdSeconds = cdTimer / 20;
        int hours = cdSeconds / 3600;
        int minutes = (cdSeconds % 3600) / 60;
        int seconds = cdSeconds % 60;
        if (hours > 0) {
            result += String.format("[%02dh %02dm %02ds]", hours, minutes, seconds);
        } else if (minutes > 0){
            result += String.format("[%02dm %02ds]", minutes, seconds);
        } else if (seconds > 0){
            result += String.format("[%02ds]", seconds);
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
        animationTimeStart = System.currentTimeMillis();
    }
}
