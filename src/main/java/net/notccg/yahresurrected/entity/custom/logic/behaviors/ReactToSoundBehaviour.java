package net.notccg.yahresurrected.entity.custom.logic.behaviors;

import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;
import net.notccg.yahresurrected.entity.custom.logic.steve_ai.HeardSoundType;
import net.notccg.yahresurrected.entity.custom.logic.steve_ai.SteveLogic;
import net.notccg.yahresurrected.util.ModMemoryTypes;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import org.slf4j.Logger;

import java.util.List;

public class ReactToSoundBehaviour<E extends PathfinderMob> extends ExtendedBehaviour<E> {
    private static final Logger LOGGER = LogUtils.getLogger();
    private final float baseInvestigateSpeed;
    private final int arriveDistance;
    private final int repathInterval;

    private final boolean isEnabled;

    private long nextTick = 0;
    private final int fleeVertical = 8;
    private final int fleeHorizontal = 24;

    private long lastProcessedSoundTime = Long.MIN_VALUE;

    public ReactToSoundBehaviour(int arriveDistance, int repathInterval, float investigateSpeed, boolean isEnabled) {
        this.baseInvestigateSpeed = investigateSpeed;
        this.arriveDistance = arriveDistance;
        this.repathInterval = repathInterval;
        this.isEnabled = isEnabled;
    }

    private static boolean shouldFlee(Brain<?> brain, long gameTime) {
        return SteveLogic.isTerrified(brain, gameTime) ||
                SteveLogic.isParanoid(brain, gameTime) ||
                SteveLogic.isVeryParanoid(brain, gameTime) ||
                brain.hasMemoryValue(ModMemoryTypes.PLAYER_HURT.get());
    };

    private static boolean shouldCautiouslyInvestigate(Brain<?> brain, long gameTime) {
        return SteveLogic.isCautious(brain, gameTime);
    };

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return ObjectArrayList.of(
                Pair.of(ModMemoryTypes.SPOTTED_PLAYER.get(), MemoryStatus.REGISTERED),
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

        Long heardTime = brain.getMemory(ModMemoryTypes.LAST_HEARD_TIME.get()).orElse(null);
        Vec3 heardPos = brain.getMemory(ModMemoryTypes.HEARD_SOUND_POS.get()).orElse(null);

        // A catch for any wackiness
        if (heardTime == null || heardPos == null)
            return;

        if (heardTime > lastProcessedSoundTime) {
            lastProcessedSoundTime = heardTime;
            entity.setPose(Pose.STANDING);
            BlockPos targetPos = BlockPos.containing(heardPos);

            HeardSoundType heardSoundType = brain.getMemory(ModMemoryTypes.HEARD_SOUND_TYPE.get()).orElse(null);
            if (heardSoundType != null) {
                if (heardSoundType == HeardSoundType.CONTAINER_OPEN || heardSoundType == HeardSoundType.CONTAINER_CLOSE) {
                    SteveLogic.addCuriosity(brain, gameTime, 0.1f);
                }
                if (heardSoundType == HeardSoundType.FOOTSTEPS) {
                    SteveLogic.addCuriosity(brain, gameTime, 0.25f);
                    SteveLogic.addFear(brain, gameTime, .1f);
                }
                if (heardSoundType == HeardSoundType.BLOCK_PLACE || heardSoundType == HeardSoundType.BLOCK_BREAK) {
                    SteveLogic.addCuriosity(brain, gameTime, 0.1f);
                    SteveLogic.addFear(brain, gameTime, .01f);
                }
            };

            if (shouldFlee(brain, gameTime)) {
                float fleeSpeed = baseInvestigateSpeed * 1.3f;
                Vec3 fleePos = DefaultRandomPos.getPosAway(
                        entity,
                        fleeHorizontal,
                        fleeVertical,
                        heardPos
                );
                if (fleePos == null) return;
                BlockPos fleeLookPos = BlockPos.containing(fleePos).above();
                brain.setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(fleePos, fleeSpeed, arriveDistance));
                brain.setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(fleeLookPos));
            } else {
                BlockPos lookPos = targetPos.above();

                float investigateSpeed = baseInvestigateSpeed;

                if (shouldCautiouslyInvestigate(brain, gameTime)) {
                    entity.setPose(Pose.CROUCHING);
                    investigateSpeed = baseInvestigateSpeed * 1.3f;
                }
                brain.setMemory(ModMemoryTypes.INVESTIGATE_TARGET.get(), targetPos);
                brain.setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(targetPos, investigateSpeed, arriveDistance));
                brain.setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(lookPos));
            }
        }
    }

    @Override
    protected void stop(ServerLevel level, E entity, long gameTime) {
        Brain<?> brain = entity.getBrain();
        entity.setPose(Pose.STANDING);

        brain.eraseMemory(ModMemoryTypes.HEARD_SOUND_POS.get());
        brain.eraseMemory(ModMemoryTypes.INVESTIGATE_TARGET.get());
        brain.eraseMemory(ModMemoryTypes.LAST_HEARD_TIME.get());
    }
}
