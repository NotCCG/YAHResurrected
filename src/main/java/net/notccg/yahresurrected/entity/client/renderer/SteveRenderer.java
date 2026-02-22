package net.notccg.yahresurrected.entity.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import net.notccg.yahresurrected.entity.custom.Steve;
import net.notccg.yahresurrected.entity.custom.logic.steve_ai.SteveLogic;

public class SteveRenderer extends HumanoidMobRenderer<Steve, PlayerModel<Steve>> {
    private final PlayerModel<Steve> normalModel;
    private final PlayerModel<Steve> slimModel;

    public SteveRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new PlayerModel<>(pContext.bakeLayer(ModelLayers.PLAYER), false), 0.5f);

        this.normalModel = new PlayerModel<>(pContext.bakeLayer(ModelLayers.PLAYER), false);
        this.slimModel = new PlayerModel<>(pContext.bakeLayer(ModelLayers.PLAYER_SLIM), true);

        this.addLayer(new HumanoidArmorLayer<>(
                this,
                new HumanoidModel<>(pContext.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)),
                new HumanoidModel<>(pContext.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR)),
                pContext.getModelManager()
        ));

        this.addLayer(new HumanoidArmorLayer<>(
                this,
                new HumanoidModel<>(pContext.bakeLayer(ModelLayers.PLAYER_SLIM_INNER_ARMOR)),
                new HumanoidModel<>(pContext.bakeLayer(ModelLayers.PLAYER_SLIM_OUTER_ARMOR)),
                pContext.getModelManager()
        ));
    }

    @Override
    public void render(Steve pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        boolean isSlim = pEntity.hasSlimArms();
        this.model = isSlim ? slimModel : normalModel;

        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(Steve steve) {
        ResourceLocation[] textureSet = steve.hasSlimArms() ? SteveLogic.STEVE_TEXTURES_SLIM : SteveLogic.STEVE_TEXTURES_NORMAL;

        int textureVariant = steve.getTextureVariant();
        if (textureVariant < 0 || textureVariant >= textureSet.length) {
            textureVariant = 0;
        }
        return textureSet[textureVariant];
    }
}
