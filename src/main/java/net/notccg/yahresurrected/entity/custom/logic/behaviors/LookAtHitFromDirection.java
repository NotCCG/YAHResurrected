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
import net.notccg.yahresurrected.util.ModMemoryTypes;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;

import java.util.List;
import java.util.function.BooleanSupplier;

public class LookAtHitFromDirection<E extends PathfinderMob> extends ExtendedBehaviour<E> {
    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORIES = ObjectArrayList.of(
            Pair.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED),
            Pair.of(ModMemoryTypes.SPOTTED_PLAYER.get(), MemoryStatus.VALUE_ABSENT),
            Pair.of(ModMemoryTypes.LAST_HURT_BY.get(), MemoryStatus.VALUE_PRESENT),
            Pair.of(ModMemoryTypes.PLAYER_HURT.get(), MemoryStatus.VALUE_PRESENT),
            Pair.of(ModMemoryTypes.PLAYER_HIT_POS.get(), MemoryStatus.VALUE_PRESENT)
    );

    public LookAtHitFromDirection() {
    }

    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORIES;
    }

    @Override
    protected void start(ServerLevel level, E entity, long gameTime) {
        Brain<?> brain = entity.getBrain();

        BlockPos hitPos = brain.getMemory(ModMemoryTypes.PLAYER_HIT_POS.get()).orElse(null);
        if (hitPos == null) return;
        BlockPos lookPos = hitPos.above();

        brain.setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(lookPos));
        brain.eraseMemory(ModMemoryTypes.PLAYER_HIT_POS.get());
    }

    @Override
    protected void stop(E entity) {
        entity.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);
    }
}