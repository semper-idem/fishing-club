package net.semperidem.fishingclub.fish;

import net.semperidem.fishingclub.util.Point;

public class FishPatterns {

    public static Point[] getRandomizedPoints(FishPattern fishPattern, int fishLevel){
        return getRandomizedPoints(fishPattern.points, fishLevel);
    }
    public static Point[] getRandomizedControlPoints(FishPattern fishPattern, int fishLevel){
        return getRandomizedPoints(fishPattern.controlPoints, fishLevel/2);
    }

    private static Point[] getRandomizedPoints(Point[] points, int fishLevel){
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

    /*
    *   X has to start from 0 but can finish w/e
    *   Y has to start and finish 0 and cannot be higher than 1000
    *   Point count can be w/e
    *
    * */
    public static final FishPattern DEFAULT;
    public static final FishPattern EASY1;
    public static final FishPattern EASY2;
    public static final FishPattern EASY3;
    public static final FishPattern EASY4;
    public static final FishPattern EASY5;
    public static final FishPattern MID1;
    public static final FishPattern MID2;
    public static final FishPattern MID3;
    public static final FishPattern MID4;
    public static final FishPattern HARD1;
    public static final FishPattern HARD2;
    public static final FishPattern HARD3;

    static {
        DEFAULT = new FishPattern(
                new Point[]{
                        new Point(0, 0),
                        new Point(50, 600),
                        new Point(100, 200),
                        new Point(150, 600),
                        new Point(200, 0)
                }
        );

        EASY1 = new FishPattern(
                new Point[]{
                        new Point(0, 0),
                        new Point(50, 200),
                        new Point(100, 100),
                        new Point(150, 300),
                        new Point(200, 0)
                }
        );

        EASY2 = new FishPattern(
                new Point[]{
                        new Point(0, 0),
                        new Point(50, 400),
                        new Point(100, 800),
                        new Point(150, 400),
                        new Point(200, 0)
                }
        );
        EASY3 = new FishPattern(
                new Point[]{
                        new Point(0, 0),
                        new Point(50, 200),
                        new Point(100, 0),
                        new Point(150, 200),
                        new Point(200, 0)
                }
        );
        EASY4 = new FishPattern(
                new Point[]{
                        new Point(0, 0),
                        new Point(50, 300),
                        new Point(100, 100),
                        new Point(150, 200),
                        new Point(200, 0)
                }
        );
        EASY5 = new FishPattern(
                new Point[]{
                        new Point(0, 0),
                        new Point(50, 300),
                        new Point(100, 400),
                        new Point(150, 100),
                        new Point(200, 0)
                }
        );

        MID1 = new FishPattern(
                new Point[]{
                        new Point(0, 0),
                        new Point(40, 600),
                        new Point(80, 200),
                        new Point(120, 400),
                        new Point(160, 700),
                        new Point(200, 0)
                }
        );

        MID2 = new FishPattern(
                new Point[]{
                        new Point(0, 0),
                        new Point(40, 600),
                        new Point(80, 200),
                        new Point(120, 100),
                        new Point(160, 200),
                        new Point(200, 0)
                }
        );

        MID3 = new FishPattern(
                new Point[]{
                        new Point(0, 0),
                        new Point(40, 100),
                        new Point(80, 150),
                        new Point(120, 900),
                        new Point(160, 500),
                        new Point(200, 0)
                }
        );

        MID4 = new FishPattern(
                new Point[]{
                        new Point(0, 0),
                        new Point(40, 700),
                        new Point(80, 600),
                        new Point(120, 400),
                        new Point(160, 700),
                        new Point(200, 0)}
        );

        HARD1 = new FishPattern(
                new Point[]{
                        new Point(0, 0),
                        new Point(35, 600),
                        new Point(70, 200),
                        new Point(105, 400),
                        new Point(140, 100),
                        new Point(170, 400),
                        new Point(200, 0)
                }
        );

        HARD2 = new FishPattern(
                new Point[]{
                        new Point(0, 0),
                        new Point(35, 600),
                        new Point(70, 200),
                        new Point(105, 400),
                        new Point(140, 100),
                        new Point(170, 400),
                        new Point(200, 0)
                }
        );

        HARD3 = new FishPattern(
                new Point[]{
                        new Point(0, 0),
                        new Point(35, 100),
                        new Point(70, 150),
                        new Point(105, 900),
                        new Point(140, 100),
                        new Point(170, 900),
                        new Point(200, 0)
                }
        );


    }
}
