package net.notccg.yahresurrected.event;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.notccg.yahresurrected.YouAreHerobrineResurrected;
import net.notccg.yahresurrected.util.ModConfigCommon;

import java.util.Map;

@Mod.EventBusSubscriber(modid = YouAreHerobrineResurrected.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ForgeClientEvents {
    private static boolean reentrantGuard = false;

    @SubscribeEvent
    public static void onRenderPlayerPre(RenderPlayerEvent.Pre event) {
        if (!ModConfigCommon.OVERRIDE_SKIN_CONFIG.get()) return;
        if (reentrantGuard) return;

        AbstractClientPlayer player = (AbstractClientPlayer) event.getEntity();
        if (player.isSpectator()) return;
        event.setCanceled(true);

        EntityRenderDispatcher dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        Map<String, EntityRenderer<? extends Player>> skinMap = dispatcher.getSkinMap();

        EntityRenderer<? extends Player> renderer = skinMap.get("default");
        if (!(renderer instanceof PlayerRenderer defaultRenderer)) {
            return;
        }

        reentrantGuard = true;
        try {
            PoseStack poseStack = event.getPoseStack();
            MultiBufferSource buffer = event.getMultiBufferSource();
            int packedLight = event.getPackedLight();
            float partialTick = event.getPartialTick();
            float yaw = player.getYRot();

            defaultRenderer.render(
                    player,
                    yaw,
                    partialTick,
                    poseStack,
                    buffer,
                    packedLight
            );
        } finally {
            reentrantGuard = false;
        }
    }


}
