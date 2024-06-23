package net.semperidem.fishingclub.util;

import net.minecraft.util.math.Vec3d;

public class Util {

    private String getVectorString(Vec3d vector) {
        return String.format("%5.2f", vector.x) + String.format("%5.2f", vector.y) + String.format("%5.2f", vector.z);
    }
}
