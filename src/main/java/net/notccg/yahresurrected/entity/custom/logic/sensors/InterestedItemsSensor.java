package net.notccg.yahresurrected.entity.custom.logic.sensors;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.notccg.yahresurrected.util.ModMemoryTypes;
import net.notccg.yahresurrected.util.ModSensorTypes;
import net.notccg.yahresurrected.util.ModTags;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;

import java.util.List;

public class InterestedItemsSensor<E extends PathfinderMob> extends ExtendedSensor<E> {
    private static final List<MemoryModuleType<?>> MEMORIES = ObjectArrayList.of(
            ModMemoryTypes.INTERESTED_ITEM_LOCATION.get()
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

        var brain = entity.getBrain();

        List<ItemEntity> nearbyItems = level.getEntitiesOfClass(ItemEntity.class, entity.getBoundingBox().inflate(16, 8, 16));
    }
}