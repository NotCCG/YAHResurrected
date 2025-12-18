package net.notccg.yahresurrected.entity.custom.logic.sensors;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.notccg.yahresurrected.util.ModMemoryTypes;
import net.notccg.yahresurrected.util.ModSensorTypes;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;

import java.util.List;

public class PlayerSoundsSensor<E extends Mob> extends ExtendedSensor<E> {
    @Override
    public List<MemoryModuleType<?>> memoriesUsed() {
        return ObjectArrayList.of(
                ModMemoryTypes.HEARD_PLAYER.get(),
                ModMemoryTypes.HEARD_PLAYER_POS.get(),
                ModMemoryTypes.LAST_HEARD_TIME.get()
        );
    }

    @Override
    public SensorType<? extends ExtendedSensor<?>> type() {
        return ModSensorTypes.PLAYER_SOUND_SENSOR.get();
    }

    private static final int SCAN_INTERVAL_TICKS = 5;
    private static final double HEARING_RANGE = 14.0;
    private static final double MIN_MOVEMENT_SQR = 0.003;

    private long nextScanTick = 0;

    @Override
    protected void doTick(ServerLevel level, E entity) {
        long now = level.getGameTime();
        if (now < nextScanTick) return;
        nextScanTick = now + SCAN_INTERVAL_TICKS;

        Brain<?> brain = entity.getBrain();
        Player nearest = level.getNearestPlayer(entity, HEARING_RANGE);
        if (nearest == null || nearest.isSpectator()) {
            brain.eraseMemory(ModMemoryTypes.HEARD_PLAYER.get());
            brain.eraseMemory(ModMemoryTypes.HEARD_PLAYER_POS.get());
            return;
        }

        if (entity.hasLineOfSight(nearest)) {
            brain.eraseMemory(ModMemoryTypes.HEARD_PLAYER.get());
            brain.eraseMemory(ModMemoryTypes.HEARD_PLAYER_POS.get());
            return;
        }

        Vec3 delta = nearest.getDeltaMovement();
        double movementSqr = delta.lengthSqr();

        if (nearest.isCrouching()) {
            movementSqr *= 0.25;
        }

        if (movementSqr < MIN_MOVEMENT_SQR) {
            return;
        }

        brain.setMemory(ModMemoryTypes.HEARD_PLAYER.get(), nearest);
        brain.setMemory(ModMemoryTypes.HEARD_PLAYER_POS.get(), nearest.position());
        brain.setMemory(ModMemoryTypes.LAST_HEARD_TIME.get(), now);
    }
}
