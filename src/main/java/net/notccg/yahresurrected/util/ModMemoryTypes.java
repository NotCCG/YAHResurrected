package net.notccg.yahresurrected.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.notccg.yahresurrected.YouAreHerobrineResurrected;

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
}