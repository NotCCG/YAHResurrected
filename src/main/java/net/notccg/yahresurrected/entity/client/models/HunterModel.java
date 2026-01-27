package net.notccg.yahresurrected.entity.client.models;

import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;


public class HunterModel<T extends Mob & RangedAttackMob> extends HumanoidModel<T> {
    public HunterModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = HumanoidModel.createMesh(CubeDeformation.NONE, 0);
        return LayerDefinition.create(meshdefinition, 64, 32);
    }

    @Override
    public void prepareMobModel(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTick) {
        this.rightArmPose = ArmPose.EMPTY;
        this.leftArmPose = ArmPose.EMPTY;
        ItemStack mainHand = pEntity.getItemInHand(InteractionHand.MAIN_HAND);
        if (mainHand.is(Items.BOW) && pEntity.isAggressive()) {
            if (pEntity.getMainArm() == HumanoidArm.RIGHT) {
                this.rightArmPose = ArmPose.BOW_AND_ARROW;
            } else {
                this.leftArmPose = ArmPose.BOW_AND_ARROW;
            }
        }
        if (mainHand.is(ItemTags.SWORDS)) {
            if (pEntity.getMainArm() == HumanoidArm.RIGHT) {
                this.rightArmPose = ArmPose.ITEM;
            } else {
                this.leftArmPose = ArmPose.ITEM;
            }
        }

        super.prepareMobModel(pEntity, pLimbSwing, pLimbSwingAmount, pPartialTick);
    }

    @Override
    public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        this.leftArmPose = ArmPose.EMPTY;
        this.rightArmPose = ArmPose.EMPTY;

        ItemStack mainHand = pEntity.getMainHandItem();

        if (pEntity.isUsingItem() && pEntity.getUseItem().is(Items.BONE)) {
            if (pEntity.getUsedItemHand() == InteractionHand.MAIN_HAND) {
                this.rightArmPose = ArmPose.BOW_AND_ARROW;
            } else {
                this.leftArmPose = ArmPose.BOW_AND_ARROW;
            }
        }

        super.setupAnim(pEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);

        boolean inBowPose = (this.rightArmPose == ArmPose.BOW_AND_ARROW) || (this.leftArmPose == ArmPose.BOW_AND_ARROW);

        boolean swordLike =
                !mainHand.isEmpty()
                        && mainHand.canPerformAction(net.minecraftforge.common.ToolActions.SWORD_SWEEP);

        if (!inBowPose && pEntity.isAggressive() && swordLike) {
            this.setupAttackAnimation(pEntity, pAgeInTicks);
        }
    }
}