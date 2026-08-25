package net.notccg.yahresurrected.util.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.notccg.yahresurrected.YouAreHerobrineResurrected;
import net.notccg.yahresurrected.util.config.ModConfigClent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayer.class)
public abstract class AbstractClientPlayerMixin {

    @Unique
    private static final ResourceLocation OVERRIDE_SKIN =
            new ResourceLocation(YouAreHerobrineResurrected.MOD_ID, "textures/entity/player/override.png");

    @Inject(method = "getSkinTextureLocation", at = @At("HEAD"), cancellable = true)
    private void yahr$overrideSkin(
            CallbackInfoReturnable<ResourceLocation> cir
    ) {
        AbstractClientPlayer player = (AbstractClientPlayer) (Object) this;

        if (ModConfigClent.OVERRIDE_SKIN_CONFIG.get() && player == Minecraft.getInstance().player) {
            cir.setReturnValue(OVERRIDE_SKIN);
        }
    }

    @Inject(method = "getModelName", at = @At("HEAD"), cancellable = true)
    private void yahr$overrideModel(
            CallbackInfoReturnable<String> cir
    ) {
        AbstractClientPlayer player = (AbstractClientPlayer) (Object) this;

        if (ModConfigClent.OVERRIDE_SKIN_CONFIG.get() && player == Minecraft.getInstance().player) {
            cir.setReturnValue("default");
        }
    }
}
