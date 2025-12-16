package net.notccg.yahresurrected.entity.custom.logic.behaviors;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.ai.util.RandomPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.notccg.yahresurrected.util.ModMemoryTypes;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;

import java.util.List;
import java.util.Random;

public class FleeFromPlayerWithoutItem<E extends Mob> extends ExtendedBehaviour<E> {
    private final Item calmingItem;
    private final double speed;
    private final int fleeHorizontal;
    private final int fleeVertical;

    private long nextRepathTick = 0;

    public FleeFromPlayerWithoutItem(Item calmingItem, double speed, int fleeHorizontal, int fleeVertical) {
        this.calmingItem = calmingItem;
        this.speed = speed;
        this.fleeHorizontal = fleeHorizontal;
        this.fleeVertical = fleeVertical;
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return ObjectArrayList.of(
                Pair.of(ModMemoryTypes.SPOTTED_PLAYER.get(), MemoryStatus.VALUE_PRESENT)
        );
    }

    @Override
    protected void start(ServerLevel level, E entity, long gameTime) {
        this.nextRepathTick = 0;
    }
}
