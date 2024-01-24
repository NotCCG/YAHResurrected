package net.notccg.yahresurrected.datagen.loot;

import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.RegistryObject;
import net.notccg.yahresurrected.block.ModBlocks;
import net.notccg.yahresurrected.item.ModItems;

import java.util.Set;

public class ModBlockLootTables extends BlockLootSubProvider {
    public ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        this.add(ModBlocks.INVISIBLE_ORE.get(),
                block -> createInvisibleOreDrops(ModBlocks.INVISIBLE_ORE.get(), ModItems.INVISIBLEDUST.get()));
        this.add(ModBlocks.DEEPSLATE_INVISIBLE_ORE.get(),
                block -> createInvisibleOreDrops(ModBlocks.DEEPSLATE_INVISIBLE_ORE.get(), ModItems.INVISIBLEDUST.get()));

        this.add(ModBlocks.ICE_RUBY_ORE.get(),
                block -> createIceRubyOreDrops(ModBlocks.ICE_RUBY_ORE.get(), ModItems.ICERUBY.get()));
        this.add(ModBlocks.DEEPSLATE_ICE_RUBY_ORE.get(),
                block -> createIceRubyOreDrops(ModBlocks.DEEPSLATE_ICE_RUBY_ORE.get(), ModItems.ICERUBY.get()));
    }

    protected LootTable.Builder createInvisibleOreDrops(Block pBlock, Item item) {
        return createSilkTouchDispatchTable(pBlock,
                this.applyExplosionDecay(pBlock,
                        LootItem.lootTableItem(item)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))
                                .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))));
    }

    protected LootTable.Builder createIceRubyOreDrops(Block pBlock, Item item) {
        return createSilkTouchDispatchTable(pBlock,
                this.applyExplosionDecay(pBlock,
                        LootItem.lootTableItem(item)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 1)))
                                .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
