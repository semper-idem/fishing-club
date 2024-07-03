package net.semperidem.fishingclub.entity.renderer;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.entity.HarpoonEntity;
import net.semperidem.fishingclub.entity.renderer.model.HarpoonEntityModel;
import net.semperidem.fishingclub.registry.FCEntityTypes;

public class HarpoonEntityRenderer extends EntityRenderer<HarpoonEntity> {
    public static final Identifier TEXTURE = FishingClub.getIdentifier("textures/entity/harpoon_rod.png");
    private final HarpoonEntityModel model;
    public HarpoonEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.model = new HarpoonEntityModel(context.getPart(FCEntityTypes.MODEL_HARPOON_LAYER));
    }


    @Override
    public void render(HarpoonEntity harpoonEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
//        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(MathHelper.lerp(g, harpoonEntity.prevYaw, harpoonEntity.getYaw()) - 90.0f));
//        matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(MathHelper.lerp(g, harpoonEntity.prevPitch, harpoonEntity.getPitch()) + 90.0f));
      //  VertexConsumer vertexConsumer = ItemRenderer.getDirectItemGlintConsumer(vertexConsumerProvider, this.model.getLayer(this.getTexture(harpoonEntity)), false, false);
//        this.model.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1.0f, 1.0f, 1.0f, 1.0f);
        matrixStack.pop();

        double s;
        float r;
        double q;
        double p;
        double o;
        if (harpoonEntity.getOwner() != null && harpoonEntity.getOwner() instanceof PlayerEntity playerEntity) {
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
            s = MathHelper.lerp(g, harpoonEntity.prevX, harpoonEntity.getX());
            double t = MathHelper.lerp(g, harpoonEntity.prevY, harpoonEntity.getY()) + 0.25;
            double u = MathHelper.lerp(g, harpoonEntity.prevZ, harpoonEntity.getZ());
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
//        vertexConsumer.vertex(entry.getPositionMatrix(), k, l, m).color(111, 111, 111, 111).normal(entry.getNormalMatrix(), n /= q, o /= q, p /= q).next();
    }

    @Override
    public Identifier getTexture(HarpoonEntity tridentEntity) {
        return TEXTURE;
    }
}
