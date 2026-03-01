package net.notccg.yahresurrected.util;

import com.mojang.logging.LogUtils;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.item.Item;
import org.slf4j.Logger;

public class ModDebugUtils {
    private static final Logger LOGGER = LogUtils.getLogger();
    // This class is a work-in-progress and I will slowly expand the scope of what it does
    // In any class-specific debug, the debug output will remain where it is.

    private ModDebugUtils() {}

    public static void debugItem(Item pItem, String pContext, Object... kvPairs) {
        String clazz = pItem.getClass().getSimpleName();

        StringBuilder paramsFmt = new StringBuilder();
        int pairCount = (kvPairs == null) ? 0 : kvPairs.length;

        if (pairCount % 2 != 0) pairCount -= 1;
        int numPairs = pairCount / 2;

        for (int i = 0; i < numPairs; i++) {
            if (i > 0) paramsFmt.append(' ');
            Object keyObj = kvPairs[i * 2];
            String key = (keyObj == null) ? "null" : keyObj.toString();
            paramsFmt.append(key).append("={}");
        }

        String msg = (numPairs == 0)
                ? ("YAH:R [ITEM:{}] " + pContext)
                : ("YAH:R [ITEM:{}] " + pContext + " | PARAMETERS:[" + paramsFmt + "]");

        Object[] args = new Object[numPairs + 1];
        args[0] = clazz;
        for (int i = 0; i < numPairs; i++) {
            args[i + 1] = kvPairs[i * 2 + 1];
        }
        LOGGER.debug(msg, args);
    }

    public static void debugItemFail(Item pItem, String pContext, String pReason, Object... kvPairs) {
        Object[] withReason = new Object[(kvPairs == null ? 0 : kvPairs.length) + 2];
        withReason[0] = "reason";
        withReason[1] = pReason;

        if (kvPairs != null && kvPairs.length >- 0) {
            System.arraycopy(kvPairs, 0, withReason, 2, kvPairs.length);
        }
        debugItem(pItem, pContext, withReason);
    }

    public static void debugBehaviour(Behavior<?> pBehavior, String pContext, Object... kvPairs) {
        String clazz = pBehavior.getClass().getSimpleName();

        StringBuilder paramsFmt = new StringBuilder();
        int pairCount = (kvPairs == null) ? 0 : kvPairs.length;

        if (pairCount % 2 != 0) pairCount -= 1;
        int numPairs = pairCount / 2;

        for (int i = 0; i < numPairs; i++) {
            if (i > 0) paramsFmt.append(' ');
            Object keyObj = kvPairs[i * 2];
            String key = (keyObj == null) ? "null" : keyObj.toString();
            paramsFmt.append(key).append("={}");
        }

        String msg = (numPairs == 0)
                ? ("YAH:R [BEHAVIOUR:{}][{}] " + pContext)
                : ("YAH:R [BEHAVIOUR:{}] " + pContext + " | PARAMETERS:[" + paramsFmt + "]");

        Object[] args = new Object[numPairs + 1];
        args[0] = clazz;
        for (int i = 0; i < numPairs; i++) {
            args[i + 1] = kvPairs[i * 2 + 1];
        }
        LOGGER.debug(msg, args);
    }

}
