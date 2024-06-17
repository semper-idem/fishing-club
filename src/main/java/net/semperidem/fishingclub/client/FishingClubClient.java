package net.semperidem.fishingclub.client;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;
import net.semperidem.fishingclub.fisher.perks.FishingPerk;
import net.semperidem.fishingclub.fisher.perks.spells.SpellInstance;
import net.semperidem.fishingclub.item.fishing_rod.components.FishingRodConfiguration;
import net.semperidem.fishingclub.registry.FishingClubRegistry;
import net.semperidem.fishingclub.registry.ItemRegistry;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.UUID;

public class FishingClubClient implements ClientModInitializer {
    public static UUID FISHING_KING_UUID;
    private static HashMap<FishingPerk, SpellInstance> AVAILABLE_SPELLS = new HashMap<>();
    private static HashMap<String, FishingPerk> PERKS = new HashMap<>();

    public static HashMap<FishingPerk, SpellInstance> getAvailableSpells() {
        return AVAILABLE_SPELLS;
    }

    public static void updateAvailableSpells(HashMap<FishingPerk, SpellInstance> availableSpells) {
        AVAILABLE_SPELLS = availableSpells;
    }
    public static HashMap<String, FishingPerk> getPerks() {
        return PERKS;
    }

    public static void updatePerks(HashMap<String, FishingPerk> perksMap) {
        PERKS = perksMap;
    }

    public static void onPosition3rd(ModelPart arm, BipedEntityModel.ArmPose armPose, LivingEntity entity) {
        if (armPose != BipedEntityModel.ArmPose.THROW_SPEAR) {
            return;
        }
        if (entity.getActiveItem().isOf(ItemRegistry.MEMBER_FISHING_ROD))  {
            float usedFor = MathHelper.clamp(entity.getItemUseTime() / 100f, 0, 1);
            arm.pitch = (float) Math.PI - usedFor + 1f;
        }
    }

    public static void onPosition1st(AbstractClientPlayerEntity player, float tickDelta, MatrixStack matrices) {
        matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion((float) MathHelper.clamp((Math.sqrt(player.getItemUseTime() + tickDelta)* 4f), 0, 50)));
        matrices.translate(0, MathHelper.clamp((Math.sqrt(player.getItemUseTime()+tickDelta)/20f), 0, 0.5),0);
    }


    @Override
    public void onInitializeClient() {
        FishingClubRegistry.registerClient();
    }
}
