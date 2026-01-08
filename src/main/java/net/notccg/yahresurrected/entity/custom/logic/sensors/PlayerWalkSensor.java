package net.notccg.yahresurrected.entity.custom.logic.sensors;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.notccg.yahresurrected.entity.custom.logic.steve_ai.HeardSoundType;
import net.notccg.yahresurrected.util.ModMemoryTypes;
import net.notccg.yahresurrected.util.ModSensorTypes;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;

import java.util.List;

public class PlayerWalkSensor<E extends Mob> extends ExtendedSensor<E> {
    @Override
    public List<MemoryModuleType<?>> memoriesUsed() {
        return ObjectArrayList.of(
                ModMemoryTypes.HEARD_SOUND.get(),
                ModMemoryTypes.HEARD_SOUND_TYPE.get(),
                ModMemoryTypes.HEARD_SOUND_POS.get(),
                ModMemoryTypes.LAST_HEARD_TIME.get()
        );
    }

    private static void setHeardSoundIfNewer(Brain<?> brain,
                                             long now,
                                             Player source,
                                             Vec3 pos,
                                             HeardSoundType type) {
        long last = brain.getMemory(ModMemoryTypes.LAST_HEARD_TIME.get()).orElse(Long.MIN_VALUE);

        if (now <= last)
            return;

        brain.setMemory(ModMemoryTypes.HEARD_SOUND.get(), source);
        brain.setMemory(ModMemoryTypes.HEARD_SOUND_POS.get(), pos);
        brain.setMemory(ModMemoryTypes.HEARD_SOUND_TYPE.get(), type);
        brain.setMemory(ModMemoryTypes.LAST_HEARD_TIME.get(), now);
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
            brain.eraseMemory(ModMemoryTypes.HEARD_SOUND.get());
            brain.eraseMemory(ModMemoryTypes.HEARD_SOUND_POS.get());
            return;
        }

        if (entity.hasLineOfSight(nearest)) {
            brain.eraseMemory(ModMemoryTypes.HEARD_SOUND.get());
            brain.eraseMemory(ModMemoryTypes.HEARD_SOUND_POS.get());
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

        brain.setMemory(ModMemoryTypes.HEARD_SOUND.get(), nearest);
        brain.setMemory(ModMemoryTypes.HEARD_SOUND_POS.get(), nearest.position());
        brain.setMemory(ModMemoryTypes.HEARD_SOUND_TYPE.get(), HeardSoundType.FOOTSTEPS);
        brain.setMemory(ModMemoryTypes.LAST_HEARD_TIME.get(), now);
    }
}
