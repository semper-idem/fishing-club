package net.semperidem.fishingclub.mixin.common;


import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.encryption.PlayerPublicKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.fisher.FishingCardSerializer;
import net.semperidem.fishingclub.fisher.FishingPlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin extends PlayerEntity implements FishingPlayerEntity {
    @Unique
    public FishingCard fishingCard = new FishingCard(this);

    public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile, @Nullable PlayerPublicKey publicKey) {
        super(world, pos, yaw, gameProfile, publicKey);
    }

    @Override
    public void readNbt(NbtCompound nbt){
        fishingCard = FishingCardSerializer.fromNbt(this, nbt.getCompound(FishingCardSerializer.TAG));
        super.readNbt(nbt);
    }
    @Override
    public NbtCompound writeNbt(NbtCompound nbt){
        super.writeNbt(nbt);
        nbt.put(FishingCardSerializer.TAG, FishingCardSerializer.toNbt(fishingCard));
        return nbt;
    }

    @Shadow
    public boolean isSpectator() {
        return false;
    }

    @Shadow
    public boolean isCreative() {
        return false;
    }

    @Override
    public FishingCard getCard() {
        return fishingCard;
    }
}
