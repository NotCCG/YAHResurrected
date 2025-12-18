package net.notccg.yahresurrected.entity.custom.logic.sensors;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.player.Player;
import net.notccg.yahresurrected.util.ModMemoryTypes;
import net.notccg.yahresurrected.util.ModSensorTypes;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;

import java.util.List;

public class SpotPlayerSensor<E extends PathfinderMob> extends ExtendedSensor<E> {
    private static final double RANGE = 16.0;
    private static final int SCAN_INTERVAL_TICKS = 30; // twice per one and a half seconds

    private long nextScanTick = 0;

    @Override
    public List<MemoryModuleType<?>> memoriesUsed() {
        return ObjectArrayList.of(
                ModMemoryTypes.SPOTTED_PLAYER.get()
        );
    }

    @Override
    public SensorType<? extends ExtendedSensor<?>> type() {
        return ModSensorTypes.SPOT_PLAYER_SENSOR.get();
    }

    @Override
    protected void doTick(ServerLevel level, E entity) {
        Brain<?> brain = entity.getBrain();
        long gameTime = level.getGameTime();

        if (!brain.hasMemoryValue(ModMemoryTypes.HESITATION.get())) {
            long hesitsteTicks = 5 + entity.getRandom().nextInt(6);
            brain.setMemory(ModMemoryTypes.HESITATION.get(), gameTime + hesitsteTicks);
        }

        if (gameTime < nextScanTick)
            return;

        nextScanTick = gameTime + SCAN_INTERVAL_TICKS;

        Player nearest = level.getNearestPlayer(entity, RANGE);
        if (nearest == null
                || nearest.isSpectator()
                || !entity.hasLineOfSight(nearest)) {

            brain.eraseMemory(ModMemoryTypes.SPOTTED_PLAYER.get());
            return;
        }

        brain.setMemory(ModMemoryTypes.SPOTTED_PLAYER.get(), nearest);
    }
}
