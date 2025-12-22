package net.notccg.yahresurrected.entity.client.models;

import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.function.Function;

public class SlayerModel<T extends Mob & RangedAttackMob> extends HumanoidModel<T> {

    public SlayerModel(ModelPart pRoot, Function<ResourceLocation, RenderType> pRenderType) {
        super(pRoot, pRenderType);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = HumanoidModel.createMesh(CubeDeformation.NONE, 0);
        return LayerDefinition.create(meshdefinition, 64, 32);
    }

    @Override
    public void prepareMobModel(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTick) {
        this.leftArmPose = ArmPose.EMPTY;
        this.rightArmPose = ArmPose.EMPTY;
        ItemStack mainHand = pEntity.getItemInHand(InteractionHand.MAIN_HAND);

        if (mainHand.is(ItemTags.SWORDS) && pEntity.isAggressive()) {
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
        super.setupAnim(pEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
        ItemStack mainHandItem = pEntity.getMainHandItem();
        if (pEntity.isAggressive() && (mainHandItem.isEmpty() || !mainHandItem.is(Items.BOW))) {
            float $$7 = Mth.sin(this.attackTime * 3.1415927F);
            float $$8 = Mth.sin((1.0F - (1.0F - this.attackTime) * (1.0F - this.attackTime)) * 3.1415927F);
            this.rightArm.zRot = 0.0F;
            this.leftArm.zRot = 0.0F;
            this.rightArm.yRot = -(0.1F - $$7 * 0.6F);
            this.leftArm.yRot = 0.1F - $$7 * 0.6F;
            this.rightArm.xRot = -1.5707964F;
            this.leftArm.xRot = -1.5707964F;
            ModelPart armRot = this.rightArm;
            armRot.xRot -= $$7 * 1.2F - $$8 * 0.4F;
            armRot = this.leftArm;
            armRot.xRot -= $$7 * 1.2F - $$8 * 0.4F;
            AnimationUtils.bobArms(this.rightArm, this.leftArm, pAgeInTicks);
        }
    }
}
