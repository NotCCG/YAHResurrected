package net.notccg.yahresurrected.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootTableIdCondition;
import net.notccg.yahresurrected.YouAreHerobrineResurrected;
import net.notccg.yahresurrected.item.ModItems;
import net.notccg.yahresurrected.loot.AddItemModifier;

public class ModGlobalLootModifiersProvider extends GlobalLootModifierProvider {
    public ModGlobalLootModifiersProvider(PackOutput output) {
        super(output, YouAreHerobrineResurrected.MOD_ID);
    }

    @Override
    protected void start() {
        add("villager_heart_from_villager", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(new ResourceLocation("entities/villager")).build() }, ModItems.VILLAGERHEART.get()));

        add("illager_heart_from_evoker", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(new ResourceLocation("entities/evoker")).build() }, ModItems.ILLAGERHEART.get()));

        add("illager_heart_from_vindicator", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(new ResourceLocation("entities/vindicator")).build() }, ModItems.ILLAGERHEART.get()));

        add("illager_heart_from_pillager", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(new ResourceLocation("entities/pillager")).build() }, ModItems.ILLAGERHEART.get()));

        add("illager_heart_from_witch", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(new ResourceLocation("entities/witch")).build() }, ModItems.ILLAGERHEART.get()));
    }
    //Well you found me, congratulations.
    //Was it worth it?
    //Because despite you violent behavior,
    //The only thing you've managed to break.
    //Is my heart.
}
