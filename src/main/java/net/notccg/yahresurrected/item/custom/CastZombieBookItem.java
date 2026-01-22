package net.notccg.yahresurrected.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CastZombieBookItem extends Item {
    public CastZombieBookItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("tooltip.youareherobrineresurrected.cast_zombie_book.tooltip"));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    public static List<BlockPos> getRandomSpawnPosition(ServerLevel pLevel, BlockPos pOrigin, int pRadius) {
        RandomSource random = pLevel.getRandom();
        List<BlockPos> results =new ArrayList<>();

        int count = 1 + random.nextInt(3);

        for (int i = 0; i < count; i++) {
            double angle = random.nextDouble() * Math.PI * 2;
            double distance = random.nextDouble() * pRadius;

            int dx = (int) Math.round(Math.cos(angle) * distance);
            int dz = (int) Math.round(Math.sin(angle) * distance);

            BlockPos candidate = pOrigin.offset(dx, 0, dz);
            BlockPos surfacePos = pLevel.getHeightmapPos(
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    candidate
            );
            if (pLevel.getBlockState(surfacePos.below()).isSolidRender(pLevel, surfacePos.below()) &&
                    pLevel.isEmptyBlock(surfacePos) &&
                    pLevel.isEmptyBlock(surfacePos.above())) {
                results.add(surfacePos);
            }
        }
        return results;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        BlockPos playerPos = pPlayer.getOnPos();
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
        pPlayer.getCooldowns().addCooldown(this, 600);

        if (!pLevel.isClientSide) {
            ServerLevel serverLevel = (ServerLevel) pLevel;

            serverLevel.sendParticles(
                    ParticleTypes.FLAME,
                    pPlayer.getX(), pPlayer.getY() + 1.0D, pPlayer.getZ(),
                    40,
                    0.35D, 0.5D, 0.35D,
                    0.02D
            );

            List<BlockPos> spawnPositions = getRandomSpawnPosition(serverLevel, playerPos, 6);

            for (BlockPos pos : spawnPositions) {
                Zombie mob = EntityType.ZOMBIE.create(serverLevel);
                if (mob == null) continue;

                mob.moveTo(
                        pos.getX() + 0.5D,
                        pos.getY(),
                        pos.getZ() + 0.5D,
                        serverLevel.getRandom().nextFloat() * 360.0F,
                        0.0F
                );

                mob.finalizeSpawn(
                        serverLevel,
                        serverLevel.getCurrentDifficultyAt(pos),
                        MobSpawnType.TRIGGERED,
                        null,
                        null
                );

                serverLevel.sendParticles(
                        ParticleTypes.SOUL_FIRE_FLAME,
                        pos.getX() + 0.5D,
                        pos.getY() + 0.1D,
                        pos.getZ() + 0.5D,
                        25,
                        0.25D, 0.35D, 0.25D,
                        0.02D
                );

                serverLevel.sendParticles(
                        ParticleTypes.SMOKE,
                        pos.getX() + 0.5D,
                        pos.getY() + 0.1D,
                        pos.getZ() + 0.5D,
                        12,
                        0.25D, 0.25D, 0.25D,
                        0.01D
                );

                serverLevel.addFreshEntity(mob);
            }
        }
        return InteractionResultHolder.sidedSuccess(stack, pLevel.isClientSide);
    }
}
