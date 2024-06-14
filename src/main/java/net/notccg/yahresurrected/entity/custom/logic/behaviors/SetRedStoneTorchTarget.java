package net.notccg.yahresurrected.entity.custom.logic.behaviors;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.notccg.yahresurrected.util.ModMemoryTypes;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.registry.SBLMemoryTypes;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SetRedStoneTorchTarget<E extends Mob> extends ExtendedBehaviour<E> {
    private static final MemoryModuleType<List<Pair<BlockPos, BlockState>>> NEARBY_BLOCKS = SBLMemoryTypes.NEARBY_BLOCKS.get();
    private static final MemoryModuleType<BlockPos> TARGET_BLOCK = ModMemoryTypes.TARGET_BLOCK.get();

    private final Block targetBlockType;
    public SetRedStoneTorchTarget(Block targetBlockType) {
        this.targetBlockType = targetBlockType;
    }

    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = ObjectArrayList.of(
            Pair.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT),
            Pair.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.VALUE_ABSENT));

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        return entity.getBrain().hasMemoryValue(NEARBY_BLOCKS);
    }

    @Override
    protected void start(ServerLevel level, E entity, long gameTime) {
        List<Pair<BlockPos, BlockState>> nearbyBlocks = entity.getBrain().getMemory(NEARBY_BLOCKS).get();
        List<Pair<BlockPos, BlockState>> filteredBlocks = nearbyBlocks.stream()
                .filter(pair -> pair.getSecond().getBlock() == targetBlockType)
                .collect(Collectors.toList());

        Optional<BlockPos> targetBlock = filteredBlocks.stream()
                .map(Pair::getFirst)
                .filter(blockPos -> !hasVisitedBlock(entity, blockPos))
                .findFirst();

        targetBlock.ifPresent(blockPos -> {
            entity.getBrain().setMemory(TARGET_BLOCK, blockPos);
            navigateToBlock(entity, blockPos);
        });
    }

    @Override
    protected void stop(ServerLevel level, E entity, long gameTime) {
        entity.getBrain().eraseMemory(TARGET_BLOCK);
    }

    @Override
    protected boolean canStillUse(ServerLevel level, E entity, long gameTime) {
        return entity.getBrain().hasMemoryValue(TARGET_BLOCK);
    }

    @Override
    protected void tick(E entity) {
        BlockPos targetBlock = entity.getBrain().getMemory(TARGET_BLOCK).get();
        if (targetBlock.closerThan(entity.blockPosition(), 1.0)) {
            markBlockAsVisited(entity, targetBlock);
            entity.getBrain().eraseMemory(TARGET_BLOCK);
        } else {
            navigateToBlock(entity, targetBlock);
        }
    }

    private boolean hasVisitedBlock(E entity, BlockPos blockPos) {
        // Implement logic to check if the entity has visited the block before
        return false;
    }

    private void markBlockAsVisited(E entity, BlockPos blockPos) {
        // Implement logic to mark the block as visited
    }

    private void navigateToBlock(Mob entity, BlockPos blockPos) {
        entity.getNavigation().moveTo(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5, 1.0);
    }
}
