package net.notccg.yahresurrected.entity.custom.logic.SteveAI;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.List;

public class SteveInterests {
    //A list of blocks that steve will wonder towards
    public static List<Block> INTERESTED_BLOCKS = ObjectArrayList.of(
            Blocks.OAK_SIGN,
            Blocks.REDSTONE_WIRE,
            Blocks.REDSTONE_TORCH,
            Blocks.DIAMOND_BLOCK);

    //A list of items steve will navigate to pick up
    public static List<Item> INTERESTED_ITEMS = ObjectArrayList.of(
            Items.ANCIENT_DEBRIS.asItem(),
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
            Items.NETHERITE_AXE);
}
