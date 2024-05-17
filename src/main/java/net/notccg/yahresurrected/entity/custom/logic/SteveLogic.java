package net.notccg.yahresurrected.entity.custom.logic;

import java.util.HashMap;
import java.util.Map;
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



    public static class FearState {

        public FearState(int fearLevel) {
            this.getFearLevel();
        }

        public Map<String, Boolean> getFearState() {
            Map<String, Boolean> fearState = new HashMap<>();
            fearState.put("isCalm", isCalm());
            fearState.put("isUneasy", isUneasy());
            return fearState;
        }

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
            return  (getFearLevel() <= 0.9);
        }

    }



}
