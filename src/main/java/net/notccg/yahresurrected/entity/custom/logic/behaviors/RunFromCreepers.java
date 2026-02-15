package net.notccg.yahresurrected.entity.custom.logic.behaviors;

import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.phys.Vec3;
import net.notccg.yahresurrected.util.ModMemoryTypes;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import org.slf4j.Logger;

import java.util.List;

public class RunFromCreepers<E extends PathfinderMob> extends ExtendedBehaviour<E> {
    private static final Logger LOGGER = LogUtils.getLogger();
    private final long updateTicks;

    private long nextUpdateTick = 0;

    private final float speed = 1.3F;

    public RunFromCreepers(long updateTicks) {
        this.updateTicks = updateTicks;
    }

    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORIES = ObjectArrayList.of(
            Pair.of(MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED),
            Pair.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED),
            Pair.of(ModMemoryTypes.SPOTTED_PLAYER.get(), MemoryStatus.VALUE_ABSENT),
            Pair.of(ModMemoryTypes.NEARBY_CREEPERS.get(), MemoryStatus.VALUE_PRESENT)
    );

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

        if (creeper == null) {
            LOGGER.debug("[YAH:R] [BEHAVIOR:{}][{}] variable \"creeper\" is null, return",
                    this.getClass().getSimpleName(), entity.getUUID());
            return;
        }

        Vec3 awayPos = entity.position().subtract(creeper.position()).normalize();
        Vec3 runPos = entity.position().add(awayPos.scale(16));

        BlockPos lookPos = BlockPos.containing(runPos).above();

        brain.setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(runPos, speed, 1));
        LOGGER.debug("[YAH:R] [BEHAVIOR:{}][{}] set WALK_TARGET -> {}",
                this.getClass().getSimpleName(), entity.getUUID(), runPos);

        brain.setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(lookPos));
        LOGGER.debug("[YAH:R] [BEHAVIOR:{}][{}] set LOOK_TARGET -> {}",
                this.getClass().getSimpleName(), entity.getUUID(), lookPos);
    }
}
