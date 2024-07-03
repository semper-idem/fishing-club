package net.semperidem.fishingclub.client;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.semperidem.fishingclub.fisher.perks.FishingPerk;
import net.semperidem.fishingclub.fisher.perks.spells.SpellInstance;
import net.semperidem.fishingclub.registry.FCItems;
import net.semperidem.fishingclub.registry.FCRegistry;

import java.util.HashMap;
import java.util.UUID;

public class FishingClubClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        FCRegistry.registerClient();
    }
}
