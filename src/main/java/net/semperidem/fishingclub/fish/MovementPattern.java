package net.semperidem.fishingclub.fish;

import net.semperidem.fishingclub.util.Point;

import java.util.Random;

public class MovementPattern {
    Point[] points;
    Point[] controlPoints;

    MovementPattern(Point[] points){
        this.points = points;
        this.controlPoints = generateControlPoints(points, 300);
    }
    MovementPattern(Point[] points, float randomOffset){
        this.points = points;
        this.controlPoints = generateControlPoints(points, randomOffset);
    }

    MovementPattern(Point[] points, Point[] controlPoints){
        this.points = points;
        this.controlPoints = controlPoints;
    }



    public Point[] generateControlPoints(Point[] points, float randomOffset) {
        int pointCount = points.length;
        Point[] controlPoints = new Point[pointCount - 1];
        Random random = new Random();

        for (int i = 0; i < pointCount - 1; i++) {
            Point currentPoint = points[i];
            Point nextPoint = points[i + 1];
            float midX = (currentPoint.x + nextPoint.x) / 2;
            float midY = (currentPoint.y + nextPoint.y) / 2;

            // Add a random offset to the y-coordinate
            float offsetY = random.nextFloat() * randomOffset - randomOffset / 2;
            controlPoints[i] = new Point(midX, midY + offsetY);
        }

        return controlPoints;
    }

    public Point[] getRandomizedPoints(int fishLevel){
        return getRandomizedPoints(points, fishLevel);
    }
    public Point[] getRandomizedControlPoints(int fishLevel){
        return getRandomizedPoints(controlPoints, fishLevel/2);
    }

    private Point[] getRandomizedPoints(Point[] points, int fishLevel){
        Point[] result = new Point[points.length];
        for(int i = 0; i < points.length; i++) {
            float x = points[i].x;
            if (x != 0) {
                float xMaxRandomness = fishLevel * 0.3f;
                x = (float) (x - (xMaxRandomness / 2) + (xMaxRandomness * Math.random()));
            }

            float yMaxRandomness = fishLevel;
            float y = points[i].y;
            y = (float) (y - (yMaxRandomness / 2) + (yMaxRandomness * Math.random()));
            float clampedY = Math.max(0, Math.min(1000, y));
            result[i] = new Point(x, clampedY);
        }
        return result;
    }
}
