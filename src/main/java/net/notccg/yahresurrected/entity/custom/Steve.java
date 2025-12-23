package net.notccg.yahresurrected.entity.custom;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.SleepInBed;
import net.minecraft.world.level.Level;
import net.notccg.yahresurrected.entity.custom.logic.behaviors.LookAtSpottedPlayer;
import net.notccg.yahresurrected.entity.custom.logic.behaviors.SetInterestedBlockTarget;
import net.notccg.yahresurrected.entity.custom.logic.behaviors.SteveWander;
import net.notccg.yahresurrected.entity.custom.logic.sensors.InterestedBlocksSensor;
import net.notccg.yahresurrected.entity.custom.logic.sensors.PlayerSoundsSensor;
import net.notccg.yahresurrected.entity.custom.logic.sensors.SpotPlayerSensor;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.SmartBrainProvider;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
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
    protected ResourceLocation getDefaultLootTable() {
        return new ResourceLocation("youareherobrineresurrected", "entities/steve_entity");
    }

    @Override
    public int getExperienceReward() {
        return 60;
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
                new PlayerSoundsSensor<>(),
                new SpotPlayerSensor<>(),
                new InterestedBlocksSensor<>()
        );
    }

    @Override
    public BrainActivityGroup<? extends Steve> getCoreTasks() {
        return BrainActivityGroup.coreTasks(
                new LookAtSpottedPlayer<>(5),
                new MoveToWalkTarget<>(),
                new LookAtTarget<>()
        );
    }

    @Override
    public BrainActivityGroup<? extends Steve> getIdleTasks() {
        return BrainActivityGroup.idleTasks(
                new SetInterestedBlockTarget<>(1.0f, 2, 10),
                new SteveWander<>(1.25f, 1, 10, 8, 8),
                new SetRandomLookTarget<>()
        );
    }
}
