package net.semperidem.fishingclub.util;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

public class VelocityUtil {

    public static void addVelocity(Entity entity, Vec3d velocity) {
        entity.addVelocity(velocity.x, velocity.y, velocity.z);
    }

    public static void addVelocityX(Entity entity, double x) {
        entity.addVelocity(x, 0, 0);
    }

    public static void addVelocityY(Entity entity, double y) {
        entity.addVelocity(0, y, 0);
    }

    public static void addVelocityZ(Entity entity, double z) {
        entity.addVelocity(0, 0, z);
    }

    public static void multiplyVelocity(Entity entity, Vec3d multiplier) {
        entity.setVelocity(entity.getVelocity().multiply(multiplier));
    }

    public static void multiplyVelocity(Entity entity, double multiplier) {
        entity.setVelocity(entity.getVelocity().multiply(multiplier));
    }
}
