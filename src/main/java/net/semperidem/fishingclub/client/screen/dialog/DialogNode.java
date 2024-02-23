package net.semperidem.fishingclub.client.screen.dialog;

import java.util.ArrayList;
import java.util.List;

public class DialogNode {
    String title;
    String response;
    List<DialogNode> questions;

    public DialogNode(String title, String response) {
        this.questions = new ArrayList<>();
        this.response = response;
        this.title = title;
    }

    public DialogNode(String response) {
        this("", response);
    }

    public void chain(DialogNode question) {
        questions.add(question);
    }

    public List<DialogNode> getQuestions() {
        return questions;
    }
}
