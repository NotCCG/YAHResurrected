package net.notccg.yahresurrected.entity.custom.logic.behaviors;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.notccg.yahresurrected.util.ModMemoryTypes;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WalkToInterestedBlock<E extends Mob> extends ExtendedBehaviour<E> {

    private final float speed;
    private final double arriveDistance;

    // Constructor defines them
    public WalkToInterestedBlock(float speed, double arriveDistance) {
        this.speed = speed;
        this.arriveDistance = arriveDistance;
    }

    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS =
            ObjectArrayList.of(
                    Pair.of(ModMemoryTypes.INTERESTED_BLOCK_TARGET.get(), MemoryStatus.VALUE_PRESENT),
                    Pair.of(ModMemoryTypes.VISITED_BLOCKS.get(), MemoryStatus.REGISTERED)
            );

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected void start(ServerLevel level, E entity, long gameTime) {
        Brain<?> brain = entity.getBrain();

        BlockPos target = brain.getMemory(ModMemoryTypes.INTERESTED_BLOCK_TARGET.get()).orElse(null);
        if (target == null) return;

        entity.getNavigation().moveTo(
                target.getX() + 0.5,
                target.getY(),
                target.getZ() + 0.5,
                this.speed
        );
    }

    @Override
    protected void tick(ServerLevel level, E entity, long gameTime) {
        Brain<?> brain = entity.getBrain();

        BlockPos target = brain.getMemory(ModMemoryTypes.INTERESTED_BLOCK_TARGET.get()).orElse(null);
        if (target == null) return;

        if (entity.blockPosition().closerThan(target, this.arriveDistance)) {
            Set<BlockPos> visited = brain
                    .getMemory(ModMemoryTypes.VISITED_BLOCKS.get())
                    .orElseGet(HashSet::new);

            visited.add(target.immutable());

            brain.setMemory(ModMemoryTypes.VISITED_BLOCKS.get(), visited);
            brain.eraseMemory(ModMemoryTypes.INTERESTED_BLOCK_TARGET.get());

            entity.getNavigation().stop();
        }
    }

    @Override
    protected void stop(ServerLevel level, E entity, long gameTime) {
        entity.getNavigation().stop();
    }
}
