package net.semperidem.fishingclub.screen.dialog;

import java.util.ArrayList;

public class DialogNode {
    public final String title;
    public final String content;
    public final ArrayList<DialogNode> questions;
    public String specialAction;

    public DialogNode(String title, String response) {
        this.questions = new ArrayList<>();
        this.content = response;
        this.title = (!specialAction.isEmpty()) ? specialAction + title : title;
    }

    public void setSpecialAction(String specialAction) {
        this.specialAction = specialAction;
    }

    public DialogNode(String response) {
        this("", response);
    }

    public void chain(DialogNode question) {
        questions.add(question);
    }
}
