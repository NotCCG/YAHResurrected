package net.notccg.yahresurrected.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.notccg.yahresurrected.YouAreHerobrineResurrected;

import java.util.Optional;

public class ModMemoryTypes {
    public static final DeferredRegister<MemoryModuleType<?>> MEMORY_MODULE_TYPES = DeferredRegister.create(ForgeRegistries.Keys.MEMORY_MODULE_TYPES, YouAreHerobrineResurrected.MOD_ID);

    public static final RegistryObject<MemoryModuleType<BlockPos>> TARGET_BLOCK = MEMORY_MODULE_TYPES.register("target_block", () -> new MemoryModuleType<>(Optional.of(BlockPos.CODEC)));
}
