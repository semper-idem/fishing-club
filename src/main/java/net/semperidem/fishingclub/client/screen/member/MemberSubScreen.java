package net.semperidem.fishingclub.client.screen.member;

import net.minecraft.client.gui.Drawable;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.ArrayList;

public class MemberSubScreen implements IMemberSubScreen{
    MemberScreen parent;
    ArrayList<Drawable> components = new ArrayList<>();
    Text title = Text.empty();

    public MemberSubScreen(MemberScreen parent, Text title) {
        this.parent = parent;
        this.title = title;
    }

    public MemberScreen getParent() {
        return this.parent;
    }
    @Override
    public void init() {
        components.clear();
    }

    public Text getTitle() {
        return this.title;
    }

    @Override
    public ArrayList<Drawable> getComponents() {
        return components;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {
        for (Drawable drawable : components) {
            drawable.render(matrixStack, mouseX, mouseY, delta);
        }
    }
}
