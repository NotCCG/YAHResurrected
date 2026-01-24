package net.notccg.yahresurrected.entity.custom.logic.behaviors;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.notccg.yahresurrected.entity.custom.logic.steve_ai.SteveLogic;
import net.notccg.yahresurrected.util.ModMemoryTypes;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;

import java.util.List;

public class LookAtSpottedPlayer <E extends Mob> extends ExtendedBehaviour<E> {
    private final int updateIntervalTicks;
    private int fearUpdateInterval;
    private long nextUpdateTick = 0;

    public LookAtSpottedPlayer(int updateIntervalTicks, int fearUpdateInterval) {
        this.updateIntervalTicks = updateIntervalTicks;
        this.fearUpdateInterval = fearUpdateInterval;
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return ObjectArrayList.of(
                Pair.of(ModMemoryTypes.SPOTTED_PLAYER.get(), MemoryStatus.VALUE_PRESENT),
                Pair.of(ModMemoryTypes.PLAYER_HURT.get(), MemoryStatus.REGISTERED),
                Pair.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED),
                Pair.of(MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED)
        );
    }

    @Override
    protected void start(ServerLevel level, E entity, long gameTime) {
        nextUpdateTick = 0;
        if (gameTime < nextUpdateTick)
            return;

        nextUpdateTick = gameTime + updateIntervalTicks;

        var brain = entity.getBrain();

        Player player = brain.getMemory(ModMemoryTypes.SPOTTED_PLAYER.get()).orElse(null);
        if (player == null || !player.isAlive() || player.isSpectator()) {
            brain.eraseMemory(ModMemoryTypes.SPOTTED_PLAYER.get());
            brain.eraseMemory(MemoryModuleType.LOOK_TARGET);
            return;
        }
        boolean hasBeenHurtByPlayer = brain.hasMemoryValue(ModMemoryTypes.PLAYER_HURT.get());
        if (hasBeenHurtByPlayer) {
            double fear = SteveLogic.getFear(brain);
            SteveLogic.addFear(brain, 0.0005);
        }
        brain.setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(player, true));
        brain.eraseMemory(MemoryModuleType.WALK_TARGET);
    }

    @Override
    protected void stop(ServerLevel level, E entity, long gameTime) {
        entity.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);
    }

}
