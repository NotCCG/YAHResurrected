package net.notccg.yahresurrected.entity.custom.logic.behaviors;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.level.block.Blocks;
import net.notccg.yahresurrected.util.ModMemoryTypes;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.registry.SBLMemoryTypes;

import java.util.List;
import java.util.function.Predicate;

public class SetBlockInterestTarget<E extends PathfinderMob> extends ExtendedBehaviour<E> {

    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = ObjectArrayList.of(
            Pair.of(SBLMemoryTypes.NEARBY_BLOCKS.get(), MemoryStatus.VALUE_PRESENT),
            Pair.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT),
            Pair.of(ModMemoryTypes.TARGET_BLOCK.get(), MemoryStatus.VALUE_ABSENT));

    protected Predicate<? extends Blocks> targetPredicate = blocks -> true;

    public SetBlockInterestTarget<E> targetPredicate(Predicate<? extends Blocks> predicate) {
        this.targetPredicate = predicate;
        return this;
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected void start(ServerLevel level, E entity, long gameTime) {
        super.start(level, entity, gameTime);
    }

    @Override
    protected void tick(ServerLevel level, E entity, long gameTime) {
        super.tick(level, entity, gameTime);
    }
}

