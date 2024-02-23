package net.semperidem.fishingclub.client.screen.dialog;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.network.ClientPacketSender;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class DialogScreen extends Screen {
    Identifier TEXTURE = new Identifier(FishingClub.MOD_ID, "textures/gui/dialog.png");
    Identifier TEXTURE_FOREGROUND = new Identifier(FishingClub.MOD_ID, "textures/gui/dialog_foreground.png");
    static final String DELIMETER = ";";
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

//THIS RESETS DIALOG IT SHOULD NOT
        Entry firstEntry = new Entry("First;Line2;Line3", "");
        Entry secondEntryA = new Entry("SecondA;SecondA;SecondA", "A");
        Entry secondEntryB = new Entry("SecondB", "B");
        Entry thirdAA = new Entry("thirdAA;thirdAA;thirdAA", "AA");
        Entry thirdAB = new Entry("thirdAB", "[Shop]AB");
        Entry thirdAC = new Entry("thirdAC", "AC");
        Entry thirdBA = new Entry("thirdBA", "BA");
        Entry thirdBB = new Entry("thirdBB", "BB");
        Entry thirdBC = new Entry("thirdBC", "BC");
        Entry forth = new Entry("forth;forth", "AAA");
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
        initialChoiceY = y + 148;
        DialogChoiceWidget dummyChoice = new DialogChoiceWidget(x + 45, initialChoiceY, null);
        dummyChoice.init(firstEntry);
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

    @Override
    public boolean keyPressed(int i, int j, int k) {
        if (i == InputUtil.GLFW_KEY_SPACE) {
            dialogBox.textSpeed = 10;
            return true;
        } else if (i > InputUtil.GLFW_KEY_0 && i <= InputUtil.GLFW_KEY_0 + dialogChoiceWidgets.size()) {
            dialogChoiceWidgets.get(i - InputUtil.GLFW_KEY_0 - 1).onClick(0,0);
            return true;
        } else {
            return super.keyPressed(i, j, k);
        }
    }

    public void endDialog(){
        this.close();
    }

    class Entry extends AlwaysSelectedEntryListWidget.Entry<Entry> {
        String text;
        String chosen;
        String query;
        ArrayList<Entry> choices = new ArrayList<>();
        int id;
        static int lastId = 0;
        int textEndTime;

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
            this.textEndTime = getTextEndTime();
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

        int getTextEndTime(){
            int result = 0;
            for(int i = 0; i < text.length(); i++){
                char c = text.charAt(i);
                result += pauseMap.getOrDefault(c, cDuration);
            }

            return result;
        }

        private void addChoice(Entry child){
            choices.add(child);
        }

        @Override
        public Text getNarration() {
            return Text.of(text);
        }

        private boolean isItemWithinBox(int itemY){
            return itemY > dialogBox.getTop() - dialogBox.lineHeight && itemY < dialogBox.getBottom() + dialogBox.lineHeight;
        }

        private boolean isLastInBox(){
            return this.id == dialogBox.lastDialog.id;
        }

        private String getText(){
            return isLastInBox() ? getTextForTime(dialogBox.textTick) : text;
        }

        private void renderTextLines(MatrixStack matrices, int x, int y){
            String[] lines = getText().split(DELIMETER);
            int offset = 0;
            boolean firstLine = true;
            for(String line : lines){
                if (firstLine) {
                    line = "Derek: " + line;
                    firstLine = false;
                }
                int itemY = y + 1 + offset;
                if (isItemWithinBox(itemY)) {
                    textRenderer.drawWithShadow(matrices, line, dialogBox.getLeft() + 1 ,itemY, 0xFFFFFF);
                }
                offset += dialogBox.lineHeight;
            }
        }

        private void renderTextFooter(MatrixStack matrices,int x, int y, int itemHeight){
            int itemY = y + itemHeight;
            if(isTypedOut() && isItemWithinBox(itemY)) {
                fill(matrices, x,itemY,x + dialogBox.getRowWidth(),itemY - 1, Color.WHITE.getRGB());
            }
        }
        
        private boolean isTypedOut(){
            return !isLastInBox() ||  dialogBox.textTick > this.textEndTime;
        }

        private void renderResponse(MatrixStack matrices, int x, int y, int index, int itemHeight){
            int itemY = y + itemHeight - dialogBox.lineHeight + 1;
            if(!isLastInBox() && isItemWithinBox(itemY)) {
                fill(matrices, x,itemY - 1,x + dialogBox.getRowWidth(),itemY - 2, Color.WHITE.getRGB());
                String respText = MinecraftClient.getInstance().player.getName().getString()+ ": " + dialogBox.getDialogEntry(index + 1).query;
                textRenderer.drawWithShadow(matrices, respText, dialogBox.getLeft() + 1 ,itemY, Color.YELLOW.getRGB());
            }
        }

        @Override
            public void render(MatrixStack matrices, int index, int y, int x, int itemWidth, int itemHeight, int mouseX, int mouseY, boolean isSelected, float tickDelta) {
            renderTextLines(matrices, x,y);
            renderTextFooter(matrices, x,y, itemHeight);
            renderResponse(matrices, x,y, index, itemHeight);
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
            return getEntry(i).text.split(DELIMETER).length * lineHeight + respHeight;
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
                if (entry.id == lastDialog.id) {
                    entry.chosen = chosenText;
                    clearChoices();
                }
            });
        }
    }


    class DialogChoiceWidget extends ButtonWidget {

        private static final int optionHeight = 10;
        private static final int optionWidth = 92;
        private static int lastIndex = 0;
        DialogScreen.Entry option;
        int index;

        public DialogChoiceWidget(int x, int y, PressAction onPress) {
            super(x, y, optionWidth, optionHeight, Text.empty(), onPress);
            this.index = lastIndex++;
            dialogBox.textSpeed = 1;
        }

        public void init(DialogScreen.Entry initialEntry){
            this.option = initialEntry;
            this.populateChoices();
        }

        public DialogChoiceWidget getDefaultChoice(int x, int y,  DialogScreen.Entry option){
            DialogChoiceWidget choice = new DialogChoiceWidget(x,y, buttonWidget -> {
                if (this.option == null) return;
                if (!isActive()) return;
                this.option = option;
                onChoiceClick();
            });
            choice.setMessage(Text.of(option.query));
            return choice;
        }

        public DialogChoiceWidget getExitChoice(int x, int y){
            DialogChoiceWidget choice = new DialogChoiceWidget(x,y, buttonWidget -> {
                if (!isActive()) return;
                onExitClick();
            });
            choice.setMessage(Text.of("[Exit]"));
            return choice;
        }

        public DialogChoiceWidget getShopChoice(int x, int y, DialogScreen.Entry option){
            DialogChoiceWidget choice = new DialogChoiceWidget(x,y, buttonWidget -> {
                if (!isActive()) return;
                onShopClick();
            });
            choice.setMessage(Text.of(option.query));
            return choice;
        }

        private void onChoiceClick(){
            dialogBox.setChosen(option.query);
            dialogBox.lastDialog = option;
            dialogBox.addMessage(option);
            populateChoices();
        }
        private void onExitClick(){
            endDialog();
        }
        private void onShopClick(){
            ClientPacketSender.sendOpenSellShopRequest();
        }

        private void populateChoices(){
            lastIndex = 0;
            addExitChoice();
            for(int i = 0; i < option.choices.size(); i++) {
                DialogScreen.Entry choiceEntry = option.choices.get(i);
                DialogChoiceWidget choice = getChoice(choiceEntry, i);
                dialogChoiceWidgets.add(choice);
                addDrawableChild(choice);
            }
        }

        private DialogChoiceWidget getChoice(DialogScreen.Entry choiceEntry, int i){
            return choiceEntry.query.startsWith("[Shop]") ?
                    getShopChoice(x, initialChoiceY + i * optionHeight, choiceEntry) :
                    getDefaultChoice(x,initialChoiceY + i * optionHeight, choiceEntry);
        }

        private void addExitChoice(){
            if (option.choices.size() == 0) {
                DialogChoiceWidget exitChoice = getExitChoice(x, initialChoiceY);
                dialogChoiceWidgets.add(exitChoice);
                addDrawableChild(exitChoice);
            }
        }



        private boolean isActive(){
            return dialogBox.lastDialog.isTypedOut();
        }

        @Override
        public void render(MatrixStack matrixStack, int i, int j, float f) {
            if (!isActive()) return;
            fill(matrixStack, this.x,this.y + this.height - 1,this.x + this.width,this.y + this.height, Color.WHITE.getRGB());
            textRenderer.drawWithShadow(matrixStack, (index + 1) + ") " + getMessage().getString(), this.x, this.y + 1, Color.WHITE.getRGB());
        }
    }
}
