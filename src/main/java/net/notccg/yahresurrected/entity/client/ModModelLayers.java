package net.notccg.yahresurrected.entity.client;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.notccg.yahresurrected.YouAreHerobrineResurrected;

public class ModModelLayers {
    // Hunter
    public static final ModelLayerLocation HUNTER_MAIN = new ModelLayerLocation(
            new ResourceLocation(YouAreHerobrineResurrected.MOD_ID, "hunter_main_layer"), "main");
    public static final ModelLayerLocation HUNTER_INNER = new ModelLayerLocation(
            new ResourceLocation(YouAreHerobrineResurrected.MOD_ID, "hunter_inner_layer"), "inner");
    public static final ModelLayerLocation HUNTER_OUTER = new ModelLayerLocation(
            new ResourceLocation(YouAreHerobrineResurrected.MOD_ID, "hunter_outer_layer"), "outer");

    // Slayer
    public static final ModelLayerLocation SLAYER_MAIN = new ModelLayerLocation(
            new ResourceLocation(YouAreHerobrineResurrected.MOD_ID, "slayer_main_layer"), "main");
    public static final ModelLayerLocation SLAYER_INNER = new ModelLayerLocation(
            new ResourceLocation(YouAreHerobrineResurrected.MOD_ID, "slayer_inner_layer"), "inner");
    public static final ModelLayerLocation SLAYER_OUTER = new ModelLayerLocation(
            new ResourceLocation(YouAreHerobrineResurrected.MOD_ID, "slayer_outer_layer"), "outer");

    // Jeb
    public static final ModelLayerLocation JEB_MAIN = new ModelLayerLocation(
            new ResourceLocation(YouAreHerobrineResurrected.MOD_ID, "jeb_main_layer"), "main");
    public static final ModelLayerLocation JEB_INNER = new ModelLayerLocation(
            new ResourceLocation(YouAreHerobrineResurrected.MOD_ID, "jeb_inner_layer"), "inner");
    public static final ModelLayerLocation JEB_OUTER = new ModelLayerLocation(
            new ResourceLocation(YouAreHerobrineResurrected.MOD_ID, "jeb_outer_layer"), "outer");
}
