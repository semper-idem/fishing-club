package net.semperidem.fishingclub.game.fish;

import net.semperidem.fishingclub.util.Point;

public class FishPatterns {


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
    public static final FishPattern MID5;
    public static final FishPattern MID6;
    public static final FishPattern HARD1;
    public static final FishPattern HARD2;
    public static final FishPattern HARD3;

    static {
        DEFAULT = new FishPattern(
                new Point[]{
                        new Point(0, 500),
                        new Point(50, 700),
                        new Point(100, 500),
                        new Point(150, 300),
                        new Point(200, 500)
                }
        );

        EASY1 = new FishPattern(
                new Point[]{
                        new Point(0, 500),
                        new Point(50, 400),
                        new Point(100, 600),
                        new Point(150, 400),
                        new Point(200, 500)
                }
        );

        EASY2 = new FishPattern(
                new Point[]{
                        new Point(0, 500),
                        new Point(50, 600),
                        new Point(100, 400),
                        new Point(150, 300),
                        new Point(200, 500)
                }
        );
        EASY3 = new FishPattern(
                new Point[]{
                        new Point(0, 500),
                        new Point(50, 600),
                        new Point(100, 500),
                        new Point(150, 400),
                        new Point(200, 500)
                }
        );
        EASY4 = new FishPattern(
                new Point[]{
                        new Point(0, 500),
                        new Point(50, 300),
                        new Point(100, 100),
                        new Point(150, 400),
                        new Point(200, 500)
                }
        );
        EASY5 = new FishPattern(
                new Point[]{
                        new Point(0, 500),
                        new Point(50, 300),
                        new Point(100, 400),
                        new Point(150, 100),
                        new Point(200, 500)
                }
        );

        MID1 = new FishPattern(
                new Point[]{
                        new Point(0, 500),
                        new Point(40, 700),
                        new Point(80, 500),
                        new Point(120, 200),
                        new Point(160, 700),
                        new Point(200, 500)
                }
        );

        MID2 = new FishPattern(
                new Point[]{
                        new Point(0, 500),
                        new Point(40, 700),
                        new Point(80, 300),
                        new Point(120, 100),
                        new Point(160, 300),
                        new Point(200, 500)
                }
        );

        MID3 = new FishPattern(
                new Point[]{
                        new Point(0, 500),
                        new Point(40, 600),
                        new Point(80, 700),
                        new Point(120, 200),
                        new Point(160, 300),
                        new Point(200, 500)
                }
        );

        MID4 = new FishPattern(
                new Point[]{
                        new Point(0, 500),
                        new Point(40, 900),
                        new Point(80, 600),
                        new Point(120, 400),
                        new Point(160, 950),
                        new Point(200, 500)}
        );

        MID5 = new FishPattern(
                new Point[]{
                        new Point(0, 500),
                        new Point(40, 300),
                        new Point(80, 600),
                        new Point(120, 400),
                        new Point(160, 100),
                        new Point(200, 500)}
        );

        MID6 = new FishPattern(
                new Point[]{
                        new Point(0, 500),
                        new Point(40, 900),
                        new Point(80, 200),
                        new Point(120, 900),
                        new Point(160, 700),
                        new Point(200, 500)}
        );

        HARD1 = new FishPattern(
                new Point[]{
                        new Point(0, 500),
                        new Point(35, 600),
                        new Point(70, 200),
                        new Point(105, 400),
                        new Point(140, 100),
                        new Point(170, 400),
                        new Point(200, 500)
                }
        );

        HARD2 = new FishPattern(
                new Point[]{
                        new Point(0, 500),
                        new Point(35, 600),
                        new Point(70, 200),
                        new Point(105, 400),
                        new Point(140, 100),
                        new Point(170, 400),
                        new Point(200, 500)
                }
        );

        HARD3 = new FishPattern(
                new Point[]{
                        new Point(0, 500),
                        new Point(35, 100),
                        new Point(70, 150),
                        new Point(105, 900),
                        new Point(140, 100),
                        new Point(170, 900),
                        new Point(200, 500)
                }
        );


    }
}
