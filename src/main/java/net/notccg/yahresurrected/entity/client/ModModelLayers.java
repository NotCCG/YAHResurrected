package net.notccg.yahresurrected.entity.client;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.notccg.yahresurrected.YouAreHerobrineResurrected;

public class ModModelLayers {
    public static final ModelLayerLocation STEVE_MAIN = new ModelLayerLocation(
            new ResourceLocation(YouAreHerobrineResurrected.MOD_ID, "steve_main_layer"), "main");
    public static final ModelLayerLocation STEVE_INNER = new ModelLayerLocation(
            new ResourceLocation(YouAreHerobrineResurrected.MOD_ID, "steve_inner_layer"), "main");
    public static final ModelLayerLocation STEVE_OUTER = new ModelLayerLocation(
            new ResourceLocation(YouAreHerobrineResurrected.MOD_ID, "steve_outer_layer"), "main");
    public static final ModelLayerLocation HUNTER_MAIN = new ModelLayerLocation(
            new ResourceLocation(YouAreHerobrineResurrected.MOD_ID, "hunter_main_layer"), "main");
    public static final ModelLayerLocation HUNTER_INNER = new ModelLayerLocation(
            new ResourceLocation(YouAreHerobrineResurrected.MOD_ID, "hunter_inner_layer"), "inner");
    public static final ModelLayerLocation HUNTER_OUTER = new ModelLayerLocation(
            new ResourceLocation(YouAreHerobrineResurrected.MOD_ID, "hunter_outer_layer"), "outer");
}
