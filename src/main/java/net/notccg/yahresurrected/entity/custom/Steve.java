package net.notccg.yahresurrected.entity.custom;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.level.Level;
import net.notccg.yahresurrected.entity.custom.logic.behaviors.*;
import net.notccg.yahresurrected.entity.custom.logic.sensors.*;
import net.notccg.yahresurrected.entity.custom.logic.steve_ai.SteveLogic;
import net.notccg.yahresurrected.item.ModItems;
import net.notccg.yahresurrected.util.ModMemoryTypes;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.SmartBrainProvider;
import net.tslat.smartbrainlib.api.core.behaviour.FirstApplicableBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.OneRandomBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Steve extends AbstractSteve implements SmartBrainOwner<Steve> {
    // Notes:
    // Base movement should always be 1.0f
    // Multiplier for sprinting should be 1.3f
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
            BlockPos playerPos = player.getOnPos().above();

            brain.setMemory(ModMemoryTypes.LAST_HURT_BY.get(), player);
            brain.setMemoryWithExpiry(ModMemoryTypes.PLAYER_HURT.get(), true, 1200L);
            brain.setMemory(ModMemoryTypes.CURIOSITY_LEVEL.get(), 0.0);
            brain.setMemory(ModMemoryTypes.PLAYER_HIT_POS.get(), playerPos);

            SteveLogic.addFear(brain, 0.5);
            System.out.println("the player hurt me!");
        }
        return result;
    }

    // Primary Steve AI behaviour

    @Override
    public List<? extends ExtendedSensor<? extends Steve>> getSensors() {
        return ObjectArrayList.of(
                new SpotPlayerSensor<>(),
                new PlayerWalkSensor<>(),
                new InterestedBlocksSensor<>(),
                new InterestedItemsSensor<>(),
                new NearestUnoccupiedBedSensor<>()
        );
    }

    @Override
    public BrainActivityGroup<? extends Steve> getCoreTasks() {
        return BrainActivityGroup.coreTasks(
                new MoveToWalkTarget<>(),
                new LookAtTarget<>()
        );
    }

    @Override
    public BrainActivityGroup<? extends Steve> getIdleTasks() {
        return BrainActivityGroup.idleTasks(
                new FirstApplicableBehaviour<Steve>(
                        new FleeOrApproachPlayer<>(1.0F, 32, 8, 10, 1),
                        new LookAtHitFromDirection<>(),
                        new LookAtSpottedPlayer<>(5, 60),
                        new FleeOrInvestigateBehaviour<>(2, 20, 1),
                        new SetInterestedBlockTarget<>(1.0f, 2, 80)
                ),
                new OneRandomBehaviour<Steve>(
                        new SteveWander<>(1.0f, 1, 32, 8),
                        new SteveRandomLook<>(),
                        new Idle<>().runFor(entity -> entity.getRandom().nextInt(30, 60))
                )
        );
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);

        var brain = this.getBrain();

        brain.getMemory(ModMemoryTypes.VISITED_BLOCKS.get()).ifPresent(visitedSet -> {
            ListTag listTag = new ListTag();
            for (BlockPos pos : visitedSet) {
                listTag.add(NbtUtils.writeBlockPos(pos));
            }
            pCompound.put("VisitedBlocks", listTag);
        });

        brain.getMemory(ModMemoryTypes.FEAR_LEVEL.get()).ifPresent(f -> pCompound.putDouble("FearLevel", f));
        brain.getMemory(ModMemoryTypes.CURIOSITY_LEVEL.get()).ifPresent(c -> pCompound.putDouble("CuriosityLevel", c));
        brain.getMemory(ModMemoryTypes.PARANOIA_LEVEL.get()).ifPresent(p -> pCompound.putDouble("ParanoiaLevel", p));
        brain.getMemory(ModMemoryTypes.PLAYER_IS_SPOTTED.get()).ifPresent(sp -> pCompound.putBoolean("PlayerIsSpotted", sp));
        brain.getMemory(ModMemoryTypes.LAST_SPOTTED_PLAYER_TIME.get()).ifPresent(st -> pCompound.putLong("SpottedPlayerTime", st));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);

        var brain = this.getBrain();

        if (pCompound.contains("VisitedBlocks", Tag.TAG_LIST)) {
            ListTag listTag = pCompound.getList("VisitedBlocks", Tag.TAG_COMPOUND);
            Set<BlockPos> visitedSet = new HashSet<>();
            for (int i = 0; i < listTag.size(); i++) {
                visitedSet.add(NbtUtils.readBlockPos(listTag.getCompound(i)));
            }
            brain.setMemory(ModMemoryTypes.VISITED_BLOCKS.get(), visitedSet);
        }

        if (pCompound.contains("FearLevel", Tag.TAG_DOUBLE)) {
            brain.setMemory(ModMemoryTypes.FEAR_LEVEL.get(), pCompound.getDouble("FearLevel"));
        }
        if (pCompound.contains("CuriosityLevel", Tag.TAG_DOUBLE)) {
            brain.setMemory(ModMemoryTypes.CURIOSITY_LEVEL.get(), pCompound.getDouble("CuriosityLevel"));
        }
        if (pCompound.contains("ParanoiaLevel", Tag.TAG_DOUBLE)) {
            brain.setMemory(ModMemoryTypes.PARANOIA_LEVEL.get(), pCompound.getDouble("ParanoiaLevel"));
        }
        if (pCompound.contains("PlayerIsSpotted", Tag.TAG_BYTE)) {
            brain.setMemoryWithExpiry(ModMemoryTypes.PLAYER_IS_SPOTTED.get(), true, 1200L);
        }
    }
}
