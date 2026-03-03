package net.notccg.yahresurrected.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.notccg.yahresurrected.YouAreHerobrineResurrected;
import net.notccg.yahresurrected.entity.custom.Hunter;
import net.notccg.yahresurrected.entity.custom.Slayer;
import net.notccg.yahresurrected.entity.custom.Steve;
import net.notccg.yahresurrected.entity.custom.boss.JebEntity;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, YouAreHerobrineResurrected.MOD_ID);
    public static final RegistryObject<EntityType<Steve>> STEVE =
            ENTITY_TYPES.register("steve", () -> EntityType.Builder.of(Steve::new, MobCategory.CREATURE)
                    .sized(0.8f, 1.8f).build("steve"));

    public static final RegistryObject<EntityType<Hunter>> HUNTER =
            ENTITY_TYPES.register("hunter", () -> EntityType.Builder.of(Hunter::new, MobCategory.MONSTER)
                    .sized(0.8f, 1.8f).build("hunter"));

    public static final RegistryObject<EntityType<Slayer>> SLAYER =
            ENTITY_TYPES.register("slayer", () -> EntityType.Builder.of(Slayer::new, MobCategory.MONSTER)
                    .sized(0.8f, 1.8f).build("slayer"));

    public static final RegistryObject<EntityType<JebEntity>> JEB_ =
            ENTITY_TYPES.register("jeb", () -> EntityType.Builder.of(JebEntity::new, MobCategory.MONSTER)
                    .sized(0.8f, 1.8f).build("jeb"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
