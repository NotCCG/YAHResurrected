package net.notccg.yahresurrected.entity.client.steve;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.notccg.yahresurrected.YouAreHerobrineResurrected;
import net.notccg.yahresurrected.entity.client.ModModelLayers;
import net.notccg.yahresurrected.entity.custom.AbstractSteve;
import net.notccg.yahresurrected.entity.custom.Steve;

public class SteveRenderer extends AbstractSteveRenderer<Steve, SteveModel<Steve>> {

    public SteveRenderer(EntityRendererProvider.Context pContext) {
        this(pContext, ModModelLayers.STEVE_MAIN, ModModelLayers.STEVE_INNER, ModModelLayers.STEVE_OUTER);
    }

    public SteveRenderer(EntityRendererProvider.Context pContext, ModelLayerLocation steveMain, ModelLayerLocation steveInner, ModelLayerLocation steveOuter) {
        super(pContext, new SteveModel<>(pContext.bakeLayer(steveMain)), new SteveModel<>(pContext.bakeLayer(steveInner)), new SteveModel<>(pContext.bakeLayer(steveOuter)));
    }

}
