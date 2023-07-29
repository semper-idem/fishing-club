package net.semperidem.fishingclub.entity;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.WanderingTraderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.ChestBoatEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.client.screen.dialog.DialogScreen;

public class FishermanEntity extends WanderingTraderEntity {
    public FishermanEntity(World world) {
        super(FishingClub.FISHERMAN, world);
    }

    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, EntityData entityData, NbtCompound entityNbt) {

        this.setCustomName(Text.of("Derek ol'Stinker"));
        if (!this.hasVehicle()) {
            // Create the boat.
            ChestBoatEntity boat = new ChestBoatEntity(EntityType.CHEST_BOAT, (World) world);
            boat.refreshPositionAndAngles(this.getX(), this.getY(), this.getZ(), 0, 0.0F);

            // Make the entity start riding the boat.
            this.startRiding(boat);

            // Spawn the boat in the world.
            world.spawnEntity(boat);
        }
        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
    }

    @Override
    public ActionResult interactMob(PlayerEntity playerEntity, Hand hand) {
        if (world.isClient()){
            MinecraftClient.getInstance().setScreen(new DialogScreen(getCustomName()));
        }
        return ActionResult.CONSUME;
    }

}
