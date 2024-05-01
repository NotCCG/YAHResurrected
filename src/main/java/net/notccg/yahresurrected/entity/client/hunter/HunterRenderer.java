package net.notccg.yahresurrected.entity.client.hunter;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.notccg.yahresurrected.YouAreHerobrineResurrected;
import net.notccg.yahresurrected.entity.client.ModModelLayers;
import net.notccg.yahresurrected.entity.custom.HunterEntity;

public class HunterRenderer extends MobRenderer<HunterEntity, HunterModel<HunterEntity>> {
    public HunterRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new HunterModel<>(pContext.bakeLayer(ModModelLayers.HUNTER_LAYER_ONE)), 0.75f);
    }

    @Override
    public ResourceLocation getTextureLocation(HunterEntity hunterEntity) {
        return new ResourceLocation(YouAreHerobrineResurrected.MOD_ID, "textures/entity/hunter_entity.png");
    }

    @Override
    public void render(HunterEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }
}
