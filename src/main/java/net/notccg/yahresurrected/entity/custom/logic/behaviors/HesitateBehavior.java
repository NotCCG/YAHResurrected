package net.notccg.yahresurrected.entity.custom.logic.behaviors;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.notccg.yahresurrected.util.ModMemoryTypes;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;

import java.util.List;

public class HesitateBehavior<E extends Mob> extends ExtendedBehaviour<E> {
    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return ObjectArrayList.of(Pair.of(ModMemoryTypes.SPOTTED_PLAYER.get(), MemoryStatus.VALUE_PRESENT),
                Pair.of(ModMemoryTypes.HESITATION.get(), MemoryStatus.VALUE_PRESENT)
        );
    }

    @Override
    protected void tick(ServerLevel level, E entity, long gameTime) {
        long until = entity.getBrain()
                .getMemory(ModMemoryTypes.HESITATION.get())
                .orElse(0L);

        if (gameTime < until) {
            entity.getNavigation().stop();
        } else {
            entity.getBrain().eraseMemory(ModMemoryTypes.HESITATION.get());
        }
    }
}
