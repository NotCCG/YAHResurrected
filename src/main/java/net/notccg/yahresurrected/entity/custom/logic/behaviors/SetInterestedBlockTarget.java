package net.notccg.yahresurrected.entity.custom.logic.behaviors;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.memory.*;
import net.notccg.yahresurrected.util.ModMemoryTypes;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SetInterestedBlockTarget<E extends Mob> extends ExtendedBehaviour<E> {
    private final float speed;
    private final int arriveDistance;
    private final int repathInterval;

    private long nextSetTick = 0;

    public SetInterestedBlockTarget(float speed, int arriveDistance, int repathInterval) {
        this.speed = speed;
        this.arriveDistance = arriveDistance;
        this.repathInterval = repathInterval;
    }

    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS =
            ObjectArrayList.of(
                    Pair.of(ModMemoryTypes.INTERESTED_BLOCK_TARGET.get(), MemoryStatus.VALUE_PRESENT)
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
    protected void start(ServerLevel level, E entity, long gameTime) {
        nextSetTick = 0;
    }

    @Override
    protected void tick(ServerLevel level, E entity, long gameTime) {
        System.out.println("tick walk target");
        var brain = entity.getBrain();
        if (gameTime < nextSetTick) return;

        nextSetTick = gameTime + repathInterval;

        MemoryModuleType<BlockPos> interestedType = ModMemoryTypes.INTERESTED_BLOCK_TARGET.get();
        BlockPos target = brain.getMemory(interestedType).orElse(null);

        if (target == null) return;

        if (entity.blockPosition().closerThan(target, arriveDistance)) {
            Set<BlockPos> visited = brain.getMemory(ModMemoryTypes.VISITED_BLOCKS.get()).orElseGet(HashSet::new);
            visited.add(target.immutable());
            brain.setMemory(ModMemoryTypes.VISITED_BLOCKS.get(), new HashSet<>(visited));

            brain.eraseMemory(ModMemoryTypes.INTERESTED_BLOCK_TARGET.get());
            brain.eraseMemory(MemoryModuleType.WALK_TARGET);
            brain.eraseMemory(MemoryModuleType.LOOK_TARGET);
            return;
        }

        BlockPos walkPos = getWalkablePos(level, target);

        brain.setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(walkPos, speed, arriveDistance));
        System.out.println("WALK_TARGET after set = " + brain.getMemory(MemoryModuleType.WALK_TARGET).orElse(null));
    }
}
