package net.notccg.yahresurrected.entity.client.steve;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.notccg.yahresurrected.entity.custom.Steve;

public class SteveModel<T extends Steve> extends AbstractSteveModel<T> {

	public SteveModel(ModelPart pRoot) {
		super(pRoot);
	}

	@Override
	public void prepareMobModel(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTick) {
		this.rightArmPose = ArmPose.EMPTY;
		this.leftArmPose = ArmPose.EMPTY;
		ItemStack mainHand = pEntity.getItemInHand(InteractionHand.MAIN_HAND);
		super.prepareMobModel(pEntity, pLimbSwing, pLimbSwingAmount, pPartialTick);
		if (mainHand.is(Items.DIAMOND)) {
			if (pEntity.getMainArm() == HumanoidArm.RIGHT) {
				this.rightArmPose = ArmPose.ITEM;
			}
		}
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshDefinition = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
        return LayerDefinition.create(meshDefinition, 64, 64);
    }
}