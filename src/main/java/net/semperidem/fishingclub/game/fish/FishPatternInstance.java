package net.semperidem.fishingclub.game.fish;

import net.semperidem.fishingclub.util.Point;

public class FishPatternInstance {
    private final Point[] curvePoints;
    private final Point[] curveControlPoints;
    private final float[] jumpTimestamps;
    private final int duration;
    private int nextJumpTimestampIndex;

    public FishPatternInstance(FishPattern fishPattern, int fishLevel){
        curvePoints = fishPattern.getRandomizedPoints(fishLevel);
        curveControlPoints = fishPattern.getRandomizedControlPoints(fishLevel);
        duration = (int) curvePoints[curvePoints.length - 1].x;
        jumpTimestamps = getJumpTimestamps(fishLevel);
        nextJumpTimestampIndex = jumpTimestamps.length == 0 ? -1 : 0;
    }

    private float[] getJumpTimestamps(int fishLevel){
        int jumpCount = getJumpCount(fishLevel);
        float [] jumpTimestamps = new float[jumpCount];
        int segmentTime = (duration / jumpCount);
        for(int i = 0; i < jumpCount; i++){
            jumpTimestamps[i] = (int) (segmentTime * Math.random() + segmentTime * i);
        }
        return jumpTimestamps;
    }

    private int getJumpCount(int fishLevel) {
        return fishLevel < 10 ? 0 : (int) Math.max(1, fishLevel / 25f * Math.random());
    }

    public boolean isJumpTick(int tick){
        if (nextJumpTimestampIndex >= jumpTimestamps.length) return false;
        if (tick != jumpTimestamps[nextJumpTimestampIndex]) return false;
        nextJumpTimestampIndex++;
        return true;
    }
}
