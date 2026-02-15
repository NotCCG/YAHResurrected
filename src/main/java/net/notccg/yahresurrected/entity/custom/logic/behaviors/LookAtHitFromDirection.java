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
import net.notccg.yahresurrected.util.ModMemoryTypes;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import org.slf4j.Logger;

import java.util.List;

public class LookAtHitFromDirection<E extends PathfinderMob> extends ExtendedBehaviour<E> {
    private static final Logger LOGGER = LogUtils.getLogger();
    private final boolean isEnabled;
    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORIES = ObjectArrayList.of(
            Pair.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED),
            Pair.of(ModMemoryTypes.SPOTTED_PLAYER.get(), MemoryStatus.VALUE_ABSENT),
            Pair.of(ModMemoryTypes.LAST_HURT_BY.get(), MemoryStatus.VALUE_PRESENT),
            Pair.of(ModMemoryTypes.PLAYER_HURT.get(), MemoryStatus.VALUE_PRESENT),
            Pair.of(ModMemoryTypes.PLAYER_HIT_POS.get(), MemoryStatus.VALUE_PRESENT)
    );

    public LookAtHitFromDirection(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORIES;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        return isEnabled;
    }

    @Override
    protected void start(ServerLevel level, E entity, long gameTime) {
        Brain<?> brain = entity.getBrain();

        BlockPos hitPos = brain.getMemory(ModMemoryTypes.PLAYER_HIT_POS.get()).orElse(null);
        if (hitPos == null) {
            LOGGER.debug("[YAH:R] [BEHAVIOR:{}][{}] variable \"hitPos\" is null, returning", this.getClass().getSimpleName(), entity.getUUID());
            return;
        }
        BlockPos lookPos = hitPos.above();

        brain.setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(lookPos));
        LOGGER.debug("[YAH:R] [BEHAVIOR:{}][{}] set LOOK_TARGET -> {}", this.getClass().getSimpleName(), entity.getUUID(), lookPos);
        brain.eraseMemory(ModMemoryTypes.PLAYER_HIT_POS.get());
        LOGGER.debug("[YAH:R] [BEHAVIOR:{}][{}] erased PLAYER_HIT_POS", this.getClass().getSimpleName(), entity.getUUID());
    }
}