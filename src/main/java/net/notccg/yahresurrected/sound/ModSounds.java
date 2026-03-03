package net.notccg.yahresurrected.sound;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.notccg.yahresurrected.YouAreHerobrineResurrected;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, YouAreHerobrineResurrected.MOD_ID);

    public static final RegistryObject<SoundEvent> JEB_DEFEATED = registerSoundEvents("jeb_defeated");

    private static RegistryObject<SoundEvent> registerSoundEvents(String name) {
        ResourceLocation resourceLocation = new ResourceLocation(YouAreHerobrineResurrected.MOD_ID, name);
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(resourceLocation));
    }

    public static void register(IEventBus iEventBus) {
        SOUND_EVENTS.register(iEventBus);
    }
}
