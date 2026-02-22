package net.notccg.yahresurrected.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.notccg.yahresurrected.YouAreHerobrineResurrected;

public class ModStructures {
    public static ResourceKey<Structure> SHRINE = structure("shrine");

    public static ResourceKey<Structure> STEVE_HOUSE_DESERT = structure("steve_house_desert");
    public static ResourceKey<Structure> STEVE_HOUSE_JUNGLE = structure("steve_house_jungle");
    public static ResourceKey<Structure> STEVE_HOUSE_MOUNTAINS = structure("steve_house_mountains");
    public static ResourceKey<Structure> STEVE_HOUSE_PLAINS = structure("steve_house_plains");
    public static ResourceKey<Structure> STEVE_HOUSE_RED_DESERT = structure("steve_house_red_desert");
    public static ResourceKey<Structure> STEVE_HOUSE_SPRUCE = structure("steve_house_spruce");
    public static ResourceKey<Structure> STEVE_HOUSE_COLD = structure("steve_house_cold");

    private static ResourceKey<Structure> structure(String name) {
        return  ResourceKey.create(Registries.STRUCTURE, new ResourceLocation(YouAreHerobrineResurrected.MOD_ID, name));
    }

}
