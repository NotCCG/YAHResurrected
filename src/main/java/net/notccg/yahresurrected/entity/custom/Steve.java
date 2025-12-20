package net.notccg.yahresurrected.entity.custom;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.level.Level;
import net.notccg.yahresurrected.entity.custom.logic.behaviors.FleeOrApproachPlayer;
import net.notccg.yahresurrected.entity.custom.logic.behaviors.HesitateBehavior;
import net.notccg.yahresurrected.entity.custom.logic.behaviors.LookAtSpottedPlayer;
import net.notccg.yahresurrected.entity.custom.logic.behaviors.WalkToInterestedBlock;
import net.notccg.yahresurrected.entity.custom.logic.sensors.InterestedBlocksSensor;
import net.notccg.yahresurrected.item.ModItems;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.SmartBrainProvider;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.WalkOrRunToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetRandomLookTarget;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;


import java.util.List;

public class Steve extends AbstractSteve implements SmartBrainOwner<Steve> {

    public Steve(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public void aiStep() {
        super.aiStep();

        final float maxHeadYaw = 60.0f;
        final float maxHeadPitch = 90.0f;

        float bodyYaw = this.getYRot();
        float headYaw = this.getYHeadRot();

        float deltaYaw = Mth.wrapDegrees(headYaw - bodyYaw);
        deltaYaw = Mth.clamp(deltaYaw, -maxHeadYaw, maxHeadYaw);

        this.setYHeadRot(bodyYaw + deltaYaw);

        this.setXRot(Mth.clamp(this.getXRot(), -maxHeadPitch, maxHeadPitch));
    }

    @Override
    protected Brain.Provider<?> brainProvider() {
        return new SmartBrainProvider<>(this);
    }

    @Override
    protected void customServerAiStep() {
        tickBrain(this);
    }

    // Primary Steve AI behaviour

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
                new WalkOrRunToWalkTarget<>()
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
