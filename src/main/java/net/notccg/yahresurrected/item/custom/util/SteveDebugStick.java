package net.notccg.yahresurrected.item.custom.util;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class SteveDebugStick extends Item {
    public SteveDebugStick(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public boolean isFoil(ItemStack pStack) {
        return true;
    }
}
