package net.notccg.yahresurrected.entity.custom.logic.behaviors;

import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.Target;
import net.minecraft.world.phys.Vec3;
import net.notccg.yahresurrected.util.ModMemoryTypes;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import org.slf4j.Logger;

import java.util.*;

public class SetInterestedBlockTarget<E extends Mob> extends ExtendedBehaviour<E> {
    private static final Logger LOGGER = LogUtils.getLogger();

    private final float speed;
    private final int arriveDistance;
    private final int repathInterval;
    private final boolean isEnabled;

    private long nextTick = 0;

    private final Map<E, StuckState> stuckState = new WeakHashMap<>();
    private static final int MAX_STUCK_TICKS = 60;
    private static final double MIN_MOV_SQR = 0.01;
    private static class StuckState {
        Vec3 lastPos;
        int stuckTicks;
        long nextRepathTick;
        BlockPos lastTarget;
    }

    public SetInterestedBlockTarget(float speed, int arriveDistance, int repathInterval, boolean isEnabled) {
        this.speed = speed;
        this.arriveDistance = arriveDistance;
        this.repathInterval = repathInterval;
        this.isEnabled = isEnabled;
    }

    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS =
            ObjectArrayList.of(
                    Pair.of(MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED),
                    Pair.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED),
                    Pair.of(ModMemoryTypes.SPOTTED_PLAYER.get(), MemoryStatus.VALUE_ABSENT),
                    Pair.of(ModMemoryTypes.INTERESTED_BLOCK_TARGET.get(), MemoryStatus.VALUE_PRESENT),
                    Pair.of(ModMemoryTypes.VISITED_BLOCKS.get(), MemoryStatus.REGISTERED)
            );

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
        return isEnabled;
    }

    @Override
    protected boolean shouldKeepRunning(E entity) {
        return isEnabled &&
                (entity.getBrain().hasMemoryValue(ModMemoryTypes.INTERESTED_BLOCK_TARGET.get())) &&
                !(entity.getBrain().hasMemoryValue(ModMemoryTypes.SPOTTED_PLAYER.get()));
    }

    @Override
    protected void start(ServerLevel level, E entity, long gameTime) {
        nextTick = gameTime;
    }

    @Override
    protected void tick(ServerLevel level, E entity, long gameTime) {
        if (gameTime < nextTick) return;
        nextTick = gameTime + repathInterval;

        StuckState state = new StuckState();
        state.lastPos = entity.position();
        state.stuckTicks = 0;
        state.nextRepathTick = gameTime;
        state.lastTarget = null;
        stuckState.put(entity, state);

        var brain = entity.getBrain();
        BlockPos target = brain.getMemory(ModMemoryTypes.INTERESTED_BLOCK_TARGET.get()).orElse(null);
        if (target == null) {
            LOGGER.debug("[YAH:R] [BEHAVIOR:{}][{}] variable \"target\" null, return",
                    this.getClass().getSimpleName(), entity.getUUID());
            return;
        }

        BlockPos walkPos = getWalkablePos(level, target);
        if (walkPos == null) {
            LOGGER.debug("[YAH:R] [BEHAVIOR:{}][{}] variable \"walkPos\" null, return",
                    this.getClass().getSimpleName(), entity.getUUID());
            return;
        }

        if (entity.blockPosition().closerThan(walkPos, arriveDistance)) {
            onArrive(entity, walkPos, target);
            return;
        }

        if (state.lastTarget == null || !state.lastTarget.equals(target)) {
            state.lastTarget = target.immutable();
            state.stuckTicks = 0;
            state.lastPos = entity.position();
        } else {
            Vec3 currentPos = entity.position();
            double movedSqr = currentPos.distanceToSqr(state.lastPos);

            if (brain.hasMemoryValue(MemoryModuleType.WALK_TARGET) && (movedSqr < MIN_MOV_SQR)) {
                state.stuckTicks++;
            } else {
                state.stuckTicks = 0;
            }

            state.lastPos = currentPos;

            if (state.stuckTicks >= MAX_STUCK_TICKS) {
                abandonTarget(entity, walkPos, target, "entity_stuck");
                return;
            }
        }

        if (gameTime >+ state.nextRepathTick) {
            repath(level, entity, gameTime);
        }
    }

    private void repath(ServerLevel level, E entity, long gameTime) {
        var brain = entity.getBrain();
        BlockPos target = brain.getMemory(ModMemoryTypes.INTERESTED_BLOCK_TARGET.get()).orElse(null);
        if (target == null) return;

        BlockPos walkPos =  getWalkablePos(level, target);

        StuckState state = stuckState.get(entity);
        if (state != null) state.nextRepathTick = gameTime + repathInterval;

        brain.setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(walkPos, speed, 1));
        brain.setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(walkPos));

        LOGGER.debug("[YAH:R] [BEHAVIOR:{}][{}] set WALK_TARGET/LOOK_TARGET -> {}",
                this.getClass().getSimpleName(), entity.getUUID(), walkPos);
    }

    private void onArrive(E entity, BlockPos walkPos, BlockPos target) {
        var brain = entity.getBrain();

        LOGGER.debug("[YAH:R] [BEHAVIOR:{}][{}] arrived at {} and added {} -> VISITED_BLOCKS",
                this.getClass().getSimpleName(), entity.getUUID(), walkPos, target);

        Set<BlockPos> visited = brain.getMemory(ModMemoryTypes.VISITED_BLOCKS.get()).orElseGet(HashSet::new);
        visited.add(target.immutable());

        if (visited.size() > 1024) {
            int excess = visited.size() - 1024;
            var it = visited.iterator();
            while (excess-- > 0 && it.hasNext()) {
                it.next();
                it.remove();
            }
        }

        brain.setMemory(ModMemoryTypes.VISITED_BLOCKS.get(), new HashSet<>(visited));

        brain.eraseMemory(ModMemoryTypes.INTERESTED_BLOCK_TARGET.get());
        LOGGER.debug("[YAH:R] [BEHAVIOR:{}][{}] erased INTERESTED_BLOCK_TARGET",
                this.getClass().getSimpleName(), entity.getUUID());

        brain.eraseMemory(MemoryModuleType.WALK_TARGET);
        LOGGER.debug("[YAH:R] [BEHAVIOR:{}][{}] erased WALK_TARGET",
                this.getClass().getSimpleName(), entity.getUUID());

        brain.eraseMemory(MemoryModuleType.LOOK_TARGET);
        LOGGER.debug("[YAH:R] [BEHAVIOR:{}][{}] erased LOOK_TARGET",
                this.getClass().getSimpleName(), entity.getUUID());
    }

    private void abandonTarget(E entity, BlockPos walkPos, BlockPos target, String reason) {
        var brain = entity.getBrain();

        LOGGER.debug("[YAH:R] [BEHAVIOR:{}][{}] abondoning [TARGET: {}]|[WALKPOS: {}] [REASON:\"{}\"]",
                this.getClass().getSimpleName(), entity.getUUID(), target, walkPos, reason);

        Set<BlockPos> visited =  brain.getMemory(ModMemoryTypes.VISITED_BLOCKS.get()).orElseGet(HashSet::new);
        visited.add(target.immutable());

        if (visited.size() > 1024) {
            int excess = visited.size() - 1024;
            var it = visited.iterator();
            while ((excess-- > 0) && it.hasNext()) {
                it.next();
                it.remove();
            }
        }

        brain.setMemory(ModMemoryTypes.VISITED_BLOCKS.get(), new HashSet<>(visited));

        brain.eraseMemory(ModMemoryTypes.INTERESTED_BLOCK_TARGET.get());
        LOGGER.debug("[YAH:R] [BEHAVIOR:{}][{}] erased INTERESTED_BLOCK_TARGET",
                this.getClass().getSimpleName(), entity.getUUID());

        brain.eraseMemory(MemoryModuleType.WALK_TARGET);
        LOGGER.debug("[YAH:R] [BEHAVIOR:{}][{}] erased WALK_TARGET",
                this.getClass().getSimpleName(), entity.getUUID());

        brain.eraseMemory(MemoryModuleType.LOOK_TARGET);
        LOGGER.debug("[YAH:R] [BEHAVIOR:{}][{}] erased LOOK_TARGET",
                this.getClass().getSimpleName(), entity.getUUID());

        stuckState.remove(entity);
        LOGGER.debug("[YAH:R] [BEHAVIOR:{}][{}] removed StuckState",
                this.getClass().getSimpleName(), entity.getUUID());
    }

    @Override
    protected void stop(E entity) {
        entity.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);
        LOGGER.debug("[YAH:R] [BEHAVIOR:{}][{}] erased LOOK_TARGET",
                this.getClass().getSimpleName(), entity.getUUID());

        stuckState.remove(entity);
        LOGGER.debug("[YAH:R] [BEHAVIOR:{}][{}] removed StuckState",
                this.getClass().getSimpleName(), entity.getUUID());

        LOGGER.debug("[YAH:R] [BEHAVIOR:{}][{}] stopped",
                this.getClass().getSimpleName(), entity.getUUID());
    }
}
