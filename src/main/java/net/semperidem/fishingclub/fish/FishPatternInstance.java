package net.semperidem.fishingclub.fish;

import net.semperidem.fishingclub.util.Point;

import java.util.ArrayList;

public class FishPatternInstance {

    private final Point[] points;
    private final Point[] controlPoints;
    private final float[] jumpTicks;
    private final int length;
    private int nextJumpTimestampIndex;
    private ArrayList<Segment> segmentList;

    public FishPatternInstance(FishPattern fishPattern, int fishLevel){
        points = fishPattern.getRandomizedPoints(fishLevel);
        controlPoints = fishPattern.getRandomizedControlPoints(fishLevel);
        length = (int) points[points.length - 1].x;
        jumpTicks = getJumpTicks(fishLevel);
        nextJumpTimestampIndex = jumpTicks.length == 0 ? -1 : 0;
        segmentList = new ArrayList<>();
        for(int i = 0; i < points.length; i++) {
            segmentList.add(new Segment(
               points[i],
               i + 1 == points.length ? points[0] : points[i + 1],
               controlPoints[i]
            ));
        }
    }

    private float[] getJumpTicks(int fishLevel){
        int jumpCount = getJumpCount(fishLevel);
        float [] jumpTimestamps = new float[jumpCount];
        int segmentTime = (length / jumpCount);
        for(int i = 0; i < jumpCount; i++){
            jumpTimestamps[i] = (int) (segmentTime * Math.random() + segmentTime * i);
        }
        return jumpTimestamps;
    }

    private int getJumpCount(int fishLevel) {
        return fishLevel < 10 ? 0 : (int) Math.max(1, fishLevel / 25f * Math.random());
    }

    public boolean isJumpTick(int tick){
        if (nextJumpTimestampIndex >= jumpTicks.length) return false;
        if (tick != jumpTicks[nextJumpTimestampIndex]) return false;
        nextJumpTimestampIndex++;
        return true;
    }

    public ArrayList<Segment> getSegmentList() {
        return segmentList;
    }

    public Point[] getPoints() {
        return points;
    }

    public Point[] getControlPoints() {
        return controlPoints;
    }

    public int getLength() {
        return length;
    }

    public static class Segment {
        public final Point currentPoint;
        public final Point nextPoint;
        public final Point controlPoint;

        public Segment(Point currentPoint, Point nextPoint, Point controlPoint) {
            this.currentPoint = currentPoint;
            this.nextPoint = nextPoint;
            this.controlPoint = controlPoint;
        }
    }
}
