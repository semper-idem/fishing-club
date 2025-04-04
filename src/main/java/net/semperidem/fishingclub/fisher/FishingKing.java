package net.semperidem.fishingclub.fisher;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.scoreboard.ScoreHolder;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardCriterion;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.number.BlankNumberFormat;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;
import net.semperidem.fishingclub.fisher.level_reward.LevelUpEffect;
import net.semperidem.fishingclub.registry.Components;
import net.semperidem.fishingclub.registry.StatusEffects;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

import java.util.UUID;

public final class FishingKing implements AutoSyncedComponent {
    public static final String TAG_KEY = "fishing_king";

    private static final String UUID_KEY = "uuid";
    private static final String IS_CLAIMED = "is_claimed";
    private static final String NAME_KEY = "label";
    private static final String PRICE_KEY = "price";
    private static final String TIMESTAMP_KEY = "timestamp";

    private static final Text CLAIM_TEXT = Text.literal(" is new King of the Fishing Club");
    private static final Text SCOREBOARD_TITLE = Text.literal("Fishing King");

    private static final int MIN_PRICE = 10000;
    private static final float PRICE_MULTIPLIER = 1.5f;
    private static final int REFRESH_TICK_RATE = 200;

    private final Scoreboard scoreboard;
    private final ScoreboardObjective objective;

    private int refreshTick = 0;

    private UUID uuid;
    private String name = "";
    private int price = MIN_PRICE;
    private long timestamp = 0;


    public FishingKing(Scoreboard scoreboard) {
        this.scoreboard = scoreboard;
        ScoreboardObjective existingObjective = this.scoreboard.getNullableObjective(TAG_KEY);
        this.objective = existingObjective != null ? existingObjective : this.scoreboard.addObjective(
                TAG_KEY,
                ScoreboardCriterion.DUMMY,
                SCOREBOARD_TITLE,
                ScoreboardCriterion.RenderType.INTEGER,
                false,
                new BlankNumberFormat()
        );
    }

    public static FishingKing of(Scoreboard scoreboard) {
        return Components.FISHING_KING.get(scoreboard);
    }

    @Override
    public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        this.price = tag.getInt(PRICE_KEY, 0);
        this.timestamp = tag.getLong(TIMESTAMP_KEY, 0);
        if (tag.contains(IS_CLAIMED) && !tag.getBoolean(IS_CLAIMED, false)) {
            return;
        }
        this.uuid = UUID.fromString(tag.getString(UUID_KEY, String.valueOf(UUID.randomUUID())));
        this.name = tag.getString(NAME_KEY, "");
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
        tag.putString(UUID_KEY, String.valueOf(this.uuid));
        tag.putString(NAME_KEY, this.name);
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public String getName() {
        return this.name;
    }

    public int getPrice() {
        return this.price;
    }

    public int getPriceFor(ServerPlayerEntity claimedBy) {
        return this.price;//Will be used to calculate discount by leaderboard titles
    }

    public void claimCape(ServerPlayerEntity claimedBy, int amount) {
        if (claimedBy.getUuid().equals(this.uuid)) {
            return;
        }
        Card card = Card.of(claimedBy);
        int availableCredit = card.getGS();
        if (availableCredit < this.price || amount < this.price) {
            return;
        }

        card.addGS(-amount);
        ServerWorld serverWorld = claimedBy.getServerWorld();
        int currentReign = (int) ((serverWorld.getTime() - this.timestamp) / 1200);
        this.scoreboard.getOrCreateScore(ScoreHolder.fromName(this.name), this.objective).incrementScore(currentReign);//1 for each minute
        this.price = (int) (amount * PRICE_MULTIPLIER);
        this.uuid = claimedBy.getUuid();
        this.name = claimedBy.getNameForScoreboard();

        LevelUpEffect.RARE_EFFECT.execute(claimedBy.getServerWorld(), claimedBy.getX(), claimedBy.getY(), claimedBy.getZ());//todo add unique effect
        serverWorld.getPlayers().forEach(serverPlayerEntity -> serverPlayerEntity.sendMessageToClient(Text.literal(this.name).append(CLAIM_TEXT), true));
        Components.FISHING_KING.sync(this.scoreboard);
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
        tickMightyPresence(playerEntity);
    }

    private void tickMightyPresence(ServerPlayerEntity kingEntity) {
        kingEntity.getServerWorld()
                .getOtherEntities(kingEntity, new Box(kingEntity.getBlockPos()).expand(5))
                .stream()
                .filter(ServerPlayerEntity.class::isInstance)
                .map(ServerPlayerEntity.class::cast)
                .forEach(otherPlayer -> {
                    applyStatusEffect(otherPlayer, StatusEffects.EXP);
                    applyStatusEffect(otherPlayer, StatusEffects.QUALITY_BUFF);
                    applyStatusEffect(otherPlayer, StatusEffects.BOBBER);
                    applyStatusEffect(otherPlayer, StatusEffects.CATCH_RATE_BUFF);
                });
    }

    private void applyStatusEffect(ServerPlayerEntity otherPlayer, RegistryEntry<StatusEffect> effectEntry) {
        otherPlayer.addStatusEffect(new StatusEffectInstance(effectEntry, REFRESH_TICK_RATE * 6));
    }
}
