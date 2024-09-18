package net.semperidem.fishingclub.client.screen.hud;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.semperidem.fishingclub.fisher.tradesecret.TradeSecret;
import net.semperidem.fishingclub.network.payload.SpellCastPayload;
import net.semperidem.fishingclub.network.payload.SpellCastWithTargetPayload;

import java.awt.*;
import java.util.List;
import java.util.UUID;

public class TradeSecretCastScreen extends Screen {
    public int selectedIndex;
    public List<TradeSecret.Instance> usableTradeSecrets;
    float xPercent = 0.55f;
    float yPercent = 0.55f;
    private static final int spaceBetween = 16;
    private static final int entryWidth = 150;

    public TradeSecretCastScreen(List<TradeSecret.Instance> usableTradeSecrets) {
        super(Text.empty());
        this.usableTradeSecrets = usableTradeSecrets;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        KeyBinding.setKeyPressed(InputUtil.fromKeyCode(keyCode, scanCode), true);
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        super.mouseMoved(mouseX, mouseY);
    }

    @Override
    protected void init() {
        super.init();
        if (client == null) {
            return;
        }
        MinecraftClient.getInstance().mouse.lockCursor();
        MinecraftClient.getInstance().currentScreen = this;
    }


    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (usableTradeSecrets.isEmpty()) {
            return false;
        }

        int index = this.selectedIndex + (int) Math.signum(verticalAmount * -1);
        if (index >= usableTradeSecrets.size()) {
            index = 0;
        }
        if (index < 0) {
            index = usableTradeSecrets.size() - 1;
        }
        this.selectedIndex = index;
        return true;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        TradeSecret.Instance selectedSecret = usableTradeSecrets.get(this.selectedIndex);
        int x = (int) (MinecraftClient.getInstance().getWindow().getScaledWidth() * xPercent);
        int y = (int) (MinecraftClient.getInstance().getWindow().getScaledHeight() * yPercent);
        renderSecret(context, selectedSecret, x, y, Color.WHITE.getRGB(), 0x99000000, true);
        if (usableTradeSecrets.size() > 1) {
            renderSecret(context, previous(selectedIndex), x, y - spaceBetween, Color.WHITE.getRGB(), 0x66000000);
        }
        if (usableTradeSecrets.size() > 2) {
            renderSecret(context, next(selectedIndex), x, y + spaceBetween, Color.WHITE.getRGB(), 0x66000000);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        HitResult hitResult = client.player.raycast(5, 0, false);
        UUID targetUUID = null;
        if (hitResult instanceof EntityHitResult entityHitResult) {
            targetUUID = entityHitResult.getEntity().getUuid();
        }
        String tradeSecretName = usableTradeSecrets.get(selectedIndex).root().name();
        if (hitResult.getType() == HitResult.Type.ENTITY) {
            ClientPlayNetworking.send(new SpellCastWithTargetPayload(tradeSecretName, targetUUID));
            return true;
        }
        ClientPlayNetworking.send(new SpellCastPayload(tradeSecretName));
        this.close();
        return true;
    }

    private void renderSecret(DrawContext context, TradeSecret.Instance secret, int x, int y, int color, int backgroundColor) {
        renderSecret(context, secret, x, y, color, backgroundColor, false);
    }

    private void renderSecret(DrawContext context, TradeSecret.Instance secret, int x, int y, int color, int backgroundColor, boolean isSelected) {
        String cooldown = getCooldownString(secret);
        String name = secret.root().name();
        if (isSelected) {
            name = ">" + name + "<";
        }
        int spellCdLen = MinecraftClient.getInstance().textRenderer.getWidth(cooldown);
        context.fill(x - 4, y - 4, x + entryWidth, y + 12, backgroundColor);
        context.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, name, x, y, color);
        context.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, cooldown, x + entryWidth - spellCdLen - 4, y, color);
    }

    private int getCooldown(TradeSecret.Instance instance) {
        return (int) Math.max(0, instance.nextUseTime() - MinecraftClient.getInstance().world.getTime());
    }

    private String getCooldownString(TradeSecret.Instance instance) {
        int cdTimer = getCooldown(instance);
        String result = "";
        int cdSeconds = cdTimer / 20;
        int hours = cdSeconds / 3600;
        int minutes = (cdSeconds % 3600) / 60;
        int seconds = cdSeconds % 60;
        if (hours > 0) {
            result += String.format("[%02dh %02dm %02ds]", hours, minutes, seconds);
        } else if (minutes > 0) {
            result += String.format("[%02dm %02ds]", minutes, seconds);
        } else if (seconds > 0) {
            result += String.format("[%02ds]", seconds);
        }
        return result;
    }

    private TradeSecret.Instance previous(int selectedInstance) {
        int previousInstance = selectedInstance - 1;
        if (previousInstance < 0) {
            previousInstance = usableTradeSecrets.size() - 1;
        }
        return usableTradeSecrets.get(previousInstance);
    }

    private TradeSecret.Instance next(int selectedSpellIndex) {
        int nextSpellIndex = selectedSpellIndex + 1;
        if (nextSpellIndex == usableTradeSecrets.size()) {
            nextSpellIndex = 0;
        }
        return usableTradeSecrets.get(nextSpellIndex);

    }
}
