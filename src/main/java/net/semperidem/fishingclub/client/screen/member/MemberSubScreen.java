package net.semperidem.fishingclub.client.screen.member;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.text.Text;

import java.util.ArrayList;

public class MemberSubScreen implements IMemberSubScreen{
    final MemberScreen parent;
    final TextRenderer textRenderer;
    final ItemRenderer itemRenderer;
    final ArrayList<Drawable> components;
    final Text title;

    public MemberSubScreen(MemberScreen parent, Text title) {
        this.parent = parent;
        this.title = title;
        this.textRenderer = parent.getTextRenderer();
        this.itemRenderer = parent.getItemRenderer();
        this.components = new ArrayList<>();

    }

    @Override
    public int unlockLevel() {
        return 0;
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
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        for (Drawable drawable : components) {
            drawable.render(context, mouseX, mouseY, delta);
        }
    }
}
