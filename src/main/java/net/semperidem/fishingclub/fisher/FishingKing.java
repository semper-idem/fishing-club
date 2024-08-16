package net.semperidem.fishingclub.fisher;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.scoreboard.ScoreHolder;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.fisher.level_reward.LevelUpEffect;
import net.semperidem.fishingclub.registry.FCStatusEffects;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.scoreboard.ScoreboardComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.scoreboard.ScoreboardComponentInitializer;

import java.util.UUID;

public final class FishingKing implements ScoreboardComponentInitializer, AutoSyncedComponent {
    private static final String FISHING_KING_KEY = "fishing_king";
    public static final ComponentKey<FishingKing> FISHING_KING = ComponentRegistry.getOrCreate(FishingClub.getIdentifier(FISHING_KING_KEY), FishingKing.class);

    private static final String UUID_KEY = "uuid";
    private static final String IS_CLAIMED = "is_claimed";
    private static final String NAME_KEY = "name";
    private static final String PRICE_KEY = "price";
    private static final String TIMESTAMP_KEY = "timestamp";

    private static final Text CLAIM_TEXT = Text.literal(" is new King of the Fishing Club");

    private static final int MIN_PRICE = 10000;
    private static final float PRICE_MULTIPLIER = 1.5f;

    private Scoreboard scoreboard;
    private ServerWorld serverWorld;
    private ScoreboardObjective objective;

    private static final int REFRESH_TICK_RATE = 200;
    private int refreshTick = 0;

    //Serialized
    private UUID uuid;
    private String name = "";
    private int price = MIN_PRICE;
    private long timestamp = 0;


    public static FishingKing of(Scoreboard scoreboard) {
        return FISHING_KING.get(scoreboard);
    }


    public FishingKing(Scoreboard scoreboard, MinecraftServer server) {
        this();
        this.scoreboard = scoreboard;
        if (server == null) {
            return;
        }
        this.serverWorld = server.getOverworld();
        if ((this.objective = scoreboard.getNullableObjective(FISHING_KING_KEY)) != null) {
            return;
        }
//        this.objective = scoreboard.addObjective(
//                FISHING_KING_KEY,
//                ScoreboardCriterion.DUMMY,
//                Text.of(FISHING_KING_KEY),
//                ScoreboardCriterion.RenderType.INTEGER,
//                true,
//                StyledNumberFormat.EMPTY
//        );
    }
    @Override
    public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        this.price = tag.getInt(PRICE_KEY);
        this.timestamp = tag.getLong(TIMESTAMP_KEY);
        if (tag.contains(IS_CLAIMED) && !tag.getBoolean(IS_CLAIMED)) {
            return;
        }
        this.uuid = tag.getUuid(UUID_KEY);
        this.name = tag.getString(NAME_KEY);
    }

    @Override
    public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        tag.putInt(PRICE_KEY, this.price);
        tag.putLong(TIMESTAMP_KEY, this.timestamp);
        if (this.uuid == null) {
            tag.putBoolean(IS_CLAIMED, false);
            return;
        }
        tag.putBoolean(IS_CLAIMED, true);
        tag.putUuid(UUID_KEY, this.uuid);
        tag.putString(NAME_KEY, this.name);
    }

    @Override
    public void registerScoreboardComponentFactories(ScoreboardComponentFactoryRegistry registry) {
        registry.registerScoreboardComponent(FISHING_KING, FishingKing::new);
    }

    public FishingKing() {}//For entrypoint requirement

    public UUID getUuid(){
        return this.uuid;
    }

    public String getName(){
        return this.name;
    }

    public int getPrice(){
        return this.price;
    }
    public int getPriceFor(ServerPlayerEntity claimedBy) {
        return this.price;//Will be used to calculate discount by leaderboard titles
    }

    public void claimCape(ServerPlayerEntity claimedBy, int amount) {
        if (claimedBy.getUuid().equals(this.uuid)) {
            return;
        }
        FishingCard fishingCard = FishingCard.of(claimedBy);
        int availableCredit = fishingCard.getCredit();
        if (availableCredit < this.price || amount < this.price) {
            return;
        }


        fishingCard.addCredit(-amount);
        int currentReign = (int) (this.getCurrentReign() / 1200);
        this.scoreboard.getOrCreateScore(ScoreHolder.fromName(this.name), this.objective).incrementScore(currentReign);//1 for each minute
        this.price = (int)(amount * PRICE_MULTIPLIER);
        this.uuid = claimedBy.getUuid();
        this.name = claimedBy.getNameForScoreboard();

        LevelUpEffect.RARE_EFFECT.execute(claimedBy.getServerWorld(), claimedBy.getX(), claimedBy.getY(), claimedBy.getZ());//todo add unique effect
        this.serverWorld.getPlayers().forEach(serverPlayerEntity -> serverPlayerEntity.sendMessageToClient(Text.literal(this.name).append(CLAIM_TEXT), true));
        FISHING_KING.sync(this.scoreboard);
    }

    public long getCurrentReign(){
        return this.serverWorld.getTime() - this.timestamp;
    }

    public void tick(ServerPlayerEntity playerEntity) {
        if (!this.uuid.equals(playerEntity.getUuid())) {
            return;
        }

        if (this.refreshTick > 0) {
            this.refreshTick--;
            return;
        }
        this.refreshTick = REFRESH_TICK_RATE;
        applyAura(playerEntity);
    }

    private void applyAura(ServerPlayerEntity kingEntity) {
        kingEntity.getServerWorld()
                .getOtherEntities(kingEntity, new Box(kingEntity.getBlockPos()).expand(5))
                .stream()
                .filter(ServerPlayerEntity.class::isInstance)
                .map(ServerPlayerEntity.class::cast)
                .forEach(otherPlayer -> {
                    applyStatusEffect(otherPlayer, FCStatusEffects.EXP_BUFF);
                    applyStatusEffect(otherPlayer, FCStatusEffects.QUALITY_BUFF);
                    applyStatusEffect(otherPlayer, FCStatusEffects.BOBBER_BUFF);
                    applyStatusEffect(otherPlayer, FCStatusEffects.FREQUENCY_BUFF);
                });
    }

    private void applyStatusEffect(ServerPlayerEntity otherPlayer, RegistryEntry<StatusEffect> effectEntry) {
        otherPlayer.addStatusEffect(new StatusEffectInstance(effectEntry, REFRESH_TICK_RATE * 6));
    }
}
