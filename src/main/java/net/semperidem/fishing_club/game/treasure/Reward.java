package net.semperidem.fishing_club.game.treasure;

import net.minecraft.item.ItemStack;

import java.util.ArrayList;

public class Reward {
    ArrayList<ItemStack> content;
    int grade;

    Reward(ArrayList<ItemStack> content, int grade) {
        this.grade = grade;
        this.content = content;
    }

    public int getGrade(){
        return this.grade;
    }

    public ArrayList<ItemStack> getContent(){
        return this.content;
    }


}
