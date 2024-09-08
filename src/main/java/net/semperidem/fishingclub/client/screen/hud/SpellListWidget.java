package net.semperidem.fishingclub.client.screen.hud;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.semperidem.fishingclub.client.screen.DataBuffer;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.fisher.tradesecret.TradeSecret;
import net.semperidem.fishingclub.registry.FCKeybindings;

import java.awt.*;
import java.util.ArrayList;

public class SpellListWidget implements DataBuffer {
    public static TradeSecret.Instance selectedInstance;
    public static int selectedSpellIndex;
    public static ArrayList<TradeSecret.Instance> usableTradeSecrets = new ArrayList<>();
    public static boolean pressed = false;
    float xPercent = 0.55f;
    float yPercent = 0.55f;
    private static final int spaceBetween = 16;
    private static final int entryWidth = 150;
    private long lastRefreshTime;
    private FishingCard card;

    public static void stickPress(){
        if (pressed) return;
        pressed = true;
    }

    public void render(DrawContext context, RenderTickCounter tickCounter){
        if (this.card == null) {
            this.card = FishingCard.of(MinecraftClient.getInstance().player);
        }
        this.refresh();
        selectedInstance = usableTradeSecrets.get(selectedSpellIndex);
        if(selectedInstance == null){
            if (pressed) pressed = false;
            return;
        }
        if (!FCKeybindings.SPELL_SELECT.isPressed()){
            pressed = false;
            return;
        }
        int x = (int) (MinecraftClient.getInstance().getWindow().getScaledWidth() * xPercent);
        int y = (int) (MinecraftClient.getInstance().getWindow().getScaledHeight() * yPercent);
        renderSpell(context, selectedInstance, x, y, Color.WHITE.getRGB(), 0x99000000);
        if (usableTradeSecrets.size() > 1) {
            renderSpell(context, previous(selectedSpellIndex), x, y - spaceBetween, Color.WHITE.getRGB(), 0x66000000);
        }
        if (usableTradeSecrets.size() > 2) {
            renderSpell(context, next(selectedSpellIndex), x, y + spaceBetween, Color.WHITE.getRGB(), 0x66000000);
        }
    }


    private void renderSpell(DrawContext context, TradeSecret.Instance instance, int x, int y, int color, int bgColor){
        String spellName = instance.root().name();
        if (selectedInstance == instance) {
            spellName = "> " + spellName + " <";
        }
        String spellCd = getCooldownString(instance);
        int spellCdLen = MinecraftClient.getInstance().textRenderer.getWidth(spellCd);
        context.fill(x - 4, y - 4, x + entryWidth, y + 12, bgColor);
        context.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, spellName , x,y, color);
        context.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, spellCd , x + entryWidth - spellCdLen - 4,y, color);
    }

    private int getCooldown(TradeSecret.Instance instance) {
        return (int) Math.max(0, instance.nextUseTime() - MinecraftClient.getInstance().world.getTime());
    }

    private String getCooldownString(TradeSecret.Instance instance){
        int cdTimer = getCooldown(instance);
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

    private TradeSecret.Instance previous(int selectedInstance){
        int previousInstance = selectedInstance - 1;
        if (previousInstance < 0){
            previousInstance = usableTradeSecrets.size() - 1;
        }
        return usableTradeSecrets.get(previousInstance);
    }

    private TradeSecret.Instance next(int selectedSpellIndex){
        int nextSpellIndex = selectedSpellIndex + 1;
        if (nextSpellIndex == usableTradeSecrets.size()){
            nextSpellIndex = 0;
        }
        return usableTradeSecrets.get(nextSpellIndex);

    }

    public static void scroll(int scrollAmount){
        if (usableTradeSecrets.isEmpty()) return;
        int index = usableTradeSecrets.indexOf(selectedInstance) + (int)Math.signum(scrollAmount * -1);
        if (index >= usableTradeSecrets.size()) {
            index = 0;
        }
        if (index < 0) {
            index = usableTradeSecrets.size() - 1;
        }
        selectedInstance = usableTradeSecrets.get(index);
        selectedSpellIndex = index;
    }

    @Override
    public long getTime() {
        return card.holder().getWorld().getTime();
    }

    @Override
    public int getRefreshRate() {
        return 20;
    }

    @Override
    public long lastRefreshTime() {
        return this.lastRefreshTime;
    }

    @Override
    public void lastRefreshTime(long time) {
        this.lastRefreshTime = time;
    }

    @Override
    public void refresh() {
        usableTradeSecrets.clear();
        usableTradeSecrets.addAll(card.tradeSecrets().stream().filter(tradeSecret -> tradeSecret.root().hasActive()).toList());
        if (usableTradeSecrets.isEmpty()) {
            return;
        }
        selectedInstance = usableTradeSecrets.getFirst();
    }
}
