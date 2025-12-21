package net.notccg.yahresurrected.entity.custom.logic.behaviors;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.notccg.yahresurrected.entity.custom.logic.steve_ai.SteveLogic;
import net.notccg.yahresurrected.util.ModMemoryTypes;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;

import java.util.List;

public class HearingCuriosityIncreaseBehavior<E extends Mob> extends ExtendedBehaviour<E> {
    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return ObjectArrayList.of(
                Pair.of(ModMemoryTypes.HEARD_PLAYER.get(), MemoryStatus.VALUE_PRESENT),
                Pair.of(ModMemoryTypes.CURIOSITY_LEVEL.get(), MemoryStatus.REGISTERED)
        );
    }

    private final double amount;
    private final int intervalTicks;

    private long nextGainTick = 0;

    public HearingCuriosityIncreaseBehavior(double amount, int intervalTicks) {
        this.amount = amount;
        this.intervalTicks = intervalTicks;
    }

    @Override
    protected void tick(ServerLevel level, E entity, long gameTime) {
        if (gameTime < nextGainTick) return;
        nextGainTick = gameTime + intervalTicks;

        Brain<?> brain = entity.getBrain();
        double cur = brain.getMemory(ModMemoryTypes.CURIOSITY_LEVEL.get()).orElse(0.0);
        cur = SteveLogic.clampEmotion(cur + amount);
        brain.setMemory(ModMemoryTypes.CURIOSITY_LEVEL.get(), cur);
    }
}
