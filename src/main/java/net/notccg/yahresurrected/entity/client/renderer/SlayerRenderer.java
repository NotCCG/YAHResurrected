package net.notccg.yahresurrected.entity.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.notccg.yahresurrected.YouAreHerobrineResurrected;
import net.notccg.yahresurrected.entity.client.ModModelLayers;
import net.notccg.yahresurrected.entity.custom.AbstractHunter;

public class SlayerRenderer extends HunterRenderer{
    public SlayerRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, ModModelLayers.SLAYER_MAIN, ModModelLayers.SLAYER_INNER, ModModelLayers.SLAYER_OUTER);
    }

    public SlayerRenderer(EntityRendererProvider.Context pContext, ModelLayerLocation hunterMain, ModelLayerLocation hunterInner, ModelLayerLocation hunterOuter) {
        super(pContext, hunterMain, hunterInner, hunterOuter);
    }

    @Override
    public ResourceLocation getTextureLocation(AbstractHunter hunterEntity) {
        return new ResourceLocation(YouAreHerobrineResurrected.MOD_ID, "textures/entity/humanoid_test.png");
    }

    @Override
    public void render(AbstractHunter pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }
}
