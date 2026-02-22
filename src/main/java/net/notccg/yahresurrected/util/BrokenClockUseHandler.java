package net.notccg.yahresurrected.util;

import net.minecraft.server.level.ServerLevel;

public class BrokenClockUseHandler {
    private static Long overWorldTarget = null;

    public static boolean isSkipping(ServerLevel level) {
        return overWorldTarget != null;
    }

    public static void startSKip(ServerLevel level) {
        overWorldTarget = nextNightTarget(level);
    }

    public static void tick(ServerLevel level) {
        if (overWorldTarget == null) return;

        long now = level.getDayTime();
        long target = overWorldTarget;

        if (now >= target) {
            level.setDayTime(target);
            overWorldTarget = null;
            return;
        }

        long remaining = target - now;

        long step = 100L;
        if (remaining < 1000L) step = Math.max(40L, remaining / 10L);

        level.setDayTime(now + Math.min(step, remaining));
    }
    private static long nextNightTarget(ServerLevel level) {
        long dayTime = level.getDayTime();
        long timeOfDay = dayTime % 24000L;
        long targetTime = 13000L;

        long delta = (timeOfDay <= targetTime) ?
                (targetTime - timeOfDay) :
                (24000L - timeOfDay + targetTime);

        return dayTime + delta;
    }

}
