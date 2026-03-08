package net.notccg.yahresurrected.entity.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import net.notccg.yahresurrected.YouAreHerobrineResurrected;
import net.notccg.yahresurrected.entity.client.ModModelLayers;
import net.notccg.yahresurrected.entity.custom.boss.JebEntity;

public class JebRenderer extends HumanoidMobRenderer<JebEntity, PlayerModel<JebEntity>> {
    private static final ResourceLocation JEB_LOCATION =
            new ResourceLocation(YouAreHerobrineResurrected.MOD_ID, "textures/entity/jeb_entity.png");

    public JebRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new PlayerModel<>(pContext.bakeLayer(ModelLayers.PLAYER), false), 0.5f);
    }


    @Override
    public ResourceLocation getTextureLocation(JebEntity pEntity) {
        return JEB_LOCATION;
    }
}