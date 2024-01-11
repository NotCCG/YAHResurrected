package net.notccg.yahresurrected.item.custom;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class SpellBookTwoItem extends Item {
    public SpellBookTwoItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public boolean isFoil(ItemStack pStack) {
        return true;
    }
}
