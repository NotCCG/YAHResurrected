package net.notccg.yahresurrected.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.notccg.yahresurrected.YouAreHerobrineResurrected;
import net.notccg.yahresurrected.entity.custom.SteveEntity;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, YouAreHerobrineResurrected.MOD_ID);
    public static final RegistryObject<EntityType<SteveEntity>> STEVE =
            ENTITY_TYPES.register("steve", () -> EntityType.Builder.of(SteveEntity::new, MobCategory.CREATURE)
                    .sized(0.8f, 1.8f).build("steve"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
