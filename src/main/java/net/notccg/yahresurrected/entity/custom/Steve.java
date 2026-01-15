package net.notccg.yahresurrected.entity.custom;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.notccg.yahresurrected.entity.custom.logic.behaviors.*;
import net.notccg.yahresurrected.entity.custom.logic.sensors.*;
import net.notccg.yahresurrected.item.ModItems;
import net.notccg.yahresurrected.util.ModMemoryTypes;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.SmartBrainProvider;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;


import java.util.List;

public class Steve extends AbstractSteve implements SmartBrainOwner<Steve> {
    // Notes:
    // Base movement should always be 1.0f
    // Multiplier for sprinting should be 1.2f
    // Multiplier for sneaking should be 0.3f

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

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        boolean result = super.hurt(pSource, pAmount);

        if (!level().isClientSide && pSource.getEntity() instanceof Player player) {
            var brain = this.getBrain();
            long now = level().getGameTime();

            brain.setMemory(ModMemoryTypes.LAST_HURT_BY.get(), player);
            brain.setMemoryWithExpiry(ModMemoryTypes.PLAYER_HURT.get(), true, 1200L);
        }
        return result;
    }

    // Primary Steve AI behaviour

    @Override
    public List<? extends ExtendedSensor<? extends Steve>> getSensors() {
        return ObjectArrayList.of(
                new PlayerWalkSensor<>(),
                new SpotPlayerSensor<>(),
                new InterestedBlocksSensor<>(),
                new InterestedItemsSensor<>(),
                new NearestUnoccupiedBedSensor<>()
        );
    }

    @Override
    public BrainActivityGroup<? extends Steve> getCoreTasks() {
        return BrainActivityGroup.coreTasks(
                new MoveToWalkTarget<>(),
                new LookAtTarget<>(),
                new FleeOrApproachPlayer<>(
                        ModItems.SPELLBOOKI.get(),
                        1,
                        16,
                        8,
                        2,
                        1
                )
        );
    }

    @Override
    public BrainActivityGroup<? extends Steve> getIdleTasks() {
        return BrainActivityGroup.idleTasks(
                new LookAtSpottedPlayer<>(5),
                new SetInterestedBlockTarget<>(1.0f, 2, 80),
                new SteveWander<>(1.0f, 1, 32, 8),
                new SteveRandomLook<>()
        );
    }
}
