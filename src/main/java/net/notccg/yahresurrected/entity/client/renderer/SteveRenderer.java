package net.notccg.yahresurrected.entity.client.renderer;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.notccg.yahresurrected.entity.client.ModModelLayers;
import net.notccg.yahresurrected.entity.client.models.SteveModel;
import net.notccg.yahresurrected.entity.custom.Steve;

public class SteveRenderer extends AbstractSteveRenderer<Steve, SteveModel<Steve>> {

    public SteveRenderer(EntityRendererProvider.Context pContext) {
        this(pContext, ModModelLayers.STEVE_MAIN, ModModelLayers.STEVE_INNER, ModModelLayers.STEVE_OUTER);
    }

    public SteveRenderer(EntityRendererProvider.Context pContext, ModelLayerLocation steveMain, ModelLayerLocation steveInner, ModelLayerLocation steveOuter) {
        super(pContext, new SteveModel<>(pContext.bakeLayer(steveMain)), new SteveModel<>(pContext.bakeLayer(steveInner)), new SteveModel<>(pContext.bakeLayer(steveOuter)));
    }

}
