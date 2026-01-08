package net.notccg.yahresurrected.entity.custom.logic.behaviors;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.behavior.PositionTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.level.chunk.SingleValuePalette;
import net.minecraft.world.phys.Vec3;
import net.notccg.yahresurrected.util.ModMemoryTypes;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;

import java.util.List;

public class FleeOrInvestigateBehaviour<E extends PathfinderMob> extends ExtendedBehaviour<E> {
    private final float investigateSpeed;
    private final int arriveDistance;
    private final int repathInterval;

    private long nextTick = 0;

    private long lastProcessedSoundTime = Long.MIN_VALUE;

    public FleeOrInvestigateBehaviour(int arriveDistance, int repathInterval, float investigateSpeed) {
        this.investigateSpeed = investigateSpeed;
        this.arriveDistance = arriveDistance;
        this.repathInterval = repathInterval;
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return ObjectArrayList.of(
                Pair.of(ModMemoryTypes.INVESTIGATE_TARGET.get(), MemoryStatus.REGISTERED),
                Pair.of(ModMemoryTypes.HEARD_SOUND_POS.get(), MemoryStatus.VALUE_PRESENT),
                Pair.of(ModMemoryTypes.LAST_HEARD_TIME.get(), MemoryStatus.VALUE_PRESENT),
                Pair.of(ModMemoryTypes.CURIOSITY_LEVEL.get(), MemoryStatus.REGISTERED),
                Pair.of(ModMemoryTypes.FEAR_LEVEL.get(), MemoryStatus.REGISTERED),
                Pair.of(ModMemoryTypes.PARANOIA_LEVEL.get(), MemoryStatus.REGISTERED),
                Pair.of(MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED),
                Pair.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED)
        );
    }

    @Override
    protected void start(ServerLevel level, E entity, long gameTime) {
        nextTick = 0;

        if (gameTime < nextTick) return;

        nextTick = gameTime + repathInterval;

        var brain = entity.getBrain();

        Long heardTime = brain.getMemory(ModMemoryTypes.LAST_HEARD_TIME.get()).get();
        Vec3 heardPos = brain.getMemory(ModMemoryTypes.HEARD_SOUND_POS.get()).get();

        if (heardTime > lastProcessedSoundTime) {
            lastProcessedSoundTime = heardTime;

            BlockPos targetPos = BlockPos.containing(heardPos);
            BlockPos lookPos = targetPos.above();

            brain.setMemory(ModMemoryTypes.INVESTIGATE_TARGET.get(), targetPos);
            brain.setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(targetPos, investigateSpeed, arriveDistance));
        } else {
            BlockPos current = brain.getMemory(ModMemoryTypes.INVESTIGATE_TARGET.get()).orElse(null);
            if (current == null) return;

            BlockPos lookCurrent = current.above();

            if (entity.blockPosition().closerThan(current, arriveDistance)) {
                brain.eraseMemory(ModMemoryTypes.INVESTIGATE_TARGET.get());
                brain.eraseMemory(ModMemoryTypes.LAST_HEARD_TIME.get());
                brain.eraseMemory(MemoryModuleType.WALK_TARGET);
                brain.eraseMemory(MemoryModuleType.LOOK_TARGET);
                return;
            }
            brain.setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(current, investigateSpeed, arriveDistance));
            brain.setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(lookCurrent));
        }
    }

    @Override
    protected void stop(ServerLevel level, E entity, long gameTime) {
        Brain<?> brain = entity.getBrain();

        brain.eraseMemory(ModMemoryTypes.HEARD_SOUND_POS.get());
        brain.eraseMemory(ModMemoryTypes.INVESTIGATE_TARGET.get());
        brain.eraseMemory(ModMemoryTypes.LAST_HEARD_TIME.get());
        brain.eraseMemory(MemoryModuleType.WALK_TARGET);
        brain.eraseMemory(MemoryModuleType.LOOK_TARGET);
    }
}
