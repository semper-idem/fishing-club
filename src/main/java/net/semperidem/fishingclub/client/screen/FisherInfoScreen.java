package net.semperidem.fishingclub.client.screen;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.semperidem.fishingclub.fish.fisher.FisherInfo;
import net.semperidem.fishingclub.fish.fisher.FisherInfos;
import net.semperidem.fishingclub.network.ClientPacketSender;

public class FisherInfoScreen extends Screen {
    int screenInnerHeight = 300;
    int screenInnerWidth = 200;
    FisherInfo clientInfo;

    public FisherInfoScreen(Text text) {
        super(text);
        ClientPacketSender.sendFishingInfoDataRequest();
        clientInfo = FisherInfos.getClientInfo();
    }



    @Override
    public void tick() {

    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        int screenX = (this.width - screenInnerWidth) / 2;
        int screenY = (this.height - screenInnerHeight) / 2;
        String clientInfoString = clientInfo.toString();
        String[] clientInfos = clientInfoString.split("\n");
        for(int i = 0; i < clientInfos.length; i++) {
            textRenderer.drawWithShadow(matrices,clientInfos[i], screenX, screenY + 20 * i, 0xae96d3);
        }
        super.render(matrices, mouseX, mouseY, delta);
    }




    @Override
    public boolean shouldPause() {
        return false;
    }

}