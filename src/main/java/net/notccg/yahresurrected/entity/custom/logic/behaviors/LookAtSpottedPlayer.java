package net.notccg.yahresurrected.entity.custom.logic.behaviors;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.notccg.yahresurrected.util.ModMemoryTypes;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;

import java.util.List;

public class LookAtSpottedPlayer <E extends Mob> extends ExtendedBehaviour<E> {
    private final Item cloakingItem;
    private final float maxYawChange;
    private final float maxPitchChange;

    public LookAtSpottedPlayer(Item cloakingItem, float maxYawChange, float maxPitchChange) {
        this.cloakingItem = cloakingItem;
        this.maxYawChange = maxYawChange;
        this.maxPitchChange = maxPitchChange;
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return ObjectArrayList.of(
                Pair.of(ModMemoryTypes.SPOTTED_PLAYER.get(), MemoryStatus.VALUE_PRESENT)
        );
    }

    @Override
    protected void tick(ServerLevel level, E entity, long gameTime) {
        Brain<?> brain = entity.getBrain();
        Player player = brain.getMemory(ModMemoryTypes.SPOTTED_PLAYER.get()).orElse(null);
        if (player == null)
            return;

        if (cloakingItem != null && player.getInventory().contains(new ItemStack(cloakingItem))) {
            return;
        }

        entity.getLookControl().setLookAt(player, maxYawChange, maxPitchChange);
    }
}
