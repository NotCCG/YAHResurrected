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
import net.notccg.yahresurrected.entity.custom.logic.steve_ai.SteveLogic;
import net.notccg.yahresurrected.util.ModMemoryTypes;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;

import java.util.List;

// Much like with myself, this behaviour does not work yet
public class GoToSleepBehaviour<E extends PathfinderMob> extends ExtendedBehaviour<E> {
    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS =
            ObjectArrayList.of(
                    Pair.of(MemoryModuleType.NEAREST_BED, MemoryStatus.VALUE_PRESENT),
                    Pair.of(MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED),
                    Pair.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED),
                    Pair.of(ModMemoryTypes.FEAR_LEVEL.get(), MemoryStatus.REGISTERED),
                    Pair.of(ModMemoryTypes.INVESTIGATE_TARGET.get(), MemoryStatus.VALUE_ABSENT)
            );

    private long nextOkSleepTime;
    private final float walkSpeed;

    private static final int COOLDOWN_AFTER_WAKING_UP = 100;
    private static final int closeEnough = 1;

    public GoToSleepBehaviour(float walkSpeed) {
        this.walkSpeed = walkSpeed;
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
        return level.isDay();
    }

    @Override
    protected void start(ServerLevel level, E entity, long gameTime) {
        Brain<?> brain = entity.getBrain();
        if (SteveLogic.isUneasy(brain, gameTime) || SteveLogic.isScared(brain, gameTime) || SteveLogic.isTerrified(brain, gameTime)) return;
        if (gameTime > this.nextOkSleepTime) {
            MemoryModuleType<BlockPos> bedTarget = ModMemoryTypes.NEAREST_UNOCCUPIED_BED.get();
            BlockPos bedPos = brain.getMemory(bedTarget).orElse(null);
            BlockPos walkTarget = getWalkablePos(level, bedPos);
            if (bedPos == null) return;
            if (entity.blockPosition().closerThan(bedPos, closeEnough)) {
                entity.startSleeping(bedPos);
            }
            brain.setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(walkTarget, walkSpeed, closeEnough));
            brain.setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(bedPos));
        }

    }

    @Override
    protected void stop(ServerLevel level, E entity, long gameTime) {
        if (entity.isSleeping()) {
            entity.stopSleeping();
            this.nextOkSleepTime = gameTime + 40L;
        }
    }
}
