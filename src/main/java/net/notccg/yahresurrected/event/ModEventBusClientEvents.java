package net.notccg.yahresurrected.event;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.notccg.yahresurrected.YouAreHerobrineResurrected;
import net.notccg.yahresurrected.entity.client.ModModelLayers;
import net.notccg.yahresurrected.entity.client.hunter.HunterModel;
import net.notccg.yahresurrected.entity.client.steve.SteveModel;

@Mod.EventBusSubscriber(modid = YouAreHerobrineResurrected.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventBusClientEvents {
    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.STEVE_LAYER, SteveModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.HUNTER_MAIN, HunterModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.HUNTER_INNER, HunterModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.HUNTER_OUTER, HunterModel::createBodyLayer);
    }
}
