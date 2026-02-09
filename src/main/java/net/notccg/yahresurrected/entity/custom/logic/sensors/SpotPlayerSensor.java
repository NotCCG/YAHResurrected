package net.notccg.yahresurrected.entity.custom.logic.sensors;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.notccg.yahresurrected.item.ModItems;
import net.notccg.yahresurrected.util.ModMemoryTypes;
import net.notccg.yahresurrected.util.ModSensorTypes;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;

import java.util.Comparator;
import java.util.List;

public class SpotPlayerSensor<E extends PathfinderMob> extends ExtendedSensor<E> {
    private static final List<MemoryModuleType<?>> MEMORIES = ObjectArrayList.of(ModMemoryTypes.SPOTTED_PLAYER.get());
    private static final double SIGHT_RANGE = 64.0;
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
                ).stream()
                .min(Comparator.comparingDouble(entity::distanceToSqr))
                .orElse(null);

        if (visibleNearest == null) {
            brain.eraseMemory(ModMemoryTypes.SPOTTED_PLAYER.get());
            return;
        }

        if (visibleNearest.getInventory().contains(
                new ItemStack(ModItems.SPELLBOOKI.get()))) return;

        System.out.println("[DEBUG] Player spotted by Steve");
        brain.setMemoryWithExpiry(ModMemoryTypes.SPOTTED_PLAYER.get(), visibleNearest, 120L);
        brain.setMemoryWithExpiry(ModMemoryTypes.PLAYER_IS_SPOTTED.get(), true, 2400L);
        brain.setMemory(ModMemoryTypes.LAST_SPOTTED_PLAYER_TIME.get(), now);
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
