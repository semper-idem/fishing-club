package net.semperidem.fishingclub.client.screen.member;

import net.minecraft.client.gui.Drawable;
import net.minecraft.client.util.math.MatrixStack;

import java.util.ArrayList;

public class MemberSubScreen implements IMemberSubScreen{
    ArrayList<Drawable> components = new ArrayList<>();

    @Override
    public void init() {

    }

    @Override
    public ArrayList<Drawable> getComponents() {
        return components;
    }

    @Override
    public void handledScreenTick() {

    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {
        for (Drawable drawable : components) {
            drawable.render(matrixStack, mouseX, mouseY, delta);
        }
    }
}
