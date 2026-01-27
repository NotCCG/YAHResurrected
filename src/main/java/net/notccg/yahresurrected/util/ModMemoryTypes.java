package net.notccg.yahresurrected.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.notccg.yahresurrected.YouAreHerobrineResurrected;
import net.notccg.yahresurrected.entity.custom.logic.steve_ai.HeardSoundType;

import java.util.Optional;
import java.util.Set;

public class ModMemoryTypes {
    public static final DeferredRegister<MemoryModuleType<?>> MEMORY_MODULE_TYPES =
            DeferredRegister.create(ForgeRegistries.MEMORY_MODULE_TYPES, YouAreHerobrineResurrected.MOD_ID);

    public static final RegistryObject<MemoryModuleType<BlockPos>> INTERESTED_BLOCK_TARGET =
            MEMORY_MODULE_TYPES.register(
                    "interested_block_target",
                    () -> new MemoryModuleType<>(Optional.empty())
            );

    public static final RegistryObject<MemoryModuleType<Set<BlockPos>>> VISITED_BLOCKS =
            MEMORY_MODULE_TYPES.register(
                    "visited_blocks",
                    () -> new MemoryModuleType<>(Optional.empty())
            );

    public static final RegistryObject<MemoryModuleType<Player>> SPOTTED_PLAYER =
            MEMORY_MODULE_TYPES.register(
                    "spotted_player",
                    () -> new MemoryModuleType<>(Optional.empty())
            );

    public static final RegistryObject<MemoryModuleType<Double>> FEAR_LEVEL =
            MEMORY_MODULE_TYPES.register(
                    "fear_level",
                    () -> new MemoryModuleType<>(Optional.empty())
            );

    public static final RegistryObject<MemoryModuleType<Long>> EMOTION_UPDATED =
            MEMORY_MODULE_TYPES.register(
                    "emotion_updated",
                    () -> new MemoryModuleType<>(Optional.empty())
            );

    public static final RegistryObject<MemoryModuleType<Double>> CURIOSITY_LEVEL =
            MEMORY_MODULE_TYPES.register(
                    "curiosity_level",
                    () -> new MemoryModuleType<>(Optional.empty())
            );

    public static final RegistryObject<MemoryModuleType<Double>> PARANOIA_LEVEL =
            MEMORY_MODULE_TYPES.register(
                    "paranoia_level",
                    () -> new MemoryModuleType<>(Optional.empty())
            );

    public static final RegistryObject<MemoryModuleType<Player>> LAST_HURT_BY =
            MEMORY_MODULE_TYPES.register(
                    "last_hurt_by",
                    () -> new MemoryModuleType<>(Optional.empty())
            );

    public static final RegistryObject<MemoryModuleType<Boolean>> PLAYER_HURT =
            MEMORY_MODULE_TYPES.register(
                    "last_hurt",
                    () -> new MemoryModuleType<>(Optional.empty())
            );

    public static final RegistryObject<MemoryModuleType<Long>> HESITATION_COOLDOWN =
            MEMORY_MODULE_TYPES.register("hesitation_cooldown",
                    () -> new MemoryModuleType<>(Optional.empty())
            );

    public static final RegistryObject<MemoryModuleType<Player>> HEARD_SOUND =
            MEMORY_MODULE_TYPES.register("heard_sound",
                    () -> new MemoryModuleType<>(Optional.empty())
            );

    public static final RegistryObject<MemoryModuleType<Vec3>> HEARD_SOUND_POS =
            MEMORY_MODULE_TYPES.register("heard_sound_pos",
                    () -> new MemoryModuleType<>(Optional.empty())
            );

    public static final RegistryObject<MemoryModuleType<Long>> LAST_HEARD_TIME =
            MEMORY_MODULE_TYPES.register("last_heard_time",
                    () -> new MemoryModuleType<>(Optional.empty())
            );

    public static final RegistryObject<MemoryModuleType<BlockPos>> INVESTIGATE_TARGET =
            MEMORY_MODULE_TYPES.register("investigate_target",
                    () -> new MemoryModuleType<>(Optional.empty())
            );

    public static final RegistryObject<MemoryModuleType<BlockPos>> INTERESTED_ITEM_LOCATION =
            MEMORY_MODULE_TYPES.register("interested_items_loc",
                    () -> new MemoryModuleType<>(Optional.empty())
            );

    public static final RegistryObject<MemoryModuleType<BlockPos>> NEAREST_UNOCCUPIED_BED =
            MEMORY_MODULE_TYPES.register("nearest_unoccupied_bed",
                    () -> new MemoryModuleType<>(Optional.empty())
            );

    public static final RegistryObject<MemoryModuleType<HeardSoundType>> HEARD_SOUND_TYPE =
            MEMORY_MODULE_TYPES.register("heard_sound_type",
                    () -> new MemoryModuleType<>(Optional.empty())
            );

    public static final RegistryObject<MemoryModuleType<BlockPos>> CONTAINER_BLOCK =
            MEMORY_MODULE_TYPES.register("container_block",
                    () -> new MemoryModuleType<>(Optional.empty())
            );
}