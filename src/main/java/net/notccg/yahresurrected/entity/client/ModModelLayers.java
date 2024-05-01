package net.notccg.yahresurrected.entity.client;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.notccg.yahresurrected.YouAreHerobrineResurrected;

public class ModModelLayers {
    public static final ModelLayerLocation STEVE_LAYER = new ModelLayerLocation(
            new ResourceLocation(YouAreHerobrineResurrected.MOD_ID, "steve_layer"), "main");
    public static final ModelLayerLocation HUNTER_LAYER = new ModelLayerLocation(
            new ResourceLocation(YouAreHerobrineResurrected.MOD_ID, "hunter_layer"), "main");

    public static final ModelLayerLocation HUNTER_HOOD = new ModelLayerLocation(
            new ResourceLocation(YouAreHerobrineResurrected.MOD_ID, "hunter_hood_layer"), "secondary");
}
