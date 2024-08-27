package net.semperidem.fishingclub.fish;

import com.mojang.datafixers.DataFixUtils;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.FollowGroupLeaderGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.passive.FishEntity;
import net.minecraft.entity.passive.SchoolingFishEntity;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public abstract class AbstractSchoolingFishEntity extends AbstractFishEntity{

    @Nullable
    private AbstractSchoolingFishEntity leader;
    private int groupSize = 1;

    public AbstractSchoolingFishEntity(EntityType<? extends AbstractFishEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(5, new FollowGroupLeaderGoal(this));
    }

    @Override
    public int getLimitPerChunk() {
        return this.getMaxGroupSize();
    }

    public int getMaxGroupSize() {
        return super.getLimitPerChunk();
    }

    @Override
    protected boolean hasSelfControl() {
        return !this.hasLeader();
    }

    public boolean hasLeader() {
        return this.leader != null && this.leader.isAlive();
    }

    public AbstractSchoolingFishEntity joinGroupOf(AbstractSchoolingFishEntity groupLeader) {
        this.leader = groupLeader;
        groupLeader.increaseGroupSize();
        return groupLeader;
    }

    public void leaveGroup() {
        this.leader.decreaseGroupSize();
        this.leader = null;
    }

    private void increaseGroupSize() {
        this.groupSize++;
    }

    private void decreaseGroupSize() {
        this.groupSize--;
    }

    public boolean canHaveMoreFishInGroup() {
        return this.hasOtherFishInGroup() && this.groupSize < this.getMaxGroupSize();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.hasOtherFishInGroup() && this.getWorld().random.nextInt(200) == 1) {
            List<? extends FishEntity> list = this.getWorld().getNonSpectatingEntities(this.getClass(), this.getBoundingBox().expand(8.0, 8.0, 8.0));
            if (list.size() <= 1) {
                this.groupSize = 1;
            }
        }
    }

    public boolean hasOtherFishInGroup() {
        return this.groupSize > 1;
    }

    public boolean isCloseEnoughToLeader() {
        return this.squaredDistanceTo(this.leader) <= 121.0;
    }

    public void moveTowardLeader() {
        if (this.hasLeader()) {
            this.getNavigation().startMovingTo(this.leader, 1.0);
        }
    }

    public void pullInOtherFish(Stream<? extends AbstractSchoolingFishEntity> fish) {
        fish.limit((long)(this.getMaxGroupSize() - this.groupSize)).filter(fishx -> fishx != this).forEach(fishx -> fishx.joinGroupOf(this));
    }

    @Nullable
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
        super.initialize(world, difficulty, spawnReason, entityData);
        if (entityData == null) {
            entityData = new AbstractSchoolingFishEntity.FishData(this);
        } else {
            this.joinGroupOf(((AbstractSchoolingFishEntity.FishData)entityData).leader);
        }

        return entityData;
    }

    public static class FishData implements EntityData {
        public final AbstractSchoolingFishEntity leader;

        public FishData(AbstractSchoolingFishEntity leader) {
            this.leader = leader;
        }
    }

    public class FollowGroupLeaderGoal extends Goal {
        private static final int MIN_SEARCH_DELAY = 200;
        private final AbstractSchoolingFishEntity fish;
        private int moveDelay;
        private int checkSurroundingDelay;

        public FollowGroupLeaderGoal(AbstractSchoolingFishEntity fish) {
            this.fish = fish;
            this.checkSurroundingDelay = this.getSurroundingSearchDelay(fish);
        }

        protected int getSurroundingSearchDelay(AbstractSchoolingFishEntity fish) {
            return toGoalTicks(200 + fish.getRandom().nextInt(200) % 20);
        }

        @Override
        public boolean canStart() {
            if (this.fish.hasOtherFishInGroup()) {
                return false;
            } else if (this.fish.hasLeader()) {
                return true;
            } else if (this.checkSurroundingDelay > 0) {
                this.checkSurroundingDelay--;
                return false;
            } else {
                this.checkSurroundingDelay = this.getSurroundingSearchDelay(this.fish);
                Predicate<AbstractSchoolingFishEntity> predicate = fish -> fish.canHaveMoreFishInGroup() || !fish.hasLeader();
                List<? extends AbstractSchoolingFishEntity> list = this.fish
                        .getWorld()
                        .getEntitiesByClass(this.fish.getClass(), this.fish.getBoundingBox().expand(8.0, 8.0, 8.0), predicate);
                AbstractSchoolingFishEntity schoolingFishEntity = DataFixUtils.orElse(list.stream().filter(AbstractSchoolingFishEntity::canHaveMoreFishInGroup).findAny(), this.fish);
                schoolingFishEntity.pullInOtherFish(list.stream().filter(fish -> !fish.hasLeader()));
                return this.fish.hasLeader();
            }
        }

        @Override
        public boolean shouldContinue() {
            return this.fish.hasLeader() && this.fish.isCloseEnoughToLeader();
        }

        @Override
        public void start() {
            this.moveDelay = 0;
        }

        @Override
        public void stop() {
            this.fish.leaveGroup();
        }

        @Override
        public void tick() {
            if (--this.moveDelay <= 0) {
                this.moveDelay = this.getTickCount(10);
                this.fish.moveTowardLeader();
            }
        }
    }
}
