package net.notccg.yahresurrected.entity.custom;


import net.minecraft.client.model.HumanoidModel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
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
import net.notccg.yahresurrected.util.ModConfigServer;
import net.notccg.yahresurrected.util.ModTags;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractSteve extends PathfinderMob {
    //Basic Steve Entity Setup
    private static final EntityDataAccessor<Integer> TEXTURE_VARIANT =
            SynchedEntityData.defineId(AbstractSteve.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> SLIM_ARMS =
            SynchedEntityData.defineId(AbstractSteve.class, EntityDataSerializers.BOOLEAN);

    protected AbstractSteve(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setPathfindingMalus(BlockPathTypes.DOOR_WOOD_CLOSED, 0.0f);
        this.setPathfindingMalus(BlockPathTypes.DOOR_OPEN, 0.0f);
        this.setPathfindingMalus(BlockPathTypes.DOOR_IRON_CLOSED, 0.0f);;

        this.setCanPickUpLoot(ModConfigServer.STEVE_PICKS_UP_WANTED_ITEMS.get());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(TEXTURE_VARIANT, -1);
        this.entityData.define(SLIM_ARMS, false);
    }

    public int getTextureVariant() {
        return this.entityData.get(TEXTURE_VARIANT);
    }
    public void setTextureVariant(int textureVariant) {
        this.entityData.set(TEXTURE_VARIANT, textureVariant);
    }

    public boolean hasSlimArms() {
        return this.entityData.get(SLIM_ARMS);}
    public void setSlimArms(boolean slimArms) {
        this.entityData.set(SLIM_ARMS, slimArms);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("TextureVariant", getTextureVariant());
        pCompound.putBoolean("SlimArms", hasSlimArms());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        if (pCompound.contains("TextureVariant", Tag.TAG_INT)) {
            setTextureVariant(pCompound.getInt("TextureVariant"));
        }
        if (pCompound.contains("SlimArms", Tag.TAG_BYTE)) {
            setSlimArms(pCompound.getBoolean("SlimArms"));
        }
    }

    @Override
    public boolean wantsToPickUp(ItemStack pStack) {
        return pStack.is(ModTags.Items.STEVE_LOVED) && ModConfigServer.STEVE_PICKS_UP_WANTED_ITEMS.get() && this.canPickUpLoot();
    }

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


