package net.notccg.yahresurrected.entity.client.renderer;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import net.notccg.yahresurrected.YouAreHerobrineResurrected;
import net.notccg.yahresurrected.entity.client.ModModelLayers;
import net.notccg.yahresurrected.entity.custom.boss.JebEntity;

public class JebRenderer extends HumanoidMobRenderer<JebEntity, HumanoidModel<JebEntity>> {
    private static final ResourceLocation JEB_LOCATION =
            new ResourceLocation(YouAreHerobrineResurrected.MOD_ID, "textures/entity/jeb_entity.png");

    public JebRenderer(EntityRendererProvider.Context pContext) {
        this(pContext, ModModelLayers.JEB_MAIN, ModModelLayers.JEB_INNER, ModModelLayers.JEB_OUTER);
    }

    public JebRenderer(EntityRendererProvider.Context pContext, ModelLayerLocation jebMain, ModelLayerLocation jebInner, ModelLayerLocation jebOuter) {
        super(pContext, new HumanoidModel<>(pContext.bakeLayer(jebMain)), 0.5F);
        this.addLayer(new HumanoidArmorLayer<>(this,
                new HumanoidModel<>(pContext.bakeLayer(jebInner)),
                new HumanoidModel<>(pContext.bakeLayer(jebOuter)),
                pContext.getModelManager()));
    }


    @Override
    public ResourceLocation getTextureLocation(JebEntity pEntity) {
        return JEB_LOCATION;
    }
}
