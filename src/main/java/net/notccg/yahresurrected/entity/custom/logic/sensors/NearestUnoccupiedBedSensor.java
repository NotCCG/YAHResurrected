package net.notccg.yahresurrected.entity.custom.logic.sensors;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.notccg.yahresurrected.entity.custom.logic.steve_ai.SteveLogic;
import net.notccg.yahresurrected.util.ModMemoryTypes;
import net.notccg.yahresurrected.util.ModSensorTypes;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.minecraft.world.level.block.Block;

import java.util.List;

public class NearestUnoccupiedBedSensor<E extends PathfinderMob> extends ExtendedSensor<E> {

    private static final int RADIUS = 16;
    private static final int Y_RADIUS = 4;
    private static final int SCAN_INTERVAL_TICKS = 20;

    private long nextScanTick = 0;

    @Override
    public List<MemoryModuleType<?>> memoriesUsed() {
        return List.of(ModMemoryTypes.NEAREST_UNOCCUPIED_BED.get());
    }

    @Override
    public SensorType<? extends ExtendedSensor<?>> type() {
        return ModSensorTypes.UNOCCUPIED_BED_SENSOR.get();
    }

    @Override
    protected void doTick(ServerLevel level, E entity) {
        if (level.isDay()) return;

        long gameTime = level.getGameTime();
        if (gameTime < nextScanTick) return;

        nextScanTick = gameTime + SCAN_INTERVAL_TICKS;

        var brain = entity.getBrain();
        BlockPos origin = entity.blockPosition();

        MemoryModuleType<BlockPos> bedLoc = ModMemoryTypes.NEAREST_UNOCCUPIED_BED.get();

        for (BlockPos pos : BlockPos.betweenClosed(
                origin.offset(-RADIUS, -Y_RADIUS, -RADIUS),
                origin.offset(RADIUS, Y_RADIUS, RADIUS))) {

            BlockPos immutablePos = pos.immutable();

            if (!SteveLogic.isUnoccupiedBed(level, immutablePos)) return;
            brain.setMemory(bedLoc, immutablePos);
        }
    }
}
