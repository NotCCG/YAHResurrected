package net.notccg.yahresurrected.entity.custom.logic.behaviors;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;
import net.notccg.yahresurrected.util.ModMemoryTypes;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;

import java.util.List;

public class SteveWander<E extends PathfinderMob> extends ExtendedBehaviour<E> {
    private static final int MIN_REPATH_TICKS = 10;
    private static final int MAX_REPATH_TICKS = 1200;
    private final float speed;
    private final int arriveDistance;
    private final int wanderHorizontal;
    private final int wanderVertical;


    private long nextWanderTick = 0;

    public SteveWander(float speed, int arriveDistance, int wanderHorizonal, int wanderVertical) {
        this.speed = speed;
        this.arriveDistance = arriveDistance;
        this.wanderHorizontal = wanderHorizonal;
        this.wanderVertical = wanderVertical;
    }



    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return ObjectArrayList.of(
                Pair.of(ModMemoryTypes.SPOTTED_PLAYER.get(), MemoryStatus.VALUE_ABSENT),
                Pair.of(ModMemoryTypes.INTERESTED_BLOCK_TARGET.get(), MemoryStatus.VALUE_ABSENT),
                Pair.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT),
                Pair.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED)
        );
    }

    @Override
    protected void start(ServerLevel level, E entity, long gameTime) {
        var brain = entity.getBrain();
        if (gameTime < nextWanderTick) return;

        Vec3 randomPos = DefaultRandomPos.getPos(entity, wanderHorizontal, wanderVertical);
        if (randomPos == null) return;

        int nextInterval = entity.getRandom().nextInt(
                MAX_REPATH_TICKS - MIN_REPATH_TICKS + 1
        ) + MIN_REPATH_TICKS;
        nextWanderTick = gameTime + nextInterval;

        brain.eraseMemory(MemoryModuleType.LOOK_TARGET);

        WalkTarget walkTarget = new WalkTarget(randomPos, speed, arriveDistance);

        BlockPos walkPos = walkTarget.getTarget().currentBlockPosition();
        BlockPos lookPos = walkPos.above();

        brain.setMemory(MemoryModuleType.WALK_TARGET, walkTarget);
        brain.setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(lookPos));
    }

    @Override
    protected void stop(ServerLevel level, E entity, long gameTime) {
        entity.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
    }
}
