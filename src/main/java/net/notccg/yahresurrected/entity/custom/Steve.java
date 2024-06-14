package net.notccg.yahresurrected.entity.custom;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.notccg.yahresurrected.entity.custom.logic.SteveAI.SteveLogic;
import org.jetbrains.annotations.Nullable;

public class Steve extends AbstractSteve {

    public HumanoidModel.ArmPose getArmPose() {
        if(SteveLogic.isLovedItem(this.getMainHandItem())) {
            return HumanoidModel.ArmPose.ITEM;
        }
        return HumanoidModel.ArmPose.EMPTY;
    }

    private final SimpleContainer inventory = new SimpleContainer(128);

    public Steve(EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }


    public void findBlockTarget(BlockState pBlockstate, MemoryModuleType pBlockTargetMemory) {

    }

    public void holdInMainHand(ItemStack pStack) {
        this.setItemSlotAndDropWhenKilled(EquipmentSlot.MAINHAND, pStack);
    }

    public ItemStack addToInventory(ItemStack pItemStack) {
        return this.inventory.addItem(pItemStack);
    }

    public boolean canAddToInventory(ItemStack pItemStack) {
        return this.inventory.canAddItem(pItemStack);
    }

    public boolean canReplaceCurrentItem(ItemStack pCanidate) {
        EquipmentSlot equipmentSlot = LivingEntity.getEquipmentSlotForItem(pCanidate);
        ItemStack itemStack = this.getItemBySlot(equipmentSlot);
        return this.canReplaceCurrentItem(pCanidate, itemStack);
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
