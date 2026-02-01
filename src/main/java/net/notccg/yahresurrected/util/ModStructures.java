package net.notccg.yahresurrected.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraftforge.registries.DeferredRegister;
import net.notccg.yahresurrected.YouAreHerobrineResurrected;

public class ModStructures {
    public static ResourceKey<Structure> SHRINE = structure("shrine");

    public static ResourceKey<Structure> STEVE_HOUSE_PLAINS = structure("steve_house_plains");
    public static ResourceKey<Structure> STEVE_HOUSE_SPRUCE = structure("steve_house_spruce");

    private static ResourceKey<Structure> structure(String name) {
        return  ResourceKey.create(Registries.STRUCTURE, new ResourceLocation(YouAreHerobrineResurrected.MOD_ID, name));
    }

}
