package net.notccg.yahresurrected.entity.custom.logic.behaviors;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
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

    private static final int COOLDOWN_AFTER_WAKING_UP = 100;

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected void start(ServerLevel level, E entity, long gameTime) {
        Brain<?> brain = entity.getBrain();
        if (SteveLogic.isUneasy(brain) || SteveLogic.isScared(brain) || SteveLogic.isTerrified(brain)) return;
        if (gameTime > this.nextOkSleepTime) {

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
