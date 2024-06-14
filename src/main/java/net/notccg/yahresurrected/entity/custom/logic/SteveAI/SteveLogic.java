package net.notccg.yahresurrected.entity.custom.logic.SteveAI;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.notccg.yahresurrected.entity.custom.Steve;
import net.notccg.yahresurrected.util.ModTags;
import java.util.Random;

public class SteveLogic {

    private static final Random RANDOM = new Random();

    String[] STEVE_NAMES = {
            "Player",
            "Steve",
            "DanTDM"
    };

    public String getSteveName() {
        int index = RANDOM.nextInt(STEVE_NAMES.length);
        return STEVE_NAMES[index];
    }

    public static void putInInventory(Steve pSteve, ItemStack pStack) {
        pSteve.addToInventory(pStack);
    }

    public static void pickUpItem(Steve pSteve, ItemEntity pItemEntity) {
        stopWalking(pSteve);
        ItemStack itemStack = pItemEntity.getItem();
        if(isLovedItem(itemStack)) {
            pSteve.getBrain().eraseMemory(MemoryModuleType.TIME_TRYING_TO_REACH_ADMIRE_ITEM);
            holdInHand(pSteve, itemStack);
            startAdmiringWantedItem(pSteve);
        }
    }


    public static void holdInHand(Steve pSteve, ItemStack pStack) {
        if(isHoldingItemInHand(pSteve)) {
            pSteve.spawnAtLocation(pSteve.getItemInHand(InteractionHand.MAIN_HAND));
        }
        pSteve.holdInMainHand(pStack);
    }

    public static boolean isLovedItem(ItemStack pItemsTack) {
        return pItemsTack.is(ModTags.Items.STEVE_LOVED);
    }

    public static void startAdmiringWantedItem(LivingEntity pSteve) {
        pSteve.getBrain().setMemoryWithExpiry(MemoryModuleType.ADMIRING_ITEM, true, 120);
    }

    public static boolean wantsToPickup(Steve pSteve, ItemStack pStack) {
        boolean flag = pSteve.canAddToInventory(pStack);
        if(!isLovedItem(pStack)) {
            return pSteve.canReplaceCurrentItem(pStack);
        }
        return isNotHoldingLovedItem(pSteve) & flag;
    }

    public static boolean isHoldingItemInHand(Steve pSteve) {
        return !pSteve.getMainHandItem().isEmpty();
    }

    public static boolean isNotHoldingLovedItem(Steve pSteve) {
        return pSteve.getMainHandItem().isEmpty() || !isLovedItem(pSteve.getMainHandItem());
    }

    public static void stopWalking(Steve pSteve) {
        pSteve.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        pSteve.getNavigation().stop();
    }

    public static class FearState {

        private int fearLevel;

        public int getFearLevel() {
            return fearLevel;
        }

        public void increaseFear() {
        }

        public void decreaseFear() {
        }

        public boolean isCalm() {
            return fearLevel == 0;
        }
        public boolean isUneasy() {
            return  (getFearLevel() <= 1.0 && getFearLevel() > 0 );
        }
        public boolean isSpooked() {
            return (getFearLevel() <= 2.0 && getFearLevel() > 1.0);
        }

    }
}
