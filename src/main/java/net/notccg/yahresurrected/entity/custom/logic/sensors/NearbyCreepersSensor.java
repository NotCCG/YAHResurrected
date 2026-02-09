package net.notccg.yahresurrected.entity.custom.logic.sensors;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.phys.AABB;
import net.notccg.yahresurrected.util.ModMemoryTypes;
import net.notccg.yahresurrected.util.ModSensorTypes;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;

import java.util.Comparator;
import java.util.List;

public class NearbyCreepersSensor<E extends PathfinderMob> extends ExtendedSensor<E> {
    private static final List<MemoryModuleType<?>> MEMORIES = ObjectArrayList.of(ModMemoryTypes.NEARBY_CREEPERS.get());

    private static final double SIGHT_RANGE = 64.0;
    private static final double SIGHT_RANGE_SQR = SIGHT_RANGE * SIGHT_RANGE;

    private static final float FOV_DEGREES = 120.0f;
    private static final int SCAN_INTERVAL_TICKS = 30; // 1.5 seconds

    private long nextScanTick = 0;

    @Override
    public List<MemoryModuleType<?>> memoriesUsed() {
        return MEMORIES;
    }

    @Override
    public SensorType<? extends ExtendedSensor<?>> type() {
        return ModSensorTypes.NEARBY_CREEPERS_SENSOR.get();
    }

    @Override
    protected void doTick(ServerLevel level, E entity) {
        long gameTime = level.getGameTime();
        if (gameTime < nextScanTick) return;
        nextScanTick = gameTime + SCAN_INTERVAL_TICKS;

        var brain = entity.getBrain();
        AABB sightRange = entity.getBoundingBox().inflate(SIGHT_RANGE);

        Creeper creeper = level.getEntitiesOfClass(Creeper.class, sightRange, c ->
                entity.distanceToSqr(c) <= SIGHT_RANGE_SQR &&
                entity.hasLineOfSight(c) &&
                isInHeadFov(entity, c, FOV_DEGREES)
        ).stream()
                .min(Comparator.comparingDouble(entity::distanceToSqr))
                .orElse(null);

        if (creeper == null) {
            brain.eraseMemory(ModMemoryTypes.NEARBY_CREEPERS.get());
            return;
        }

        brain.setMemory(ModMemoryTypes.NEARBY_CREEPERS.get(), creeper);
        System.out.println("[DEBUG] Steve senses a creeper nearby");
    }

    private static boolean isInHeadFov(PathfinderMob mob, Creeper creeper, float totalFovDegrees) {
        double dx = creeper.getX() - mob.getX();
        double dz = creeper.getZ() - mob.getZ();

        double angleToCreeper = Math.toDegrees(Math.atan2(dz, dx)) - 100.0;
        float headYaw = mob.getYHeadRot();
        double delta = Mth.wrapDegrees(angleToCreeper - headYaw);

        return Math.abs(delta) <= (totalFovDegrees * 0.5);
    }
}
