package net.notccg.yahresurrected.entity.custom.logic.behaviors;

import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.memory.*;
import net.notccg.yahresurrected.util.ModMemoryTypes;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import org.slf4j.Logger;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SetInterestedBlockTarget<E extends Mob> extends ExtendedBehaviour<E> {
    private static final Logger LOGGER = LogUtils.getLogger();

    private final float speed;
    private final int arriveDistance;
    private final int repathInterval;
    private final boolean isEnabled;

    private long nextSetTick = 0;

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
    protected void start(ServerLevel level, E entity, long gameTime) {
        if (gameTime < nextSetTick) return;
        nextSetTick = gameTime + repathInterval;

        var brain = entity.getBrain();

        MemoryModuleType<BlockPos> interestedType = ModMemoryTypes.INTERESTED_BLOCK_TARGET.get();

        BlockPos target = brain.getMemory(interestedType).orElse(null);
        if (target == null) {
            LOGGER.debug("[YAH:R] [BEHAVIOR:{}][{}] variable \"target\" is null, return",
                    this.getClass().getSimpleName(), entity.getUUID());
            return;
        }

        BlockPos walkPos = getWalkablePos(level, target);
        if (walkPos == null) {
            LOGGER.debug("[YAH:R] [BEHAVIOR:{}][{}] variable \"walkPos\" is null, return",
                    this.getClass().getSimpleName(), entity.getUUID());
            return;
        }

        if (entity.blockPosition().closerThan(walkPos, arriveDistance)) {
            LOGGER.debug("[YAH:R] [BEHAVIOR:{}][{}] arrived at {} and added {} -> VISITED_BLOCKS",
                    this.getClass().getSimpleName(), entity.getUUID(), walkPos, target);

            Set<BlockPos> visited = brain.getMemory(ModMemoryTypes.VISITED_BLOCKS.get()).orElseGet(HashSet::new);

            visited.add(walkPos.immutable());
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
            brain.eraseMemory(MemoryModuleType.WALK_TARGET);
            brain.eraseMemory(MemoryModuleType.LOOK_TARGET);
            return;
        }

        System.out.println("[YAH:R STEVE-DEBUG] Steve set his walk target to " + walkPos);
        brain.setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(walkPos, speed, 1));
        LOGGER.debug("[YAH:R] [BEHAVIOR:{}][{}] set WALK_TARGET -> {}",
                this.getClass().getSimpleName(), entity.getUUID(), walkPos);

        brain.setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(walkPos));
        LOGGER.debug("[YAH:R] [BEHAVIOR:{}][{}] set LOOK_TARGET -> {}",
                this.getClass().getSimpleName(), entity.getUUID(), walkPos);
    }

    @Override
    protected void stop(E entity) {
        entity.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);
        LOGGER.debug("[YAH:R] [BEHAVIOR:{}][{}] stopped",
                this.getClass().getSimpleName(), entity.getUUID());
    }
}
