package net.semperidem.fishingclub.util;

public class MathUtil {

    public static float quadraticBezier(float t, float p0, float p1, float p2) {
        float oneMinusT = 1 - t;
        return oneMinusT * oneMinusT * p0 + 2 * oneMinusT * t * p1 + t * t * p2;
    }

}
