package net.notccg.yahresurrected.entity.custom.logic.steve_ai;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ShulkerBoxMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.notccg.yahresurrected.YouAreHerobrineResurrected;
import net.notccg.yahresurrected.entity.custom.Steve;
import net.notccg.yahresurrected.util.ModMemoryTypes;
import net.notccg.yahresurrected.util.ModTags;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = YouAreHerobrineResurrected.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SteveSoundEvents {
    private static final double HEARING_RANGE = 14.0;

    private static final Map<UUID, BlockPos> PENDING_CHEST_POS = new HashMap<>();
    private static final Map<UUID, BlockPos> OPEN_CHEST_POS = new HashMap<>();

    @SubscribeEvent
    public static void onRightCLickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (event.getHand() != InteractionHand.MAIN_HAND) return;

        ServerLevel level = (ServerLevel) player.level();
        BlockPos pos = event.getPos();
        BlockState state = level.getBlockState(pos);

        if (state.is(ModTags.Blocks.CONTAINER_BLOCK)) {
            PENDING_CHEST_POS.put(player.getUUID(), pos.immutable());
        }
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if (!(event.getPlayer() instanceof ServerPlayer player)) return;

        ServerLevel level = (ServerLevel) player.level();

        notifyNearbySteves(level, player, Vec3.atCenterOf(event.getPos()), HeardSoundType.BLOCK_BREAK);
    }

    @SubscribeEvent
    public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        ServerLevel level = (ServerLevel) player.level();

        notifyNearbySteves(level, player, Vec3.atCenterOf(event.getPos()), HeardSoundType.BLOCK_PLACE);
    }

    @SubscribeEvent
    public static void onContainerOpen(PlayerContainerEvent.Open event) {
        if (!((event.getEntity()) instanceof ServerPlayer player)) return;
        if (!(event.getContainer() instanceof ChestMenu) || !(event.getContainer() instanceof ShulkerBoxMenu)) return;

        BlockPos chestPos = PENDING_CHEST_POS.remove(player.getUUID());
        if (chestPos == null) return;

        OPEN_CHEST_POS.put(player.getUUID(),chestPos);

        ServerLevel level = (ServerLevel) player.level();
        notifyNearbySteves(level, player, Vec3.atCenterOf(chestPos), HeardSoundType.CONTAINER_OPEN);
    }

    @SubscribeEvent
    public static void onContainerClose(PlayerContainerEvent.Close event) {
        if (!((event.getEntity()) instanceof ServerPlayer player)) return;
        if (!(event.getContainer() instanceof ChestMenu) || !(event.getContainer() instanceof ShulkerBoxMenu)) return;

        BlockPos chestPos = OPEN_CHEST_POS.remove(player.getUUID());
        if (chestPos == null) return;

        ServerLevel level = (ServerLevel) player.level();
        notifyNearbySteves(level, player, Vec3.atCenterOf(chestPos), HeardSoundType.CONTAINER_CLOSE);
    }

    private static void notifyNearbySteves(ServerLevel level, ServerPlayer player, Vec3 pos, HeardSoundType type) {
        double r = HEARING_RANGE;
        AABB box = new AABB(pos, pos).inflate(r);

        for (Steve steve : level.getEntitiesOfClass(Steve.class, box, s -> s.isAlive())) {
            var brain = steve.getBrain();
            long now = level.getGameTime();

            brain.setMemory(ModMemoryTypes.HEARD_SOUND.get(), player);
            brain.setMemory(ModMemoryTypes.HEARD_SOUND_POS.get(), pos);
            brain.setMemory(ModMemoryTypes.LAST_HEARD_TIME.get(), now);
            brain.setMemory(ModMemoryTypes.HEARD_SOUND_TYPE.get(), type);
            System.out.println("[DEBUG] Steve heard a sound " + type);
        }
    }
}