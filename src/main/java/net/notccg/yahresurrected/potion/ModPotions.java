package net.notccg.yahresurrected.potion;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.notccg.yahresurrected.YouAreHerobrineResurrected;

public class ModPotions {
    public static final DeferredRegister<Potion> POTIONS =
            DeferredRegister.create(ForgeRegistries.POTIONS, YouAreHerobrineResurrected.MOD_ID);

    public static final RegistryObject<Potion> DILUTED_ENCHANTING = POTIONS.register("diluted_xp_potion",
            () -> new Potion(new MobEffectInstance(MobEffects.GLOWING, 720)));
    public static final RegistryObject<Potion> DEMONIC_POTION = POTIONS.register("demonic_potion",
            () -> new Potion(
                    new MobEffectInstance(MobEffects.ABSORPTION, 720, 3),
                    new MobEffectInstance(MobEffects.DAMAGE_BOOST, 720, 3),
                    new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 720, 3),
                    new MobEffectInstance(MobEffects.REGENERATION, 900, 3),
                    new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 900, 3),
                    new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 900, 3)
            )
    );

    public static void register(IEventBus eventBus) {
        POTIONS.register(eventBus);
    }
}
