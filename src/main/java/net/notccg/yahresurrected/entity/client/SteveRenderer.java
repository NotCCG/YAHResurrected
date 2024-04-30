package net.notccg.yahresurrected.entity.client;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.notccg.yahresurrected.YouAreHerobrineResurrected;
import net.notccg.yahresurrected.entity.custom.SteveEntity;

public class SteveRenderer extends MobRenderer<SteveEntity, SteveModel<SteveEntity>> {
    public SteveRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new SteveModel<>(pContext.bakeLayer(ModModelLayers.STEVE_LAYER)), 1f);
    }

    @Override
    public ResourceLocation getTextureLocation(SteveEntity steveEntity) {
        return new ResourceLocation(YouAreHerobrineResurrected.MOD_ID, "textures/entity/steve_entity.png");
    }
}
