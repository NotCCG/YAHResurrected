package net.notccg.yahresurrected.entity.custom;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.notccg.yahresurrected.entity.custom.logic.SteveAI.SteveLogic;
import net.notccg.yahresurrected.entity.custom.logic.behaviors.SetRedStoneTorchTarget;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.SmartBrainProvider;
import net.tslat.smartbrainlib.api.core.behaviour.FirstApplicableBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.WalkOrRunToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToBlock;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.custom.NearbyBlocksSensor;
import net.tslat.smartbrainlib.api.core.sensor.custom.NearbyItemsSensor;
import net.tslat.smartbrainlib.api.core.sensor.custom.UnreachableTargetSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.HurtBySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyPlayersSensor;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Steve extends AbstractSteve implements SmartBrainOwner {

    public HumanoidModel.ArmPose getArmPose() {
        if(SteveLogic.isLovedItem(this.getMainHandItem())) {
            return HumanoidModel.ArmPose.ITEM;
        }
        return HumanoidModel.ArmPose.EMPTY;
    }


    public Steve(EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }


    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.PLAYER_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.PLAYER_DEATH;
    }


    @Override
    protected Brain.Provider<?> brainProvider() {
        return new SmartBrainProvider<>(this);
    }

    @Override
    protected void customServerAiStep() {
        tickBrain(this);
    }

    @Override
    public BrainActivityGroup getIdleTasks() {
        return BrainActivityGroup.idleTasks(
                new FirstApplicableBehaviour(
                        new SetRedStoneTorchTarget(Blocks.REDSTONE_TORCH),
                        new Idle<>().runFor(entity -> entity.getRandom().nextInt(30, 60))
                )
        );
    }

    @Override
    public BrainActivityGroup getCoreTasks() {
        return BrainActivityGroup.coreTasks(
                new LookAtTarget<>(),
                new WalkOrRunToWalkTarget<>()
        );
    }

    @Override
    public List<? extends ExtendedSensor> getSensors() {
        return List.of(
                new NearbyPlayersSensor(),
                new HurtBySensor(),
                new NearbyBlocksSensor<>(),
                new NearbyItemsSensor(),
                new UnreachableTargetSensor<AbstractSteve>()
        );
    }
}
