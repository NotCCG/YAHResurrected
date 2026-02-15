package net.notccg.yahresurrected.entity.custom.logic.behaviors;

import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.phys.Vec3;
import net.notccg.yahresurrected.util.ModMemoryTypes;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import org.slf4j.Logger;

import java.util.List;

public class PickupInterestedItem<E extends PathfinderMob> extends ExtendedBehaviour<E> {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final float WALK_SPEED = 1.3F;
    private ItemEntity targetItem;

    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = ObjectArrayList.of(
            Pair.of(MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED),
            Pair.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED),
            Pair.of(ModMemoryTypes.INTERESTED_ITEM.get(), MemoryStatus.VALUE_PRESENT),
            Pair.of(ModMemoryTypes.SPOTTED_PLAYER.get(), MemoryStatus.VALUE_ABSENT),
            Pair.of(ModMemoryTypes.FEAR_LEVEL.get(), MemoryStatus.REGISTERED)
    );

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        Brain<?> brain = entity.getBrain();
        ItemEntity itemEntity = brain.getMemory(ModMemoryTypes.INTERESTED_ITEM.get()).orElse(null);

        return (itemEntity != null) && (itemEntity.isAlive()) && (!itemEntity.hasPickUpDelay());
    }

    @Override
    protected void start(ServerLevel level, E entity, long gameTime) {
        this.targetItem = entity.getBrain().getMemory(ModMemoryTypes.INTERESTED_ITEM.get()).orElse(null);
        if (targetItem == null) {
            LOGGER.debug("[YAH:R] [BEHAVIOR:{}][{}] variable \"targetItem\" is null, return", this.getClass().getSimpleName(), entity.getUUID());
        }

        Vec3 targetItemPos = targetItem.position();
        entity.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(targetItemPos, WALK_SPEED, 1));
        LOGGER.debug("[YAH:R] [BEHAVIOR:{}][{}] set WALK_TARGET -> {}", this.getClass().getSimpleName(), entity.getUUID(), targetItemPos);
    }

    @Override
    protected void stop(ServerLevel level, E entity, long gameTime) {
        LOGGER.debug("[YAH:R] [BEHAVIOR:{}][{}] stopped", this.getClass().getSimpleName(), entity.getUUID());
        entity.getBrain().eraseMemory(ModMemoryTypes.INTERESTED_ITEM.get());
        this.targetItem = null;
    }
}
