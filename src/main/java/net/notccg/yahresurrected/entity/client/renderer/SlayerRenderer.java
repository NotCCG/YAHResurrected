package net.notccg.yahresurrected.entity.client.renderer;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.notccg.yahresurrected.YouAreHerobrineResurrected;
import net.notccg.yahresurrected.entity.client.ModModelLayers;
import net.notccg.yahresurrected.entity.custom.AbstractHunter;

public class SlayerRenderer extends HunterRenderer {
    private static final ResourceLocation SLAYER_LOCATION =
            new ResourceLocation(YouAreHerobrineResurrected.MOD_ID, "textures/entity/slayer_entity.png");

    public SlayerRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, ModModelLayers.SLAYER_MAIN, ModModelLayers.SLAYER_INNER, ModModelLayers.SLAYER_OUTER);
    }

    @Override
    public ResourceLocation getTextureLocation(AbstractHunter hunterEntity) {
        return SLAYER_LOCATION;
    }
}
