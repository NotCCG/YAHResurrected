package net.notccg.yahresurrected.event;

import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.notccg.yahresurrected.YouAreHerobrineResurrected;
import net.notccg.yahresurrected.entity.ModEntities;
import net.notccg.yahresurrected.entity.custom.AbstractHunter;
import net.notccg.yahresurrected.entity.custom.AbstractSteve;

@Mod.EventBusSubscriber(modid = YouAreHerobrineResurrected.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.STEVE.get(), AbstractSteve.createAttribute().build());
        event.put(ModEntities.HUNTER.get(), AbstractHunter.createAttribute().build());
    }
}
