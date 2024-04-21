package net.notccg.yahresurrected.event;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.notccg.yahresurrected.YouAreHerobrineResurrected;
import net.notccg.yahresurrected.item.custom.SpellBookTwoItem;

@Mod.EventBusSubscriber(modid = YouAreHerobrineResurrected.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void addSunSensitivity(TickEvent.PlayerTickEvent event) {
        Player player = (Player) event.player;
        Level world = player.level();
        if(world.isDay() && !hasSpecificItem(player)) {
            BlockPos playerPos = player.blockPosition();

            if(world.getBlockState(playerPos.above()).isAir()) {
                player.setSecondsOnFire(5);
            }
        }
    }

    private static boolean hasSpecificItem(Player player){
        for (ItemStack stack : player.getInventory().items) {
            if (stack.getItem() instanceof SpellBookTwoItem) {
                return true;
            }
        }
        return false;
    }
}
