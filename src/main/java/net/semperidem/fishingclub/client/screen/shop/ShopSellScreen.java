package net.semperidem.fishingclub.client.screen.shop;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.semperidem.fishingclub.fish.fisher.FisherInfo;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class ShopSellScreen extends HandledScreen<ShopScreenHandler> implements ScreenHandlerProvider<ShopScreenHandler> {
    private static final Identifier TEXTURE = new Identifier("minecraft:textures/gui/container/generic_54.png");
    private final int rows = 3;

    int outerHeight = 300;
    int outerWidth = 200;
    int border = 10;
    int innerHeight = outerHeight - border;
    int innerWidth = outerWidth - border;

    int screenX = 0;
    int screenY = 0;
    FisherInfo clientInfo;


    public ShopSellScreen(ShopScreenHandler shopScreenHandler, PlayerInventory playerInventory, Text text) {
        super(shopScreenHandler, playerInventory, text);
        this.passEvents = false;
        this.backgroundHeight = 114 + this.rows * 18;
        this.playerInventoryTitleY = this.backgroundHeight - 94;
    }



    public void render(MatrixStack matrixStack, int i, int j, float f) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, i, j, f);
        this.drawMouseoverTooltip(matrixStack, i, j);
    }

    protected void drawBackground(MatrixStack matrixStack, float f, int i, int j) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int k = (this.width - this.backgroundWidth) / 2;
        int l = (this.height - this.backgroundHeight) / 2;
        this.drawTexture(matrixStack, k, l, 0, 0, this.backgroundWidth, this.rows * 18 + 17);
        this.drawTexture(matrixStack, k, l + this.rows * 18 + 17, 0, 126, this.backgroundWidth, 96);
    }


    public void renderFishContainer(MatrixStack matrices){
        int screenInnerX = (this.width - innerWidth) / 2;
        int screenInnerY = (this.height - innerHeight) / 2;
        DrawableHelper.fill(matrices, screenInnerX,screenInnerY,screenX + innerWidth,screenY + innerHeight,0xFFcdc6d8);
        ArrayList<ItemStack> fishes = getFishFromInventory();
        int index = 0;
        boolean endOfFish = false;
        for(int i = 0; i < 9; i++) {
            int fishY = screenInnerY + 20 * i + 20;
            for (int j = 0; j < 9; j++)  {
                index = j + i * 9;
                endOfFish = fishes.size() == index;
                if(endOfFish) {
                    break;
                }
                int fishX = screenInnerX + 20 * j + 20;
                itemRenderer.renderGuiItemIcon(fishes.get(index), fishX, fishY);
            }
            if(endOfFish) {
                break;
            }
        }
    }


    public ArrayList<ItemStack> getFishFromInventory(){
        ArrayList<ItemStack> result = new ArrayList<>();
        ClientPlayerEntity player  = MinecraftClient.getInstance().player;
        if (player != null) {
            player.getInventory().main.forEach(itemStack -> {
                if (itemStack.getItem().asItem().equals(Items.TROPICAL_FISH)) {
                    result.add(itemStack);
                }
            });
        }

        return result;
    }

    @Override
    public boolean shouldPause() {
        return false;
    }


    public static void openScreen(PlayerEntity player){
        if(player.world != null && !player.world.isClient) {
            player.openHandledScreen(new NamedScreenHandlerFactory() {

                @Override
                public Text getDisplayName() {
                    return Text.translatable("OK");
                }

                @Override
                public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
                    return new ShopScreenHandler(syncId, inv);
                }
            });
        }
    }
}