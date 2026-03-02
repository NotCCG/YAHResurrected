package net.notccg.yahresurrected.entity.client.renderer;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.notccg.yahresurrected.YouAreHerobrineResurrected;
import net.notccg.yahresurrected.entity.custom.boss.JebEntity;

public class JebRenderer extends HumanoidMobRenderer<JebEntity, HumanoidModel<JebEntity>> {
    private static final ResourceLocation JEB_LOCATION =
            new ResourceLocation(YouAreHerobrineResurrected.MOD_ID, "textures/entity/jeb_entity.png");

    public JebRenderer(EntityRendererProvider.Context pContext, HumanoidModel<JebEntity> pModel, float pShadowRadius, float pScaleX, float pScaleY, float pScaleZ) {
        super(pContext, pModel, pShadowRadius, pScaleX, pScaleY, pScaleZ);
    }

    @Override
    public ResourceLocation getTextureLocation(JebEntity pEntity) {
        return JEB_LOCATION;
    }
}
