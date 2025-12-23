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
    private final float speed;
    private final int arriveDistance;
    private final int repathInterval;
    private final int wanderHorizontal;
    private final int wanderVertical;


    private long nextWonderTick = 0;

    public SteveWander(float speed, int arriveDistance, int repathInterval, int wanderHorizonal, int wanderVertical) {
        this.speed = speed;
        this.arriveDistance = arriveDistance;
        this.repathInterval = repathInterval;
        this.wanderHorizontal = wanderHorizonal;
        this.wanderVertical = wanderVertical;
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return ObjectArrayList.of(
                Pair.of(ModMemoryTypes.SPOTTED_PLAYER.get(), MemoryStatus.VALUE_ABSENT),
                Pair.of(ModMemoryTypes.INTERESTED_BLOCK_TARGET.get(), MemoryStatus.VALUE_ABSENT),
                Pair.of(MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED),
                Pair.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED)
        );
    }

    @Override
    protected void start(ServerLevel level, E entity, long gameTime) {
        var brain = entity.getBrain();
        Vec3 randomPos = DefaultRandomPos.getPosAway(entity, wanderHorizontal, wanderVertical, entity.position());

        if (gameTime < nextWonderTick) return;
        nextWonderTick = gameTime + repathInterval;

        // Don't continue if there is already a target to walk to
        if (brain.getMemory(MemoryModuleType.WALK_TARGET).isPresent()) return;
        if (brain.getMemory(MemoryModuleType.LOOK_TARGET).isPresent()) {
            brain.eraseMemory(MemoryModuleType.LOOK_TARGET);
        }
        WalkTarget walkTarget = new WalkTarget(randomPos, speed, arriveDistance);
        BlockPos walkPos = walkTarget.getTarget().currentBlockPosition();
        BlockPos lookPos = walkPos.above();

        brain.setMemory(MemoryModuleType.WALK_TARGET, walkTarget);
        brain.setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(lookPos));
    }

    @Override
    protected void stop(ServerLevel level, E entity, long gameTime) {
        entity.getNavigation().stop();
    }
}
