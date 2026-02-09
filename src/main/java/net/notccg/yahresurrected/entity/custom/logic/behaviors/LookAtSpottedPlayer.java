package net.notccg.yahresurrected.entity.custom.logic.behaviors;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.player.Player;
import net.notccg.yahresurrected.util.ModMemoryTypes;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;

import java.util.List;
import java.util.function.BooleanSupplier;

public class LookAtSpottedPlayer <E extends Mob> extends ExtendedBehaviour<E> {
    private final long updateIntervalTicks;
    private long nextUpdateTick = 0;


    public LookAtSpottedPlayer(int updateIntervalTicks) {
        this.updateIntervalTicks = updateIntervalTicks;
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return ObjectArrayList.of(
                Pair.of(ModMemoryTypes.SPOTTED_PLAYER.get(), MemoryStatus.VALUE_PRESENT),
                Pair.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED)
        );
    }

    @Override
    protected boolean shouldKeepRunning(E entity) {
        return entity.getBrain().hasMemoryValue(ModMemoryTypes.SPOTTED_PLAYER.get());
    }

    @Override
    protected void start(ServerLevel level, E entity, long gameTime) {
        System.out.println("[DEBUG] 'LookAtSpottedPlayer' start");
        this.nextUpdateTick = gameTime;
    }

    @Override
    protected void tick(ServerLevel level, E entity, long gameTime) {
        System.out.println("[DEBUG] Steve look tick");
        if (gameTime < nextUpdateTick)
            return;

        nextUpdateTick = gameTime + updateIntervalTicks;

        var brain = entity.getBrain();

        Player player = brain.getMemory(ModMemoryTypes.SPOTTED_PLAYER.get()).orElse(null);
        if (player == null) return;

        if (!player.isAlive() || player.isSpectator() || player.isCreative()) return;
        BlockPos lookPos = player.getOnPos().above().above();

        System.out.println("[DEBUG] looking at player");
        brain.setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(lookPos));
    }

    @Override
    protected void stop(ServerLevel level, E entity, long gameTime) {
        entity.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);
    }
}
