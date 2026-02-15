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
                    Pair.of(ModMemoryTypes.NEAREST_UNOCCUPIED_BED.get(), MemoryStatus.VALUE_PRESENT),
                    Pair.of(MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED),
                    Pair.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED),
                    Pair.of(ModMemoryTypes.FEAR_LEVEL.get(), MemoryStatus.REGISTERED),
                    Pair.of(ModMemoryTypes.INVESTIGATE_TARGET.get(), MemoryStatus.VALUE_ABSENT),
                    Pair.of(ModMemoryTypes.SPOTTED_PLAYER.get(), MemoryStatus.VALUE_ABSENT)
            );

    private long nextOkSleepTime;
    private final float walkSpeed;
    private final boolean isEnabled;

    private static final long COOLDOWN_AFTER_WAKING_UP = 100L;
    private static final int closeEnough = 1;

    public GoToSleepBehaviour(float walkSpeed, boolean isEnabled) {
        this.walkSpeed = walkSpeed;
        this.isEnabled = isEnabled;
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    private static BlockPos getWalkablePos(ServerLevel level, BlockPos target) {
        if (!level.getBlockState(target).getCollisionShape(level, target).isEmpty()) {
            return target.above();
        }
        return target;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        LOGGER.debug("[YAH:R] [BEHAVIOR:{}][{}] checking extra start conditions", this.getClass().getSimpleName(), entity.getUUID());
        return level.isNight();
    }

    @Override
    protected void start(ServerLevel level, E entity, long gameTime) {
        Brain<?> brain = entity.getBrain();
        if (SteveLogic.isUneasy(brain, gameTime) || SteveLogic.isScared(brain, gameTime) || SteveLogic.isTerrified(brain, gameTime)) {
            LOGGER.debug("[YAH:R] [BEHAVIOR:{}][{}] conditions not met, return", this.getClass().getSimpleName(), entity.getUUID());
            return;
        }
        if (gameTime > this.nextOkSleepTime) {
            MemoryModuleType<BlockPos> bedTarget = ModMemoryTypes.NEAREST_UNOCCUPIED_BED.get();
            BlockPos bedPos = brain.getMemory(bedTarget).orElse(null);
            if (bedPos == null) {
                LOGGER.debug("[YAH:R] [BEHAVIOR:{}][{}] variable \"bedPos\" is null, return", this.getClass().getSimpleName(), entity.getUUID());
                return;
            }
            BlockPos walkTarget = getWalkablePos(level, bedPos);
            if (entity.blockPosition().closerThan(bedPos, closeEnough)) {
                entity.getNavigation().stop();
                brain.eraseMemory(MemoryModuleType.LOOK_TARGET);
                brain.eraseMemory(MemoryModuleType.WALK_TARGET);

                entity.startSleeping(bedPos);
            }
            brain.setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(walkTarget, walkSpeed, closeEnough));
            LOGGER.debug("[YAH:R] [BEHAVIOR:{}][{}] set WALK_TARGET -> {}", this.getClass().getSimpleName(), entity.getUUID(), walkTarget);
            brain.setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(bedPos));
            LOGGER.debug("[YAH:R] [BEHAVIOR:{}][{}] set LOOK_TARGET -> {}", this.getClass().getSimpleName(), entity.getUUID(), bedPos);
        }

    }

    @Override
    protected void stop(ServerLevel level, E entity, long gameTime) {
        LOGGER.debug("[YAH:R] [BEHAVIOR:{}][{}] stopped", this.getClass().getSimpleName(), entity.getUUID());
        if (entity.isSleeping()) {
            LOGGER.debug("[YAH:R] [BEHAVIOR:{}][{}] forced Entity({}) to stop sleeping", this.getClass().getSimpleName(), entity.getUUID(), entity.getUUID());
            entity.stopSleeping();
            this.nextOkSleepTime = gameTime + COOLDOWN_AFTER_WAKING_UP;
        }
    }
}
