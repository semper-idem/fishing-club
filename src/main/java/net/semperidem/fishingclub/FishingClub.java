package net.semperidem.fishingclub;

import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.semperidem.fishingclub.registry.FRegistry;

public class FishingClub implements ModInitializer {
    public static final String MOD_ID = "fishing-club";

    public static TrackedData<NbtCompound> FISHER_NBT = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.NBT_COMPOUND);

    @Override
    public void onInitialize() {
        CommandsUtil.register();
        FRegistry.register();
        //FishingClubTest.runTest();
    }

    public static Identifier getIdentifier(String resource){
        return new Identifier(MOD_ID, resource);
    }
}
