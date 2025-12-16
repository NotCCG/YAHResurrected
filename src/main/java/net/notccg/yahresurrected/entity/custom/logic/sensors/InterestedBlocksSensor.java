package net.notccg.yahresurrected.entity.custom.logic.sensors;


import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.level.block.Block;
import net.notccg.yahresurrected.entity.custom.logic.SteveAI.SteveInterests;
import net.notccg.yahresurrected.util.ModMemoryTypes;
import net.notccg.yahresurrected.util.ModSensorTypes;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class InterestedBlocksSensor<E extends Mob> extends ExtendedSensor<E> {
    private static final List<MemoryModuleType<?>> MEMORIES = ObjectArrayList.of(ModMemoryTypes.VISITED_BLOCKS.get(), ModMemoryTypes.INTERESTED_BLOCK_TARGET.get());
    private static final int RADIUS = 16;
    private static final int SCAN_INTERVAL_TICKS = 20; // once per second

    private long nextScanTick = 0;

    public InterestedBlocksSensor() {}

    @Override
    public List<MemoryModuleType<?>> memoriesUsed() {
        return MEMORIES;
    }

    @Override
    public SensorType<? extends ExtendedSensor<?>> type() {
        return ModSensorTypes.INTERESTED_BLOCK_SENSOR.get();
    }

    @Override
    protected void doTick(ServerLevel level, E entity) {
        long gameTime = level.getGameTime();
        if (gameTime < nextScanTick)
            return;

        nextScanTick = gameTime + SCAN_INTERVAL_TICKS;

        var brain = entity.getBrain();

        // If we already have a target, don’t pick a new one
        if (brain.hasMemoryValue(ModMemoryTypes.INTERESTED_BLOCK_TARGET.get()))
            return;

        Set<BlockPos> visited = brain.getMemory(ModMemoryTypes.VISITED_BLOCKS.get())
                .orElseGet(HashSet::new);

        BlockPos origin = entity.blockPosition();

        for (BlockPos pos : BlockPos.betweenClosed(
                origin.offset(-RADIUS, -4, -RADIUS),
                origin.offset(RADIUS, 4, RADIUS))) {

            BlockPos immutablePos = pos.immutable();
            if (visited.contains(immutablePos))
                continue;

            Block block = level.getBlockState(pos).getBlock();
            if (!SteveInterests.INTERESTED_BLOCKS.contains(block))
                continue;

            brain.setMemory(ModMemoryTypes.INTERESTED_BLOCK_TARGET.get(), immutablePos);
            brain.setMemory(ModMemoryTypes.VISITED_BLOCKS.get(), visited);
            return;
        }

        // Ensure the visited memory is at least present once we start thinking about blocks
        if (!brain.hasMemoryValue(ModMemoryTypes.VISITED_BLOCKS.get()))
            brain.setMemory(ModMemoryTypes.VISITED_BLOCKS.get(), visited);
    }
}
