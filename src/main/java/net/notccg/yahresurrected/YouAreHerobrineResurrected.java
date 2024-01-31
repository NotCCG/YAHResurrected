package net.notccg.yahresurrected;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.notccg.yahresurrected.block.ModBlocks;
import net.notccg.yahresurrected.item.ModCreativeModeTabs;
import net.notccg.yahresurrected.item.ModItems;
import net.notccg.yahresurrected.loot.ModLootModifiers;
import net.notccg.yahresurrected.util.ModStructures;

@Mod(YouAreHerobrineResurrected.MOD_ID)
public class YouAreHerobrineResurrected {
    public static final String MOD_ID = "youareherobrineresurrected";

    public YouAreHerobrineResurrected() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModCreativeModeTabs.register(modEventBus);

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);

        ModLootModifiers.register(modEventBus);
        ModStructures.STRUCTURE_TYPE_DEFERRED_REGISTER.register(modEventBus);
    }


    private void commonSetup(final FMLCommonSetupEvent event) {

    }
}



//You aren't a good person, you know that, right?
//Good people don't end up here
