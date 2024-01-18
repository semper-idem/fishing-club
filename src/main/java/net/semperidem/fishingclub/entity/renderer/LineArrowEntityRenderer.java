package net.semperidem.fishingclub.entity.renderer;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.ArrowEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.semperidem.fishingclub.entity.LineArrowEntity;

public class LineArrowEntityRenderer extends ProjectileEntityRenderer<LineArrowEntity> {
    private static final RenderLayer LAYER = RenderLayer.getEntityCutout(ArrowEntityRenderer.TEXTURE);

    public LineArrowEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(LineArrowEntity entity) {
        return ArrowEntityRenderer.TEXTURE;
    }

    public void render(LineArrowEntity lineArrowEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        super.render(lineArrowEntity, f, g, matrixStack, vertexConsumerProvider, i);
        double s;
        float r;
        double q;
        double p;
        double o;
        if (lineArrowEntity.getOwner() != null && lineArrowEntity.getOwner() instanceof PlayerEntity playerEntity) {
            matrixStack.push();
            int j = playerEntity.getMainArm() == Arm.RIGHT ? 1 : -1;
            float h = playerEntity.getHandSwingProgress(g);
            float k = MathHelper.sin(MathHelper.sqrt(h) * (float)Math.PI);
            float l = MathHelper.lerp(g, playerEntity.prevBodyYaw, playerEntity.bodyYaw) * ((float)Math.PI / 180);
            double d = MathHelper.sin(l);
            double e = MathHelper.cos(l);
            double m = (double)j * 0.35;
            if (this.dispatcher.gameOptions != null && !this.dispatcher.gameOptions.getPerspective().isFirstPerson() || playerEntity != MinecraftClient.getInstance().player) {
                o = MathHelper.lerp(g, playerEntity.prevX, playerEntity.getX()) - e * m - d * 0.8;
                p = playerEntity.prevY + (double)playerEntity.getStandingEyeHeight() + (playerEntity.getY() - playerEntity.prevY) * (double)g - 0.45;
                q = MathHelper.lerp(g, playerEntity.prevZ, playerEntity.getZ()) - d * m + e * 0.8;
                r = playerEntity.isInSneakingPose() ? -0.1875f : 0.0f;
            } else {
                s = 960.0 / (double)this.dispatcher.gameOptions.getFov().getValue().intValue();
                Vec3d vec3d = this.dispatcher.camera.getProjection().getPosition((float)j * 0.525f, -0.1f);
                vec3d = vec3d.multiply(s);
                vec3d = vec3d.rotateY(k * 0.5f);
                vec3d = vec3d.rotateX(-k * 0.7f);
                o = MathHelper.lerp(g, playerEntity.prevX, playerEntity.getX()) + vec3d.x;
                p = MathHelper.lerp(g, playerEntity.prevY, playerEntity.getY()) + vec3d.y;
                q = MathHelper.lerp(g, playerEntity.prevZ, playerEntity.getZ()) + vec3d.z;
                r = playerEntity.getStandingEyeHeight();
            }
            s = MathHelper.lerp(g, lineArrowEntity.prevX, lineArrowEntity.getX());
            double t = MathHelper.lerp(g, lineArrowEntity.prevY, lineArrowEntity.getY()) + 0.25;
            double u = MathHelper.lerp(g, lineArrowEntity.prevZ, lineArrowEntity.getZ());
            float v = (float)(o - s);
            float w = (float)(p - t) + r;
            float x = (float)(q - u);
            VertexConsumer vertexConsumer2 = vertexConsumerProvider.getBuffer(RenderLayer.getLineStrip());
            MatrixStack.Entry entry2 = matrixStack.peek();
            for (int z = 0; z <= 8; ++z) {
                renderFishingLine(v, w, x, vertexConsumer2, entry2, percentage(z, 8), percentage(z + 1, 8));
            }
            matrixStack.pop();

        }
    }

    private static float percentage(int i, int j) {
        return (float)i / (float)j;
    }

    private static void renderFishingLine(float f, float g, float h, VertexConsumer vertexConsumer, MatrixStack.Entry entry, float i, float j) {
        float k = f * i;
        float l = g * (i * i + i) * 0.5f + 0.25f;
        float m = h * i;
        float n = f * j - k;
        float o = g * (j * j + j) * 0.5f + 0.25f - l;
        float p = h * j - m;
        float q = MathHelper.sqrt(n * n + o * o + p * p);
        vertexConsumer.vertex(entry.getPositionMatrix(), k, l, m).color(111, 111, 111, 111).normal(entry.getNormalMatrix(), n /= q, o /= q, p /= q).next();
    }

}
