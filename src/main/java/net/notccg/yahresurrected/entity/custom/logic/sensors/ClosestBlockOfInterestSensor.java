package net.notccg.yahresurrected.entity.custom.logic.sensors;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.level.block.Blocks;
import net.notccg.yahresurrected.util.ModMemoryTypes;
import net.notccg.yahresurrected.util.ModSensorTypes;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.PredicateSensor;
import net.tslat.smartbrainlib.registry.SBLMemoryTypes;

import java.util.List;
import java.util.function.Predicate;

public class ClosestBlockOfInterestSensor<E extends LivingEntity> extends PredicateSensor<Blocks, E> {
    private static final List<MemoryModuleType<?>> MEMORIES = ObjectArrayList.of(ModMemoryTypes.NEAREST_BLOCK_OF_INTEREST.get(), SBLMemoryTypes.NEARBY_BLOCKS.get());
    protected Predicate<? extends Blocks> blocksPredicate = blocks -> true;

    public ClosestBlockOfInterestSensor() {
        setPredicate((blocks, entity) -> blocks.equals(blocksPredicate));
    }

    @Override
    public List<MemoryModuleType<?>> memoriesUsed() {
        return MEMORIES;
    }

    @Override
    public SensorType<? extends ExtendedSensor<?>> type() {
        return ModSensorTypes.CLOSEST_BLOCK_OF_INTEREST.get();
    }
}
