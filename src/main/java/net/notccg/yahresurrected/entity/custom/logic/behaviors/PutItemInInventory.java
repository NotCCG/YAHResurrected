package net.notccg.yahresurrected.entity.custom.logic.behaviors;

import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.item.ItemStack;
import net.notccg.yahresurrected.util.ModConfigServer;
import net.notccg.yahresurrected.util.ModMemoryTypes;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class PutItemInInventory<E extends PathfinderMob> extends ExtendedBehaviour<E>{
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORIES_REQUIRED = ObjectArrayList.of(
            Pair.of(ModMemoryTypes.INVENTORY_ITEMS.get(), MemoryStatus.REGISTERED)
    );
    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORIES_REQUIRED;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        return (!entity.getMainHandItem().isEmpty()) && ModConfigServer.STEVE_PICKS_UP_WANTED_ITEMS.get();
    }

    @Override
    protected void start(ServerLevel level, E entity, long gameTime) {
        var brain = entity.getBrain();

        ItemStack itemStack = entity.getMainHandItem();

        if (itemStack.isEmpty()) {
            LOGGER.debug("[YAH:R] [BEHAVIORS:{}][{}] boolean \"itemStack.isEmpty().\" is true, return",
                    this.getClass().getSimpleName(), entity.getUUID());
        }

        List<ItemStack> inventory = brain.getMemory(ModMemoryTypes.INVENTORY_ITEMS.get()).orElseGet(ArrayList::new);

        inventory.add(itemStack.copy());
        brain.setMemory(ModMemoryTypes.INVENTORY_ITEMS.get(), inventory);

        entity.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
    }
}
