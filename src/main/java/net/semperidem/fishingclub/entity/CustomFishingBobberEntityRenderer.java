package net.semperidem.fishingclub.entity;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.FishingBobberEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.item.FishingRodPartItem;

public class CustomFishingBobberEntityRenderer extends FishingBobberEntityRenderer {
    private static final Identifier DEFAULT = new Identifier(FishingClub.MOD_ID, "textures/entity/bobber.png");
    private static final Identifier ANCIENT = new Identifier(FishingClub.MOD_ID, "textures/entity/bobber_ancient.png");
    private static final Identifier PLANT = new Identifier(FishingClub.MOD_ID, "textures/entity/bobber_plant.png");
    private static final Identifier WOODEN = new Identifier(FishingClub.MOD_ID, "textures/entity/bobber_wooden.png");
    private static final RenderLayer LAYER = RenderLayer.getEntityCutout(DEFAULT);

    public CustomFishingBobberEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    public void render(FishingBobberEntity fishingBobberEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        double s;
        float r;
        double q;
        double p;
        double o;
        PlayerEntity playerEntity = fishingBobberEntity.getPlayerOwner();
        if (playerEntity == null) {
            return;
        }
        matrixStack.push();
        matrixStack.push();
        matrixStack.scale(0.5f, 0.5f, 0.5f);
        matrixStack.multiply(this.dispatcher.getRotation());
        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0f));
        MatrixStack.Entry entry = matrixStack.peek();
        Matrix4f matrix4f = entry.getPositionMatrix();
        Matrix3f matrix3f = entry.getNormalMatrix();
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(LAYER);
        vertex(vertexConsumer, matrix4f, matrix3f, i, 0.0f, 0, 0, 1);
        vertex(vertexConsumer, matrix4f, matrix3f, i, 1.0f, 0, 1, 1);
        vertex(vertexConsumer, matrix4f, matrix3f, i, 1.0f, 1, 1, 0);
        vertex(vertexConsumer, matrix4f, matrix3f, i, 0.0f, 1, 0, 0);
        matrixStack.pop();
        int j = playerEntity.getMainArm() == Arm.RIGHT ? 1 : -1;
        ItemStack itemStack = playerEntity.getMainHandStack();
        if (!itemStack.isOf(FishingClub.CUSTOM_FISHING_ROD)) {
            j = -j;
        }
        float h = playerEntity.getHandSwingProgress(g);
        float k = MathHelper.sin(MathHelper.sqrt(h) * (float)Math.PI);
        float l = MathHelper.lerp(g, playerEntity.prevBodyYaw, playerEntity.bodyYaw) * ((float)Math.PI / 180);
        double d = MathHelper.sin(l);
        double e = MathHelper.cos(l);
        double m = (double)j * 0.35;
        double n = 0.8;
        if (this.dispatcher.gameOptions != null && !this.dispatcher.gameOptions.getPerspective().isFirstPerson() || playerEntity != MinecraftClient.getInstance().player) {
            o = MathHelper.lerp((double)g, playerEntity.prevX, playerEntity.getX()) - e * m - d * 0.8;
            p = playerEntity.prevY + (double)playerEntity.getStandingEyeHeight() + (playerEntity.getY() - playerEntity.prevY) * (double)g - 0.45;
            q = MathHelper.lerp((double)g, playerEntity.prevZ, playerEntity.getZ()) - d * m + e * 0.8;
            r = playerEntity.isInSneakingPose() ? -0.1875f : 0.0f;
        } else {
            s = 960.0 / (double)this.dispatcher.gameOptions.getFov().getValue().intValue();
            Vec3d vec3d = this.dispatcher.camera.getProjection().getPosition((float)j * 0.525f, -0.1f);
            vec3d = vec3d.multiply(s);
            vec3d = vec3d.rotateY(k * 0.5f);
            vec3d = vec3d.rotateX(-k * 0.7f);
            o = MathHelper.lerp((double)g, playerEntity.prevX, playerEntity.getX()) + vec3d.x;
            p = MathHelper.lerp((double)g, playerEntity.prevY, playerEntity.getY()) + vec3d.y;
            q = MathHelper.lerp((double)g, playerEntity.prevZ, playerEntity.getZ()) + vec3d.z;
            r = playerEntity.getStandingEyeHeight();
        }
        s = MathHelper.lerp((double)g, fishingBobberEntity.prevX, fishingBobberEntity.getX());
        double t = MathHelper.lerp((double)g, fishingBobberEntity.prevY, fishingBobberEntity.getY()) + 0.25;
        double u = MathHelper.lerp((double)g, fishingBobberEntity.prevZ, fishingBobberEntity.getZ());
        float v = (float)(o - s);
        float w = (float)(p - t) + r;
        float x = (float)(q - u);
        VertexConsumer vertexConsumer2 = vertexConsumerProvider.getBuffer(RenderLayer.getLineStrip());
        MatrixStack.Entry entry2 = matrixStack.peek();
        int y = 16;
        for (int z = 0; z <= 16; ++z) {
            renderFishingLine(v, w, x, vertexConsumer2, entry2, percentage(z, 16), percentage(z + 1, 16));
        }
        matrixStack.pop();
       // super.render(fishingBobberEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    private static float percentage(int i, int j) {
        return (float)i / (float)j;
    }

    private static void vertex(VertexConsumer vertexConsumer, Matrix4f matrix4f, Matrix3f matrix3f, int i, float f, int j, int k, int l) {
        vertexConsumer.vertex(matrix4f, f - 0.5f, (float)j - 0.5f, 0.0f).color(255, 255, 255, 255).texture(k, l).overlay(OverlayTexture.DEFAULT_UV).light(i).normal(matrix3f, 0.0f, 1.0f, 0.0f).next();
    }

    private static void renderFishingLine(float f, float g, float h, VertexConsumer vertexConsumer, MatrixStack.Entry entry, float i, float j) {
        float k = f * i;
        float l = g * (i * i + i) * 0.5f + 0.25f;
        float m = h * i;
        float n = f * j - k;
        float o = g * (j * j + j) * 0.5f + 0.25f - l;
        float p = h * j - m;
        float q = MathHelper.sqrt(n * n + o * o + p * p);
        vertexConsumer.vertex(entry.getPositionMatrix(), k, l, m).color(0, 0, 0, 255).normal(entry.getNormalMatrix(), n /= q, o /= q, p /= q).next();
    }

    @Override
    public Identifier getTexture(FishingBobberEntity fishingBobberEntity) {
        if ( fishingBobberEntity instanceof CustomFishingBobberEntity) {
            FishingRodPartItem bobber = ((CustomFishingBobberEntity) fishingBobberEntity).getBobber();
            switch (bobber.getKey()) {
                case "BOBBER_WOODEN":
                    return WOODEN;
                case "BOBBER_PLANT_BASED":
                    return PLANT;
                case "BOBBER_ANCIENT":
                    return ANCIENT;
            }
        }
        return DEFAULT;
    }
}
