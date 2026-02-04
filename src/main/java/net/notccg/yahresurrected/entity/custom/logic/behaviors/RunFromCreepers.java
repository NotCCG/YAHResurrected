package net.notccg.yahresurrected.entity.custom.logic.behaviors;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.phys.Vec3;
import net.notccg.yahresurrected.util.ModMemoryTypes;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;

import java.util.List;

public class RunFromCreepers<E extends PathfinderMob> extends ExtendedBehaviour<E> {
    private final long updateTicks;

    private long nextUpdateTick = 0;

    private final float speed = 1.3F;
    private final int fleeHorizontal = 16;
    private final int fleeVertical = 8;

    public RunFromCreepers(long updateTicks) {
        this.updateTicks = updateTicks;
    }

    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORIES = ObjectArrayList.of(
            Pair.of(MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED),
            Pair.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED),
            Pair.of(ModMemoryTypes.SPOTTED_PLAYER.get(), MemoryStatus.VALUE_ABSENT),
            Pair.of(ModMemoryTypes.NEARBY_CREEPERS.get(), MemoryStatus.VALUE_PRESENT)
    );

    private static BlockPos getWalkablePos(ServerLevel level, BlockPos target) {
        if (!level.getBlockState(target).getCollisionShape(level, target).isEmpty()) {
            return target.above();
        }
        return target;
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORIES;
    }

    @Override
    protected void start(ServerLevel level, E entity, long gameTime) {
        if (gameTime < nextUpdateTick) return;
        nextUpdateTick = gameTime + updateTicks;

        Brain<?> brain = entity.getBrain();
        Creeper creeper = brain.getMemory(ModMemoryTypes.NEARBY_CREEPERS.get()).orElse(null);

        if (creeper == null) return;

        Vec3 awayPos = DefaultRandomPos.getPosAway(
                entity,
                fleeHorizontal,
                fleeVertical,
                creeper.position()
        );

        if (awayPos == null) return;

        BlockPos pos = BlockPos.containing(awayPos.x, awayPos.y, awayPos.z);
        BlockPos reachablePos = getWalkablePos(level, pos);
        WalkTarget walkTarget = new WalkTarget(reachablePos, speed, 0);
        BlockPos lookPos = reachablePos.above();

        brain.setMemory(MemoryModuleType.WALK_TARGET, walkTarget);
        brain.setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(lookPos));
    }

    @Override
    protected void stop(ServerLevel level, E entity, long gameTime) {
        entity.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        entity.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);
    }
}
