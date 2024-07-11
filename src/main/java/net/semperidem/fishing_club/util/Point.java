package net.semperidem.fishing_club.util;

public class Point {
    public final float x;
    public final float y;

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }
    public Point(String input){
        String[] inputArray = input.split(",");
        this.x = Float.parseFloat(inputArray[0]);
        this.y = Float.parseFloat(inputArray[1]);
    }

    public String toString(){
        return x + "," + y;
    }
}
