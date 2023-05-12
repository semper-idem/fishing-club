package net.semperidem.fishingclub.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.semperidem.fishingclub.fish.fisher.FisherInfo;

public class FishingInfoScreen  extends Screen {
    int screenInnerHeight = 300;
    int screenInnerWidth = 200;
    FisherInfo clientSkill;

    public FishingInfoScreen(Text text) {
        super(text);
    }

    private void initClientData(){
        ClientPlayerEntity clientPlayer = MinecraftClient.getInstance().player;
    }


    @Override
    public void tick() {

    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        int screenX = (this.width - screenInnerWidth) / 2;
        int screenY = (this.height - screenInnerHeight) / 2;
        super.render(matrices, mouseX, mouseY, delta);
    }




    @Override
    public boolean shouldPause() {
        return false;
    }

}