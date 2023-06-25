package net.semperidem.fishingclub.client.screen.dialog;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.semperidem.fishingclub.FishingClub;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class DialogScreen extends Screen {
    Identifier TEXTURE = new Identifier(FishingClub.MOD_ID, "textures/gui/dialog.png");
    Identifier TEXTURE_FOREGROUND = new Identifier(FishingClub.MOD_ID, "textures/gui/dialog_foreground.png");
    int backgroundWidth = 200;
    int backgroundHeight = 200;
    int x;
    int y;
    DialogBoxWidget dialogBox;
    ArrayList<DialogChoiceWidget> dialogChoiceWidgets = new ArrayList<>();
    int initialChoiceY;

    public DialogScreen(Text text) {
        super(text);
        this.client = MinecraftClient.getInstance();
    }

    @Override
    protected void init() {
        super.init();
        this.x = (this.width - this.backgroundWidth) / 2;
        this.y = (this.height - this.backgroundHeight) / 2;

        this.dialogBox = new DialogBoxWidget(client, 184,115,x + 8,y + 18);
        this.addDrawableChild(dialogBox);


        Entry firstEntry = new Entry("First\nLine2\nLine3", "");
        Entry secondEntryA = new Entry("SecondA\nSecondA\nSecondA", "A");
        Entry secondEntryB = new Entry("SecondB", "B");
        Entry thirdAA = new Entry("thirdAA\nthirdAA\nthirdAA", "AA");
        Entry thirdAB = new Entry("thirdAB", "AB");
        Entry thirdAC = new Entry("thirdAC", "AC");
        Entry thirdBA = new Entry("thirdBA", "BA");
        Entry thirdBB = new Entry("thirdBB", "BB");
        Entry thirdBC = new Entry("thirdBC", "BC");
        Entry forth = new Entry("forth\nforth", "AAA");
        Entry fifth = new Entry("fifth", "AAAA");
        firstEntry.addChoice(secondEntryA);
        firstEntry.addChoice(secondEntryB);
        secondEntryA.addChoice(thirdAA);
        secondEntryA.addChoice(thirdAB);
        secondEntryA.addChoice(thirdAC);
        secondEntryB.addChoice(thirdBA);
        secondEntryB.addChoice(thirdBB);
        secondEntryB.addChoice(thirdBC);
        thirdAA.addChoice(forth);
        forth.addChoice(fifth);
        //WOW T_T

        dialogBox.addMessage(firstEntry);
        dialogBox.lastDialog = firstEntry;
        int offset = 0;
        initialChoiceY = y + 148;
        for(Entry choice : firstEntry.choices) {
            DialogChoiceWidget dialogChoiceWidget = new DialogChoiceWidget(x + 45, initialChoiceY + offset,92,10, choice);
            dialogChoiceWidgets.add(dialogChoiceWidget);
            this.addDrawableChild(dialogChoiceWidget);
            offset += 10;
        }
    }

    public void clearChoices(){
        dialogChoiceWidgets.forEach(this::remove);
        dialogChoiceWidgets.clear();
    }
    @Override
    public void renderBackground(MatrixStack matrixStack) {
        super.renderBackground(matrixStack, 0);
        setTexture(TEXTURE);
        drawTexture(matrixStack, x, y, 0, 0, backgroundWidth, backgroundHeight, 256, 256);
    }

    public void renderForeground(MatrixStack matrixStack) {
        setTexture(TEXTURE_FOREGROUND);
        drawTexture(matrixStack, x, y, 0, 0, backgroundWidth, backgroundHeight, 256, 256);
    }

    @Override
    public void render(MatrixStack matrixStack, int i, int j, float f) {
        renderBackground(matrixStack);
        super.render(matrixStack, i, j, f);
        renderForeground(matrixStack);
        dialogBox.tick();

    }

    private void setTexture(Identifier texture){
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, texture);

    }

    class Entry extends AlwaysSelectedEntryListWidget.Entry<Entry> {
        String text;
        String chosen;
        String query;
        ArrayList<Entry> choices = new ArrayList<>();
        int id;
        static int lastId = 0;

        final static int cDuration = 10;
        final static HashMap<Character, Integer> pauseMap = new HashMap<>() {{
            put('.', 10);
            put(',', 5);
            put('?', 10);
            put('!', 10);
        }};

        Entry(String text, String query){
            this.id = lastId++;
            this.text = text;
            this.query = query;

        }


        String getTextForTime(int time){
            int timeLeft = time;
            StringBuilder result  = new StringBuilder();
            for(int i = 0; i < text.length(); i++){
                if (timeLeft < 0) break;
                char c = text.charAt(i);
                result.append(c);
                timeLeft -= pauseMap.getOrDefault(c, cDuration);
            }

            return result.toString();
         }

        void addChoice(Entry child){
            choices.add(child);
        }

        @Override
        public Text getNarration() {
            return Text.of(text);
        }

        @Override
            public void render(MatrixStack matrices, int index, int y, int x, int itemWidth, int itemHeight, int mouseX, int mouseY, boolean isSelected, float tickDelta) {
//            if (dialogBox.lastDialog == null) {
//                if (this.id == dialogBox.lastDialog.id) {
//                    //SLOW TYPE
//                } else {
//                }
//            }
            boolean isLastDialog = dialogBox.lastDialog != null && dialogBox.lastDialog.id == id;
            String textForTime = isLastDialog ? getTextForTime(dialogBox.textTick) : text;
            textForTime = textForTime.charAt(textForTime.length() -1) == '\\' ? textForTime.substring(0, textForTime.length() - 2) : textForTime;
            String[] lines = textForTime.split("\n");
            int offset = 0;
            boolean firstLine = true;
            for(String line : lines){
                if (firstLine) {
                    line = "Derek: " + line;
                }
                firstLine = false;
                int itemY = y + 1 + offset;
                if (itemY > dialogBox.getTop() - 14 && itemY < dialogBox.getBottom() +14) {
                    textRenderer.drawWithShadow(matrices, line, dialogBox.getLeft() + 1 ,itemY, 0xFFFFFF);
                }
                offset += dialogBox.lineHeight;

            }
            int itemY = y + itemHeight;
            if (itemY > dialogBox.getTop() - 14 && itemY < dialogBox.getBottom() + 14) {
                if (textForTime.equals(text)) {
                    fill(matrices, x,itemY,x + dialogBox.getRowWidth(),y - 1 + itemHeight, Color.WHITE.getRGB());
                }
            }

            if (dialogBox.lastDialog != null) {
                if (dialogBox.lastDialog.id != id){
                    itemY = y + itemHeight - dialogBox.lineHeight + 1;
                    if (itemY > dialogBox.getTop() - 9 && itemY < dialogBox.getBottom() + 9) {
                        fill(matrices, x,itemY - 1,x + dialogBox.getRowWidth(),itemY - 2, Color.WHITE.getRGB());
                        String respText = MinecraftClient.getInstance().player.getName().getString()+ ": " + dialogBox.getDialogEntry(index + 1).query;
                        textRenderer.drawWithShadow(matrices, respText, dialogBox.getLeft() + 1 ,itemY, Color.YELLOW.getRGB());
                    }
                }
            }
        }
    }
    class DialogBoxWidget extends AlwaysSelectedEntryListWidget<Entry> {
        int textSpeed = 1;
        int textTick = 0;
        DialogScreen.Entry lastDialog;
        int lineHeight = textRenderer.fontHeight + 2;

        public DialogBoxWidget(MinecraftClient client, int width, int height, int x, int y) {
            super(client, width, height, y, y + height, textRenderer.fontHeight + 4);
            this.top = y;
            this.bottom = y + height;
            this.left = x;
            this.right = x + width;

            setRenderBackground(false);
            setRenderHeader(false, 0);
            setRenderSelection(false);
            setRenderHorizontalShadows(false);
        }

        @Override
        public void render(MatrixStack matrixStack, int i, int j, float f) {
            super.render(matrixStack, i, j, f);
        }

        @Override
        public int getMaxScroll() {
            return Math.max(0, this.getMaxPosition() - (this.bottom - this.top));
        }



        public DialogScreen.Entry getDialogEntry(int i){
            return getEntry(i);
        }
        @Override
        protected int getScrollbarPositionX() {
            return this.right - 4;
        }


        public int getRowLeft() {
            return this.left;
        }

        @Override
        protected int getRowTop(int i) {
            int result = 0;
            for(int j = 0; j <= i; j++) {
                result += getRowHeight(j);
            }
            return this.top - (int)this.getScrollAmount() + result - getRowHeight(i);
        }

        private int getRowBottom(int i) {
            return this.getRowTop(i) + getRowHeight(i);
        }

        private int getRowHeight(int i){
            int respHeight = dialogBox.lastDialog != null && dialogBox.lastDialog.id != getEntry(i).id ? lineHeight : 0;
            return getEntry(i).text.split("\n").length * lineHeight + respHeight;
        }

        @Override
        protected int getMaxPosition() {
            int itemTotalHeight = 0;
            for(int entryIndex = 0; entryIndex < this.getEntryCount(); entryIndex++) {
                itemTotalHeight += getRowHeight(entryIndex);
            }
            return itemTotalHeight;
        }

        @Override
        protected void renderList(MatrixStack matrixStack, int y, int x, float tickDelta) {
            int rowLeft = this.getRowLeft();
            int rowWidth = this.getRowWidth();
            int entryCount = this.getEntryCount();

            for(int entryIndex = 0; entryIndex < entryCount; ++entryIndex) {
                int rowTop = this.getRowTop(entryIndex);
                int rowBottom = this.getRowBottom(entryIndex);
                int rowHeight = this.getRowHeight(entryIndex);
                if (rowBottom >= this.top && rowTop <= this.bottom) {
                    this.renderEntry(matrixStack, y, x, tickDelta, entryIndex, rowLeft, rowTop, rowWidth, rowHeight);
                }
            }

        }

        @Override
        public int getRowWidth() {
            return width;
        }

        public int getLeft(){
            return left;
        }

        public int getTop(){
            return top;
        }

        public int getBottom(){
            return bottom;
        }

        public void tick(){
            textTick+= textSpeed;
        }


        void addMessage(DialogScreen.Entry entry) {
            textTick = 0;
            addEntry(entry);
            mouseScrolled(0,0, -10);
        }

        void setChosen(String chosenText){
            this.children().forEach(entry -> {
                if (lastDialog == null || entry.id == lastDialog.id) {
                    entry.chosen = chosenText;
                    clearChoices();
                }
            });
        }
        //TODO SPACEBAR SPEEDS UP TEXT
        //KEY CONTROL CHOICE
    }


    class DialogChoiceWidget extends ButtonWidget {
        public DialogChoiceWidget(int x, int y, int width, int height, DialogScreen.Entry option) {
            super(x, y, width, height, Text.empty(), buttonWidget -> {
                dialogBox.setChosen(option.query);
                dialogBox.lastDialog = option;
                dialogBox.addMessage(option);
                int offset = 0;
                for(DialogScreen.Entry entry : option.choices) {
                    dialogChoiceWidgets.add(new DialogChoiceWidget(x, initialChoiceY + offset, width, height, entry));
                    offset += height;
                }
                for(DialogChoiceWidget dialogChoiceWidget : dialogChoiceWidgets) {
                    addDrawableChild(dialogChoiceWidget);
                }
            });
            this.setMessage(Text.of(option.query));

        }

        @Override
        public void render(MatrixStack matrixStack, int i, int j, float f) {
            fill(matrixStack, this.x,this.y + this.height - 1,this.x + this.width,this.y + this.height, Color.WHITE.getRGB());
            textRenderer.drawWithShadow(matrixStack, getMessage(), this.x, this.y + 1, Color.WHITE.getRGB());
        }
    }
}
