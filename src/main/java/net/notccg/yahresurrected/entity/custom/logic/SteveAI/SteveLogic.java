package net.notccg.yahresurrected.entity.custom.logic.SteveAI;

import net.minecraft.world.item.ItemStack;
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


    public static boolean isLovedItem(ItemStack pItemsTack) {
        return pItemsTack.is(ModTags.Items.STEVE_LOVED);
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
