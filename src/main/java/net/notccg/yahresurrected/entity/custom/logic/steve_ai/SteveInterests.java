package net.notccg.yahresurrected.entity.custom.logic.steve_ai;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.List;

public class SteveInterests {
    //A list of blocks that steve will wonder towards
    public static List<Block> INTERESTED_BLOCKS = ObjectArrayList.of(
            Blocks.ACACIA_SIGN,
            Blocks.ACACIA_WALL_SIGN,
            Blocks.BAMBOO_SIGN,
            Blocks.BAMBOO_WALL_SIGN,
            Blocks.BEDROCK,
            Blocks.BIRCH_SIGN,
            Blocks.BIRCH_WALL_SIGN,
            Blocks.CHERRY_SIGN,
            Blocks.CHERRY_WALL_SIGN,
            Blocks.CRIMSON_SIGN,
            Blocks.CRIMSON_WALL_SIGN,
            Blocks.DARK_OAK_SIGN,
            Blocks.DARK_OAK_WALL_SIGN,
            Blocks.DIAMOND_BLOCK,
            Blocks.IRON_DOOR,
            Blocks.JUNGLE_SIGN,
            Blocks.JUNGLE_WALL_SIGN,
            Blocks.MANGROVE_SIGN,
            Blocks.MANGROVE_WALL_SIGN,
            Blocks.NETHERITE_BLOCK,
            Blocks.OAK_SIGN,
            Blocks.OAK_WALL_SIGN,
            Blocks.REDSTONE_WIRE,
            Blocks.REDSTONE_TORCH,
            Blocks.SPRUCE_SIGN,
            Blocks.WARPED_SIGN,
            Blocks.WARPED_WALL_SIGN
    );

    //A list of items steve will navigate to pick up
    public static List<Item> INTERESTED_ITEMS = ObjectArrayList.of(
            Items.ANCIENT_DEBRIS.asItem(),
            Items.BEACON.asItem(),
            Items.DEEPSLATE_DIAMOND_ORE.asItem(),
            Items.DIAMOND_BLOCK.asItem(),
            Items.DIAMOND,
            Items.DIAMOND_AXE,
            Items.DIAMOND_BOOTS,
            Items.DIAMOND_CHESTPLATE,
            Items.DIAMOND_HELMET,
            Items.DIAMOND_HOE,
            Items.DIAMOND_HORSE_ARMOR,
            Items.DIAMOND_LEGGINGS,
            Items.DIAMOND_ORE.asItem(),
            Items.DIAMOND_PICKAXE,
            Items.DIAMOND_SHOVEL,
            Items.DIAMOND_SWORD,
            Items.NETHER_STAR,
            Items.NETHERITE_AXE,
            Items.NETHERITE_BOOTS,
            Items.NETHERITE_BLOCK.asItem(),
            Items.NETHERITE_CHESTPLATE,
            Items.NETHERITE_HELMET,
            Items.NETHERITE_HOE,
            Items.NETHERITE_INGOT,
            Items.NETHERITE_LEGGINGS,
            Items.NETHERITE_PICKAXE,
            Items.NETHERITE_SCRAP,
            Items.NETHERITE_SHOVEL,
            Items.NETHERITE_SWORD,
            Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE
            );
}
