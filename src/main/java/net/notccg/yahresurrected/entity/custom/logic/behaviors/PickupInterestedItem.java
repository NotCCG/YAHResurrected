package net.notccg.yahresurrected.entity.custom.logic.behaviors;

import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.notccg.yahresurrected.util.ModMemoryTypes;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import org.slf4j.Logger;

import java.util.List;

public class PickupInterestedItem<E extends PathfinderMob> extends ExtendedBehaviour<E> {
    private static final Logger LOGGER = LogUtils.getLogger();

    private final long repathInterval;
    private final float speed;
    private final boolean isEnabled;

    private long nextRepathTick = 0;

    public PickupInterestedItem(long repathInterval, float speed, boolean isEnabled) {
        this.repathInterval = repathInterval;
        this.speed = speed;
        this.isEnabled = isEnabled;
    }

    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = ObjectArrayList.of(
            Pair.of(MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED),
            Pair.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED),
            Pair.of(ModMemoryTypes.INTERESTED_ITEM.get(), MemoryStatus.VALUE_PRESENT),
            Pair.of(ModMemoryTypes.SPOTTED_PLAYER.get(), MemoryStatus.VALUE_ABSENT),
            Pair.of(ModMemoryTypes.PICKED_UP_LIKED_ITEM.get(), MemoryStatus.REGISTERED)
    );

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        return isEnabled && entity.getMainHandItem().isEmpty();
    }

    @Override
    protected void start(ServerLevel level, E entity, long gameTime) {
        if (gameTime < nextRepathTick) return;
        nextRepathTick = gameTime + repathInterval;

        Brain<?> brain = entity.getBrain();
        ItemStack entityHandItem = entity.getMainHandItem();

        if (!entityHandItem.isEmpty()) {
            LOGGER.debug("[YAH:R] [BEHAVIORS:{}][{}] steve picked up [{}]",
                    this.getClass().getSimpleName(), entity.getUUID(), entityHandItem.getItem());

            brain.eraseMemory(ModMemoryTypes.INTERESTED_ITEM.get());
            LOGGER.debug("[YAH:R] [BEHAVIORS:{}][{}] erased INTERESTED_ITEM",
                    this.getClass().getSimpleName(), entity.getUUID());

            brain.eraseMemory(MemoryModuleType.WALK_TARGET);
            LOGGER.debug("[YAH:R] [BEHAVIORS:{}][{}] erased WALK_TARGET",
                    this.getClass().getSimpleName(), entity.getUUID());

            brain.eraseMemory(MemoryModuleType.LOOK_TARGET);
            LOGGER.debug("[YAH:R] [BEHAVIORS:{}][{}] erased LOOK_TARGET",
                    this.getClass().getSimpleName(), entity.getUUID());

            brain.setMemoryWithExpiry(ModMemoryTypes.PICKED_UP_LIKED_ITEM.get(), true, 80L);
            LOGGER.debug("[YAH:R] [BEHAVIORS:{}][{}] set PICKED_UP_LIKED_ITEM",
                    this.getClass().getSimpleName(), entity.getUUID());
        }


        ItemEntity item = brain.getMemory(ModMemoryTypes.INTERESTED_ITEM.get()).orElse(null);
        if (item == null) {
            LOGGER.debug("[YAH:R] [BEHAVIORS:{}][{}] variable \"item\" is null, return",
                    this.getClass().getSimpleName(), entity.getUUID());
            return;
        }
        BlockPos itemPos = item.getOnPos();

        brain.setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(itemPos, speed, 0));
        LOGGER.debug("[YAH:R] [BEHAVIORS:{}][{}] set WALK_TARGET -> {}",
                this.getClass().getSimpleName(), entity.getUUID(), itemPos);

        brain.setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(itemPos));
        LOGGER.debug("[YAH:R] [BEHAVIORS:{}][{}] set LOOK_TARGET -> {}",
                this.getClass().getSimpleName(), entity.getUUID(), itemPos);
    }

    @Override
    protected void stop(E entity) {
        entity.getBrain().eraseMemory(ModMemoryTypes.INTERESTED_ITEM.get());
        LOGGER.debug("[YAH:R] [BEHAVIORS:{}][{}] erased INTERESTED_ITEM", this.getClass().getSimpleName(), entity.getUUID());
    }
}
