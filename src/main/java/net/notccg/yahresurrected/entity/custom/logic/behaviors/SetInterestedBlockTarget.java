package net.notccg.yahresurrected.entity.custom.logic.behaviors;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
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
    protected void start(ServerLevel level, E entity, long gameTime) {
        var brain = entity.getBrain();
        if (gameTime < nextSetTick) return;

        nextSetTick = gameTime + repathInterval;

        MemoryModuleType<BlockPos> interestedType = ModMemoryTypes.INTERESTED_BLOCK_TARGET.get();

        BlockPos target = brain.getMemory(interestedType).orElse(null);
        if (target == null) return;

        BlockPos walkPos = getWalkablePos(level, target);
        if (walkPos == null) return;

        if (entity.blockPosition().closerThan(walkPos, arriveDistance)) {
            System.out.println("steve visited his block");
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

        System.out.println("steve set his walk target to " + walkPos);
        brain.setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(walkPos, speed, 1));
        brain.setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(walkPos));
    }

    @Override
    protected void stop(E entity) {
        entity.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);
    }
}
