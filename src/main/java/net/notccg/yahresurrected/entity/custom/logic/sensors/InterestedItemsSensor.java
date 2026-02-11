package net.notccg.yahresurrected.entity.custom.logic.sensors;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.phys.AABB;
import net.notccg.yahresurrected.util.ModMemoryTypes;
import net.notccg.yahresurrected.util.ModSensorTypes;
import net.notccg.yahresurrected.util.ModTags;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;

import java.util.Comparator;
import java.util.List;

public class InterestedItemsSensor<E extends PathfinderMob> extends ExtendedSensor<E> {
    private static final List<MemoryModuleType<?>> MEMORIES = ObjectArrayList.of(
            ModMemoryTypes.INTERESTED_ITEM.get()
    );
    private static final int XY_RANGE = 16;
    private static final int Y_RANGE = 8;
    private static final int SCAN_INTERVAL_TICKS = 20;

    private long nextScanTick = 0;

    public InterestedItemsSensor() {}



    @Override
    public List<MemoryModuleType<?>> memoriesUsed() {
        return MEMORIES;
    }

    @Override
    public SensorType<? extends ExtendedSensor<?>> type() {
        return ModSensorTypes.INTERESTED_ITEMS_SENSOR.get();
    }

    @Override
    protected void doTick(ServerLevel level, E entity) {
        long gameTime = level.getGameTime();
        if (gameTime < nextScanTick) return;
        nextScanTick = gameTime + SCAN_INTERVAL_TICKS;

        var brain = entity.getBrain();

        MemoryModuleType<ItemEntity> locType = ModMemoryTypes.INTERESTED_ITEM.get();

        if (brain.hasMemoryValue(locType)) return;

        AABB box = entity.getBoundingBox().inflate(XY_RANGE, Y_RANGE, XY_RANGE);

        ItemEntity nearest = level.getEntitiesOfClass(ItemEntity.class, box, itemEntity ->
                itemEntity.isAlive()
                        && !itemEntity.getTags().isEmpty()
                        && itemEntity.getItem().is(ModTags.Items.STEVE_LOVED)
            ).stream().min(Comparator.comparingDouble(entity::distanceToSqr)).orElse(null);

        if (nearest == null) {
            brain.eraseMemory(locType);
            return;
        }

        System.out.println("[YAH:R STEVE-DEBUG] Steve found item " + nearest);
        brain.setMemory(locType, nearest);
    }
}