package net.notccg.yahresurrected.entity.client.steve;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.notccg.yahresurrected.entity.animations.ModAnimationsDefinitions;
import net.notccg.yahresurrected.entity.custom.SteveEntity;

public class SteveModel<T extends Entity> extends HierarchicalModel<T> {
	private final ModelPart Steve;
	private final ModelPart Head;

	public SteveModel(ModelPart root) {
		this.Steve = root.getChild("Steve");
		this.Head = Steve.getChild("Torso").getChild("Head");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Steve = partdefinition.addOrReplaceChild("Steve", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Torso = Steve.addOrReplaceChild("Torso", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, -28.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 28.0F, 0.0F));

		PartDefinition Arms = Torso.addOrReplaceChild("Arms", CubeListBuilder.create(), PartPose.offset(2.0F, -16.0F, 0.0F));

		PartDefinition ArmL = Arms.addOrReplaceChild("ArmL", CubeListBuilder.create().texOffs(32, 48).addBox(0.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, -10.0F, 0.0F));

		PartDefinition ArmR = Arms.addOrReplaceChild("ArmR", CubeListBuilder.create().texOffs(40, 16).addBox(-4.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.0F, -10.0F, 0.0F));

		PartDefinition Head = Torso.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -28.0F, 0.0F));

		PartDefinition Legs = Torso.addOrReplaceChild("Legs", CubeListBuilder.create(), PartPose.offset(-2.0F, -16.0F, 0.0F));

		PartDefinition LegL = Legs.addOrReplaceChild("LegL", CubeListBuilder.create().texOffs(16, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 0.0F, 0.0F));

		PartDefinition LegR = Legs.addOrReplaceChild("LegR", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.applyHeadRotation(netHeadYaw, headPitch, ageInTicks);

		this.animateWalk(ModAnimationsDefinitions.STEVE_WALKING, limbSwing, limbSwingAmount, 4f, 2.5f);
		this.animate(((SteveEntity) entity).idleAnimationState, ModAnimationsDefinitions.STEVE_IDLE, ageInTicks, 1f);
	}

	private void applyHeadRotation(float pNetHeadYaw, float pHeadPitch, float pAgeInTicks) {
		pNetHeadYaw = Mth.clamp(pNetHeadYaw, -30.0F, 30.0F);
		pHeadPitch = Mth.clamp(pHeadPitch, -25.0F, 45.0F);

		this.Head.yRot = pNetHeadYaw * ((float)Math.PI / 180F);
		this.Head.xRot = pHeadPitch * ((float)Math.PI / 180F);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		Steve.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart root() {
		return Steve;
	}
}