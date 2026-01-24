package net.notccg.yahresurrected.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class OverrideSkinLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    private static final ResourceLocation SKIN_OVERRIDE =
            new ResourceLocation("youareherobrineresurrected", "textures/entity/player_skin_override.png");

    private final PlayerModel<AbstractClientPlayer> steveModel;

    public OverrideSkinLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> pRenderer) {
        super(pRenderer);
        this.steveModel = new PlayerModel<>(
                Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.PLAYER),
                false
        );
    }

    @Override
    public void render(PoseStack poseStack,
                       MultiBufferSource buffer,
                       int packedLight,
                       AbstractClientPlayer player,
                       float limbSwing,
                       float limbSwingAmount,
                       float partialTick,
                       float ageInTicks,
                       float netHeadYaw,
                       float headPitch
    ) {
        if (player.isSpectator()) return;

        PlayerModel<AbstractClientPlayer> model = this.getParentModel();
        model.copyPropertiesTo(this.steveModel);

        this.steveModel.jacket.visible = false;
        this.steveModel.leftSleeve.visible = false;
        this.steveModel.rightSleeve.visible = false;
        this.steveModel.leftPants.visible = false;
        this.steveModel.rightPants.visible = false;
        this.steveModel.hat.visible = false;

        // Prevent any Z-Fighting
        poseStack.pushPose();
        poseStack.scale(1.001f, 1.001f, 1.001f);

        VertexConsumer vc = buffer.getBuffer(
                RenderType.armorCutoutNoCull(SKIN_OVERRIDE)
        );

        model.renderToBuffer(
                poseStack,
                vc,
                packedLight,
                OverlayTexture.NO_OVERLAY,
                1, 1, 1, 1
        );

        poseStack.popPose();
    }
}
