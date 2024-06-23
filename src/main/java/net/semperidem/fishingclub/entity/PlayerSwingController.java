package net.semperidem.fishingclub.entity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

public class PlayerSwingController {

    public static void setPosition(CustomFishingBobberEntity anchorPoint, PlayerEntity player, double targetX, double targetY, double targetZ) {
        Vec3d targetPos = new Vec3d(targetX, targetY, targetZ);
        Vec3d bobberPos = anchorPoint.getPos();
        Vec3d playerPos = player.getPos();

        //Gravity (0, -0.098, 0) applied on player outside this method and is stored in velocity vector
        Vec3d gravity = player.getVelocity();

        Vec3d radiusVec = targetPos.subtract(bobberPos); // the line from the bobber to the player

            // player is "outside" of the swing circle - apply tension force

            // calculate the tension force
            Vec3d direction = radiusVec.normalize();
            double tensionForceValue = gravity.dotProduct(direction);
            Vec3d tension = direction.multiply(tensionForceValue);

            // calculate the net force acting on the player
            Vec3d netForce = tension.add(gravity);

            //Zero Pos, Swing balance
        Vec3d zeroPos = bobberPos.add(0, -anchorPoint.lineLength, 0);
        player.world.addParticle(ParticleTypes.SCULK_CHARGE_POP, zeroPos.x, zeroPos.y,zeroPos.z,0, 0, 0);

        // Acceleration
         Vec3d acc = player.getVelocity().normalize().crossProduct(radiusVec.normalize()).multiply(0.1);
        Vec3d netPos = bobberPos.add(radiusVec.normalize().multiply(anchorPoint.lineLength));
         Vec3d vel = player.getVelocity().add(playerPos.relativize(zeroPos).normalize().add(0, -1, 0).multiply(acc.length()));
        player.sendMessage(Text.literal(String.format("Acceleration: %.2f", acc.length())), true);
            player.world.addParticle(ParticleTypes.ELECTRIC_SPARK, playerPos.x, playerPos.y,playerPos.z, vel.x, vel.y, vel.z);
            playerPos = netPos;
        if (radiusVec.length() > anchorPoint.lineLength) {
            if (vel.y < 0) {
                player.setVelocity(player.getVelocity().add(vel));
            }
            player.setPos(playerPos.x, playerPos.y, playerPos.z);
        } else {
            player.setPos(targetPos.x, targetPos.y, targetPos.z);
        }
        player.setBoundingBox(player.calculateBoundingBox());
    }
}