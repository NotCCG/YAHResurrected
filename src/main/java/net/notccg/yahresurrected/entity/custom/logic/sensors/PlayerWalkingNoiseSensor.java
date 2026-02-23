package net.notccg.yahresurrected.entity.custom.logic.sensors;

import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.notccg.yahresurrected.entity.custom.logic.steve_ai.HeardSoundType;
import net.notccg.yahresurrected.entity.custom.logic.steve_ai.SteveLogic;
import net.notccg.yahresurrected.item.custom.spell_books.SpellBookOneItem;
import net.notccg.yahresurrected.util.ModMemoryTypes;
import net.notccg.yahresurrected.util.ModSensorTypes;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import org.slf4j.Logger;

import java.util.List;

public class PlayerWalkingNoiseSensor<E extends Mob> extends ExtendedSensor<E> {
    private static final Logger LOGGER = LogUtils.getLogger();

    @Override
    public List<MemoryModuleType<?>> memoriesUsed() {
        return ObjectArrayList.of(
                ModMemoryTypes.HEARD_SOUND.get(),
                ModMemoryTypes.HEARD_SOUND_TYPE.get(),
                ModMemoryTypes.HEARD_SOUND_POS.get(),
                ModMemoryTypes.LAST_HEARD_TIME.get()
        );
    }

    @Override
    public SensorType<? extends ExtendedSensor<?>> type() {
        return ModSensorTypes.PLAYER_SOUND_SENSOR.get();
    }

    private static final int SCAN_INTERVAL_TICKS = 5;
    private static final double HEARING_RANGE = 14.0;
    private static final double MIN_MOVEMENT_SQR = 0.4;

    private long nextScanTick = 0;

    @Override
    protected void doTick(ServerLevel level, E entity) {
        long now = level.getGameTime();
        if (now < nextScanTick) return;
        nextScanTick = now + SCAN_INTERVAL_TICKS;

        Brain<?> brain = entity.getBrain();
        Player nearest = level.getNearestPlayer(entity, HEARING_RANGE);
        if (nearest == null) return;

        if (nearest.isSpectator() || nearest.isCreative() || !nearest.isAlive() ||
                (entity.hasLineOfSight(nearest) && !SpellBookOneItem.isCloakActivated(nearest)) ) {
            brain.eraseMemory(ModMemoryTypes.HEARD_SOUND.get());
            brain.eraseMemory(ModMemoryTypes.HEARD_SOUND_POS.get());
            brain.eraseMemory(ModMemoryTypes.LAST_HEARD_TIME.get());
            brain.eraseMemory(ModMemoryTypes.HEARD_SOUND_TYPE.get());
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
        boolean playerHasBeenSeen = brain.hasMemoryValue(ModMemoryTypes.PLAYER_IS_SPOTTED.get());
        boolean hasBeenHurtByPlayer = brain.hasMemoryValue(ModMemoryTypes.PLAYER_HURT.get());

        if (!hasBeenHurtByPlayer) {
            SteveLogic.addCuriosity(brain, now, 0.15);
        }
        if (!playerHasBeenSeen && hasBeenHurtByPlayer) {
            SteveLogic.addParanoia(brain, now, 0.2);
            SteveLogic.addFear(brain, now, 0.05);
        }
        if (playerHasBeenSeen && hasBeenHurtByPlayer) {
            SteveLogic.addFear(brain, now, 0.25);
        }

        brain.setMemory(ModMemoryTypes.HEARD_SOUND.get(), nearest);
        LOGGER.debug("[YAH:R] [SENSOR:{}][{}] set HEARD_SOUND -> {}",
                this.getClass().getSimpleName(),
                entity.getUUID(),
                nearest);
        brain.setMemory(ModMemoryTypes.HEARD_SOUND_POS.get(), nearest.position());
        LOGGER.debug("[YAH:R] [SENSOR:{}][{}] set HEARD_SOUND_POS -> {}",
                this.getClass().getSimpleName(),
                entity.getUUID(),
                nearest.position());
        brain.setMemory(ModMemoryTypes.HEARD_SOUND_TYPE.get(), HeardSoundType.FOOTSTEPS);
        LOGGER.debug("[YAH:R] [SENSOR:{}][{}] set HEARD_SOUND_TYPE -> {} FOOTSTEPS expected",
                this.getClass().getSimpleName(),
                entity.getUUID(),
                brain.getMemory(ModMemoryTypes.HEARD_SOUND_TYPE.get()).orElse(null));
        brain.setMemory(ModMemoryTypes.LAST_HEARD_TIME.get(), now);
        LOGGER.debug("[YAH:R] [SENSOR:{}][{}] set LAST_HEARD_TIME -> {}",
                this.getClass().getSimpleName(),
                entity.getUUID(),
                now);
    }
}
