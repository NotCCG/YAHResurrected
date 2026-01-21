package net.notccg.yahresurrected.event;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.notccg.yahresurrected.YouAreHerobrineResurrected;
import net.notccg.yahresurrected.entity.client.ModModelLayers;
import net.notccg.yahresurrected.entity.client.models.HunterModel;
import net.notccg.yahresurrected.entity.client.models.SteveModel;

@Mod.EventBusSubscriber(modid = YouAreHerobrineResurrected.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventBusClientEvents {
    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.STEVE_MAIN, SteveModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.STEVE_INNER, SteveModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.STEVE_OUTER, SteveModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.HUNTER_MAIN, HunterModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.HUNTER_INNER, HunterModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.HUNTER_OUTER, HunterModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.SLAYER_MAIN, HunterModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.SLAYER_INNER, HunterModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.SLAYER_OUTER, HunterModel::createBodyLayer);
    }
}
