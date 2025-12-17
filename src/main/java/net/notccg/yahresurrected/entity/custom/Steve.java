package net.notccg.yahresurrected.entity.custom;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;
import net.notccg.yahresurrected.entity.custom.logic.behaviors.FleeOrApproachPlayer;
import net.notccg.yahresurrected.entity.custom.logic.behaviors.WalkToInterestedBlock;
import net.notccg.yahresurrected.entity.custom.logic.sensors.InterestedBlocksSensor;
import net.notccg.yahresurrected.item.ModItems;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetRandomLookTarget;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;


import java.util.List;

public class Steve extends AbstractSteve implements SmartBrainOwner<Steve> {

    protected Steve(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    // Primary Steve AI behaviou

    @Override
    public List<? extends ExtendedSensor<? extends Steve>> getSensors() {
        return ObjectArrayList.of(
                new InterestedBlocksSensor<>()
        );
    }

    @Override
    public BrainActivityGroup<? extends Steve> getCoreTasks() {
        return BrainActivityGroup.coreTasks(
                new LookAtTarget<>(),
                new MoveToWalkTarget<>(),
                new FleeOrApproachPlayer<>(ModItems.SPELLBOOKII.get(), 1.3, 16, 7, 5),
                new WalkToInterestedBlock<>(1.0f, 2.0)
        );
    }

    @Override
    public BrainActivityGroup<? extends Steve> getIdleTasks() {
        return BrainActivityGroup.idleTasks(
                new SetRandomLookTarget<>(),
                new SetRandomWalkTarget<>()
        );
    }
}
