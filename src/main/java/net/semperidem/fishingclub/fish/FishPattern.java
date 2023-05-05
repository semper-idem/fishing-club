package net.semperidem.fishingclub.fish;

import net.semperidem.fishingclub.util.Point;

import java.util.Random;

public class FishPattern {
    Point[] points;
    Point[] controlPoints;

    FishPattern(Point[] points){
        this.points = points;
        this.controlPoints = generateControlPoints(points, 300);
    }
    FishPattern(Point[] points, float randomOffset){
        this.points = points;
        this.controlPoints = generateControlPoints(points, randomOffset);
    }

    FishPattern(Point[] points, Point[] controlPoints){
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
}
