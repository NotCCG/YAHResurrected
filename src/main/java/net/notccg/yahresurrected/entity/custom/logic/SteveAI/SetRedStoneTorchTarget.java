package net.notccg.yahresurrected.entity.custom.logic.SteveAI;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.List;
import java.util.function.Predicate;

public class SetRedStoneTorchTarget<E extends LivingEntity> extends ExtendedBehaviour<E> {


    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = ObjectArrayList.of(
            Pair.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT),
            Pair.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.VALUE_ABSENT));

    protected Predicate<? extends LivingEntity> torchTargetPredicate = entity -> true;
    protected Predicate<E> canTargetTorchPredicate = entity -> true;

    public SetRedStoneTorchTarget<E> blockTargetPredicate(Predicate<E> predicate) {
        this.torchTargetPredicate = predicate;
        return this;
    }

    public SetRedStoneTorchTarget<E> canTargetTorchPredicate(Predicate<E> predicate) {
        this.canTargetTorchPredicate = predicate;
        return this;
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        return this.canTargetTorchPredicate.test(entity);
    }

}
