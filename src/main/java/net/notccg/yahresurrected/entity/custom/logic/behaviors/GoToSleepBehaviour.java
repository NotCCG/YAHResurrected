package net.notccg.yahresurrected.entity.custom.logic.behaviors;

import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.notccg.yahresurrected.entity.custom.logic.steve_ai.SteveLogic;
import net.notccg.yahresurrected.util.ModMemoryTypes;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import org.slf4j.Logger;

import java.util.List;

// Much like with myself, this behaviour does not work yet
public class GoToSleepBehaviour<E extends PathfinderMob> extends ExtendedBehaviour<E> {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS =
            ObjectArrayList.of(
                    Pair.of(ModMemoryTypes.NEAREST_UNOCCUPIED_BED.get(), MemoryStatus.REGISTERED),
                    Pair.of(ModMemoryTypes.SPAWN_POINT.get(), MemoryStatus.REGISTERED),
                    Pair.of(ModMemoryTypes.IS_SLEEPING.get(), MemoryStatus.REGISTERED),
                    Pair.of(MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED),
                    Pair.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED),
                    Pair.of(ModMemoryTypes.FEAR_LEVEL.get(), MemoryStatus.REGISTERED),
                    Pair.of(ModMemoryTypes.INTERESTED_BLOCK_TARGET.get(), MemoryStatus.VALUE_ABSENT),
                    Pair.of(ModMemoryTypes.INVESTIGATE_TARGET.get(), MemoryStatus.VALUE_ABSENT),
                    Pair.of(ModMemoryTypes.SPOTTED_PLAYER.get(), MemoryStatus.VALUE_ABSENT)
            );

    private long nextOkSleepTime;
    private final float walkSpeed;
    private final boolean isEnabled;

    private static final long COOLDOWN_AFTER_WAKING_UP = 100L;
    private static final int closeEnough = 1;
    private BlockPos bedTarget = null;

    public GoToSleepBehaviour(float walkSpeed, boolean isEnabled) {
        this.walkSpeed = walkSpeed;
        this.isEnabled = isEnabled;
    }

    private static BlockPos getWalkablePos(ServerLevel level, BlockPos target) {
        if (!level.getBlockState(target).getCollisionShape(level, target).isEmpty()) {
            return target.above();
        }
        return target;
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected boolean shouldKeepRunning(E entity) {
        boolean correctTime = entity.level().isNight();
        return correctTime && isEnabled;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        LOGGER.debug("[YAH:R] [BEHAVIOR:{}][{}] checking extra start conditions",
                this.getClass().getSimpleName(), entity.getUUID());
        return level.isNight() && isEnabled;
    }

    @Override
    protected void start(ServerLevel level, E entity, long gameTime) {
        Brain<?> brain = entity.getBrain();
        if (SteveLogic.isUneasy(brain, gameTime) || SteveLogic.isScared(brain, gameTime) || SteveLogic.isTerrified(brain, gameTime)) {
            LOGGER.debug("[YAH:R] [BEHAVIOR:{}][{}] conditions not met, return", this.getClass().getSimpleName(), entity.getUUID());
            return;
        }
        nextOkSleepTime = gameTime;
    }

    @Override
    protected void tick(ServerLevel level, E entity, long gameTime) {
        var brain = entity.getBrain();
        if (gameTime > this.nextOkSleepTime) {
            BlockPos bedPos = brain.getMemory(ModMemoryTypes.NEAREST_UNOCCUPIED_BED.get()).orElse(null);
            BlockPos spawnPos = brain.getMemory(ModMemoryTypes.SPAWN_POINT.get()).orElse(null);
            if (spawnPos != null) {
                bedTarget = spawnPos;
            } else {
                if (bedPos != null) {
                    bedTarget = bedPos;
                }
            }
            if (bedTarget == null) {
                LOGGER.debug("[YAH:R] [BEHAVIOR:{}][{}] variable \"bedTarget\" null, return",
                        this.getClass().getSimpleName(), entity.getUUID());
                return;
            }

            if (entity.blockPosition().closerThan(bedTarget, closeEnough)) {
                brain.eraseMemory(MemoryModuleType.LOOK_TARGET);
                brain.eraseMemory(MemoryModuleType.WALK_TARGET);

                if (!brain.hasMemoryValue(ModMemoryTypes.SPAWN_POINT.get())) {
                    brain.setMemory(ModMemoryTypes.SPAWN_POINT.get(), bedTarget);
                }
                brain.setMemory(ModMemoryTypes.IS_SLEEPING.get(), true);
                entity.startSleeping(bedTarget);
                return;
            }
            BlockPos walkPos = getWalkablePos(level, bedTarget);
            brain.setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(walkPos, walkSpeed, closeEnough));
            LOGGER.debug("[YAH:R] [BEHAVIOR:{}][{}] set WALK_TARGET -> {}", this.getClass().getSimpleName(), entity.getUUID(), bedTarget);
            brain.setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(bedTarget));
            LOGGER.debug("[YAH:R] [BEHAVIOR:{}][{}] set LOOK_TARGET -> {}", this.getClass().getSimpleName(), entity.getUUID(), bedTarget);
        }
    }

    @Override
    protected void stop(ServerLevel level, E entity, long gameTime) {
        LOGGER.debug("[YAH:R] [BEHAVIOR:{}][{}] stop()",
                this.getClass().getSimpleName(), entity.getUUID());
        if (entity.isSleeping()) {
            LOGGER.debug("[YAH:R] [BEHAVIOR:{}][{}] forced Entity({}) to stop sleeping",
                    this.getClass().getSimpleName(), entity.getUUID(), entity.getUUID());
            entity.stopSleeping();

            entity.getBrain().eraseMemory(ModMemoryTypes.IS_SLEEPING.get());
            LOGGER.debug("[YAH:R] [BEHAVIOR:{}][{}] erased IS_SLEEPING",
                    this.getClass().getSimpleName(), entity.getUUID());

            this.nextOkSleepTime = gameTime + COOLDOWN_AFTER_WAKING_UP;
        }
    }
}
