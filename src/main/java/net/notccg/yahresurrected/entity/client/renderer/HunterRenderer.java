package net.notccg.yahresurrected.entity.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import net.notccg.yahresurrected.YouAreHerobrineResurrected;
import net.notccg.yahresurrected.entity.client.ModModelLayers;
import net.notccg.yahresurrected.entity.client.models.HunterModel;
import net.notccg.yahresurrected.entity.custom.AbstractHunter;

public class HunterRenderer extends MobRenderer<AbstractHunter, HunterModel<AbstractHunter>> {

    public HunterRenderer(EntityRendererProvider.Context pContext) {
        this(pContext, ModModelLayers.HUNTER_MAIN, ModModelLayers.HUNTER_INNER, ModModelLayers.HUNTER_OUTER);
}

    public HunterRenderer(EntityRendererProvider.Context pContext, ModelLayerLocation hunterMain, ModelLayerLocation hunterInner, ModelLayerLocation hunterOuter) {
        super(pContext, new HunterModel<>(pContext.bakeLayer(hunterMain)), 0.5f);
        this.addLayer(new HumanoidArmorLayer<>(this, new HunterModel<>(pContext.bakeLayer(hunterInner)), new HunterModel<>(pContext.bakeLayer(hunterOuter)), pContext.getModelManager()));
        this.addLayer(new ItemInHandLayer<>(this, pContext.getItemInHandRenderer()));
    }

    @Override
    public ResourceLocation getTextureLocation(AbstractHunter hunterEntity) {
        return new ResourceLocation(YouAreHerobrineResurrected.MOD_ID, "textures/entity/hunter_entity.png");
    }

    @Override
    public void render(AbstractHunter pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }
}
