package net.notccg.yahresurrected.entity.custom.logic.sensors;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.notccg.yahresurrected.util.ModMemoryTypes;
import net.notccg.yahresurrected.util.ModSensorTypes;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;

import java.util.Comparator;
import java.util.List;

public class SpotPlayerSensor<E extends PathfinderMob> extends ExtendedSensor<E> {
    private static final double SIGHT_RANGE = 16.0;
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
        long now = level.getGameTime();
        if (now < nextScanTick)
            return;

        nextScanTick = now + SCAN_INTERVAL_TICKS;

        var brain = entity.getBrain();

        // Detect players
        AABB box = entity.getBoundingBox().inflate(SIGHT_RANGE, 0.0, SIGHT_RANGE);

        Player visibleNearest = level.getEntitiesOfClass(Player.class, box,
                p -> !p.isSpectator()
                            && p.isAlive()
                            && !p.isCreative()
                            && entity.hasLineOfSight(p)
                ).stream()
                .min(Comparator.comparingDouble(entity::distanceToSqr))
                .orElse(null);
        if (visibleNearest == null) {
            brain.eraseMemory(ModMemoryTypes.SPOTTED_PLAYER.get());
            return;
        }

        brain.setMemory(ModMemoryTypes.SPOTTED_PLAYER.get(), visibleNearest);
    }
}
