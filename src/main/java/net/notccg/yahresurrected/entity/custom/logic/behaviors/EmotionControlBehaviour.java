package net.notccg.yahresurrected.entity.custom.logic.behaviors;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.notccg.yahresurrected.entity.custom.logic.steve_ai.SteveLogic;
import net.notccg.yahresurrected.util.ModMemoryTypes;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;

import java.util.List;

public class EmotionControlBehaviour<E extends PathfinderMob> extends ExtendedBehaviour<E> {
    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORIES =
            ObjectArrayList.of(
                    Pair.of(ModMemoryTypes.CURIOSITY_LEVEL.get(), MemoryStatus.REGISTERED),
                    Pair.of(ModMemoryTypes.FEAR_LEVEL.get(), MemoryStatus.REGISTERED),
                    Pair.of(ModMemoryTypes.PARANOIA_LEVEL.get(), MemoryStatus.REGISTERED),
                    Pair.of(ModMemoryTypes.HEARD_SOUND_TYPE.get(), MemoryStatus.REGISTERED),
                    Pair.of(ModMemoryTypes.LAST_HEARD_TIME.get(), MemoryStatus.REGISTERED),
                    Pair.of(ModMemoryTypes.SPOTTED_PLAYER.get(), MemoryStatus.REGISTERED)
            );

    private final long updateInterval;

    private long nextUpdateTick = 0;

    EmotionControlBehaviour(long updateInterval) {
        this.updateInterval = updateInterval;
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORIES;
    }

    @Override
    protected void tick(ServerLevel level, E entity, long gameTime) {
        if (gameTime < nextUpdateTick) return;
        nextUpdateTick = gameTime + updateInterval;

        var brain = entity.getBrain();
        double fear = SteveLogic.getFear(brain);
        double curiosity = SteveLogic.getCuriosity(brain);
        double paranoia = SteveLogic.getParanoia(brain);
    }
}
