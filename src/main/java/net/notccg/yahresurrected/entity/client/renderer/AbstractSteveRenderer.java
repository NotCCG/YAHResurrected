package net.notccg.yahresurrected.entity.client.renderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import net.notccg.yahresurrected.YouAreHerobrineResurrected;
import net.notccg.yahresurrected.entity.client.models.SteveModel;
import net.notccg.yahresurrected.entity.custom.Steve;

public class AbstractSteveRenderer<T extends Steve, M extends SteveModel<T>> extends HumanoidMobRenderer<T, M> {

    private static final ResourceLocation[] STEVE_TEXTURES = new ResourceLocation[] {
            new ResourceLocation(YouAreHerobrineResurrected.MOD_ID, "textures/entity/steve_entity_0.png"),
            new ResourceLocation(YouAreHerobrineResurrected.MOD_ID, "textures/entity/steve_entity_1.png"),
            new ResourceLocation(YouAreHerobrineResurrected.MOD_ID, "textures/entity/steve_entity_2.png"),
            new ResourceLocation(YouAreHerobrineResurrected.MOD_ID, "textures/entity/steve_entity_3.png"),
            new ResourceLocation(YouAreHerobrineResurrected.MOD_ID, "textures/entity/steve_entity_4.png"),
            new ResourceLocation(YouAreHerobrineResurrected.MOD_ID, "textures/entity/steve_entity_5.png"),
            new ResourceLocation(YouAreHerobrineResurrected.MOD_ID, "textures/entity/steve_entity_6.png")

    };

    public AbstractSteveRenderer(EntityRendererProvider.Context pContext, M pModel, M pInnerModel, M pOuterModel) {
        super(pContext, pModel, 0.5F);
        this.addLayer(new HumanoidArmorLayer<>(this, pInnerModel, pOuterModel, pContext.getModelManager()));
    }

    @Override
    public ResourceLocation getTextureLocation(Steve steve) {
        int v = steve.getTextureVariant();
        if (v < 0 || v >= STEVE_TEXTURES.length) {
            v = 0;
        }
        return STEVE_TEXTURES[v];
    }
}
