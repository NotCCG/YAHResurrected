package net.notccg.yahresurrected.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.notccg.yahresurrected.YouAreHerobrineResurrected;


public class ModTags {

    public static class Blocks {
        public static final TagKey<Block> NEEDS_EVIL_DIAMOND_TOOL = tag("needs_evil_diamond_tool");
        public static final TagKey<Block> NEEDS_BEDROCK_TOOL = tag("needs_bedrock_tool");
        public static final TagKey<Block> CONTAINER_BLOCK = tag("container_block");
        public static final TagKey<Block> NECESSARY_BLOCKS = tag("blocks_needed_to_beat_game");
        public static final TagKey<Block> ILLEGAL_BLOCK_ITEMS = tag("illegal_block_items");

        public static final TagKey<Block> STRANGE_BLOCKS = tag("strange_blocks");
        public static final TagKey<Block> HEROBRINE_TELLTALE_SIGNS = tag("herobrine_telltale_signs");

        private static TagKey<Block> tag(String name) {
            return BlockTags.create(new ResourceLocation(YouAreHerobrineResurrected.MOD_ID, name));
        }
    }


    public static class Items {
        public static final TagKey<Item> STEVE_LOVED = tag("steve_loved");

        public static final TagKey<Item> HEARTS = tag("hearts_from_villager_like");

        public static final TagKey<Item> SPELL_BOOKS = tag("spell_books");

        private static TagKey<Item> tag(String name) {
            return ItemTags.create(new ResourceLocation(YouAreHerobrineResurrected.MOD_ID, name));
        }
    }

    public static class Structures {
        public static final TagKey<Structure> STEVE_HOUSES = tag("steve_houses");

        private static TagKey<Structure> tag(String name) {
            return TagKey.create(
                    Registries.STRUCTURE,
                    new ResourceLocation(YouAreHerobrineResurrected.MOD_ID, name)
            );
        }
    }

    public static class Biomes {
        public static final TagKey<Biome> ICE_RUBY_BIOMES = tag("ice_ruby_biomes");

        public static TagKey<Biome> tag(String name) {
            return TagKey.create(
                    Registries.BIOME,
                    new ResourceLocation(YouAreHerobrineResurrected.MOD_ID, name)
            );
        }
    }

}
