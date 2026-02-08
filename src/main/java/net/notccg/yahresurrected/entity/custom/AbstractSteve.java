package net.notccg.yahresurrected.entity.custom;


import net.minecraft.client.model.HumanoidModel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.notccg.yahresurrected.entity.custom.logic.steve_ai.SteveLogic;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractSteve extends PathfinderMob {
    protected AbstractSteve(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setPathfindingMalus(BlockPathTypes.DOOR_WOOD_CLOSED, 0.0f);
        this.setPathfindingMalus(BlockPathTypes.DOOR_OPEN, 0.0f);
        this.setPathfindingMalus(BlockPathTypes.DOOR_IRON_CLOSED, 0.0f);;

        this.setCanPickUpLoot(true);
    }

    //Basic Steve Entity Setup
    public static AttributeSupplier.Builder createAttribute() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 20D)
                .add(Attributes.MOVEMENT_SPEED,0.3);
    }

    @Override
    protected PathNavigation createNavigation(Level pLevel) {
        GroundPathNavigation nav =new GroundPathNavigation(this, pLevel);

        nav.setCanOpenDoors(true);
        nav.setCanPassDoors(true);
        nav.setCanFloat(true);

        return nav;
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        pSpawnData = super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
        return pSpawnData;
    }


    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return super.getArmorSlots();
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot equipmentSlot) {
        return super.getItemBySlot(equipmentSlot);
    }

    @Override
    public void setItemSlot(EquipmentSlot equipmentSlot, ItemStack itemStack) {
        super.setItemSlot(equipmentSlot, itemStack);
    }

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }

    public HumanoidModel.ArmPose getArmPose() {
        if(SteveLogic.isSteveLovedItem(this.getMainHandItem())) {
            return HumanoidModel.ArmPose.ITEM;
        }
        return HumanoidModel.ArmPose.EMPTY;
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
}


