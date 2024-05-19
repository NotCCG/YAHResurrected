package net.notccg.yahresurrected.util;

import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.notccg.yahresurrected.YouAreHerobrineResurrected;


public class ModTags {

    public static class Blocks {
        public static final TagKey<Block> NEEDS_EVIL_DIAMOND_TOOL = tag("needs_evil_diamond_tool");
        public static final TagKey<Block> NEEDS_BEDROCK_TOOL = tag("needs_bedrock_tool");

        private static TagKey<Block> tag(String name) {
            return BlockTags.create(new ResourceLocation(YouAreHerobrineResurrected.MOD_ID, name));
        }
    }



    public static class Items {
        public static final TagKey<Item> STEVE_LOVED = tag("steve_loved");

        private static TagKey<Item> tag(String name) {
            return ItemTags.create(new ResourceLocation(YouAreHerobrineResurrected.MOD_ID, name));
        }
    }

}
