package net.notccg.yahresurrected.entity.custom.logic.sensors;

import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.notccg.yahresurrected.entity.custom.logic.steve_ai.SteveLogic;
import net.notccg.yahresurrected.item.ModItems;
import net.notccg.yahresurrected.item.custom.SpellBookOneItem;
import net.notccg.yahresurrected.util.ModMemoryTypes;
import net.notccg.yahresurrected.util.ModSensorTypes;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import org.slf4j.Logger;

import java.util.Comparator;
import java.util.List;

public class SpotPlayerSensor<E extends PathfinderMob> extends ExtendedSensor<E> {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final List<MemoryModuleType<?>> MEMORIES = ObjectArrayList.of(ModMemoryTypes.SPOTTED_PLAYER.get(),
            ModMemoryTypes.PLAYER_IS_SPOTTED.get(),
            ModMemoryTypes.LAST_SPOTTED_PLAYER_TIME.get(),
            ModMemoryTypes.LAST_PLAYER_SEEN.get());
    private static final double SIGHT_RANGE = 32.0;
    private static final double SIGHT_RANGE_SQR = SIGHT_RANGE * SIGHT_RANGE;

    private static final float FOV_DEGREES = 120.0f;
    private static final int SCAN_INTERVAL_TICKS = 10; // twice a second

    private long nextScanTick = 0;

    @Override
    public List<MemoryModuleType<?>> memoriesUsed() {
        return MEMORIES;
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
        AABB box = entity.getBoundingBox().inflate(SIGHT_RANGE);


        Player visibleNearest = level.getEntitiesOfClass(Player.class, box, p ->
                !p.isSpectator()
                        && p.isAlive()
                        && !p.isCreative()
                        && entity.distanceToSqr(p) <= SIGHT_RANGE_SQR
                        && entity.hasLineOfSight(p)
                        && isInHeadFov(entity, p, FOV_DEGREES)
                        && !SpellBookOneItem.isCloakActivated(p)
                ).stream()
                .min(Comparator.comparingDouble(entity::distanceToSqr))
                .orElse(null);

        if (visibleNearest == null) {
            brain.eraseMemory(ModMemoryTypes.SPOTTED_PLAYER.get());

            boolean addParanoia = brain.getTimeUntilExpiry(ModMemoryTypes.SPOTTED_PLAYER.get()) > 0;
            if (addParanoia) {
                SteveLogic.addParanoia(brain, now, 0.01);
            }

            brain.eraseMemory(ModMemoryTypes.PLAYER_IS_SPOTTED.get());
            return;
        }

        System.out.println("[YAH:R STEVE-DEBUG] Player spotted by Steve");
        brain.setMemoryWithExpiry(ModMemoryTypes.SPOTTED_PLAYER.get(), visibleNearest, 40L);
        LOGGER.debug("[YAH:R] [SENSOR:{}][{}] set SPOTTED_PLAYER -> [{} | Expires: {}] ",
                this.getClass().getSimpleName(),
                entity.getUUID(),
                visibleNearest,
                (level.getGameTime() + 40L));
        brain.setMemoryWithExpiry(ModMemoryTypes.LAST_PLAYER_SEEN.get(), visibleNearest.getUUID(), 200L);
        LOGGER.debug("[YAH:R] [SENSOR:{}][{}] set LAST_PLAYER_SEEN -> [{} | Expires: {}] ",
                this.getClass().getSimpleName(),
                entity.getUUID(),
                visibleNearest.getUUID(),
                (level.getGameTime() + 200L));
        brain.setMemoryWithExpiry(ModMemoryTypes.PLAYER_IS_SPOTTED.get(), true, 2400L);
        LOGGER.debug("[YAH:R] [SENSOR:{}][{}] set PLAYER_IS_SPOTTED -> [{} | Expires: {}] ",
                this.getClass().getSimpleName(),
                entity.getUUID(),
                true,
                (level.getGameTime() + 2400L));
        brain.setMemory(ModMemoryTypes.LAST_SPOTTED_PLAYER_TIME.get(), now);
        LOGGER.debug("[YAH:R] [SENSOR:{}][{}] set LAST_SPOTTED_PLAYER_TIME -> {}",
                this.getClass().getSimpleName(),
                entity.getUUID(),
                now);
    }

    private static boolean isInHeadFov(PathfinderMob mob, Player player, float totalFovDegrees) {
        double dx = player.getX() - mob.getX();
        double dz = player.getZ() - mob.getZ();

        double angleToPlayer = Math.toDegrees(Math.atan2(dz, dx)) - 80.0;
        float headYaw = mob.getYHeadRot();
        double delta = Mth.wrapDegrees(angleToPlayer - headYaw);

        return Math.abs(delta) <= (totalFovDegrees * 0.5);
    }
}
