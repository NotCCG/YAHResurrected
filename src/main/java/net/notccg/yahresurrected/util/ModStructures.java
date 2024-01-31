package net.notccg.yahresurrected.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraftforge.registries.DeferredRegister;
import net.notccg.yahresurrected.YouAreHerobrineResurrected;

public class ModStructures {
    public static final DeferredRegister<StructureType<?>> STRUCTURE_TYPE_DEFERRED_REGISTER = DeferredRegister.create(Registries.STRUCTURE_TYPE, YouAreHerobrineResurrected.MOD_ID);
}
