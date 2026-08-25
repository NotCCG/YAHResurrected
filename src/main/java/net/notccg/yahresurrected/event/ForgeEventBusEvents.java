package net.notccg.yahresurrected.event;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.notccg.yahresurrected.YouAreHerobrineResurrected;
import net.notccg.yahresurrected.item.ModItems;
import net.notccg.yahresurrected.multiblock.ShrineValidator;
import org.slf4j.Logger;

import java.util.Map;

@Mod.EventBusSubscriber(modid = YouAreHerobrineResurrected.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEventBusEvents {
    private static final Logger LOGGER = LogUtils.getLogger();

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        if (level.isClientSide()) return;
        BlockPos clickedPos = event.getPos();
        ItemStack held = event.getItemStack();
        boolean isFlintAndSteel = held.is(Items.FLINT_AND_STEEL);
        boolean isFireCharge = held.is(Items.FIRE_CHARGE);

        if (isFlintAndSteel || isFireCharge) {
            if (!level.getBlockState(clickedPos).is(Tags.Blocks.NETHERRACK)) return;

            BlockPos firePos = clickedPos.above();

            if (!level.getBlockState(firePos).canBeReplaced()) return;
            if (!ShrineValidator.isValidUnlit(level, clickedPos)) return;

            LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(level);
            if (bolt != null) {
                bolt.moveTo(clickedPos.getX() + 0.5, clickedPos.getY() + 1, clickedPos.getZ() + 0.5);
                bolt.setVisualOnly(true);
                level.addFreshEntity(bolt);
            }
        }
        if (ShrineValidator.isValidLit(level, clickedPos) && held.isEmpty()) {

        }
    }

    @SubscribeEvent
    public static void onAnvilUpdate(AnvilUpdateEvent event) {
        ItemStack leftItem = event.getLeft();
        ItemStack rightItem = event.getRight();

        if (leftItem.is(ModItems.SPELLBOOKVIII.get()) && rightItem.is(Items.ENCHANTED_BOOK)) {
            Map<Enchantment, Integer> bookEnchantments = EnchantmentHelper.getEnchantments(rightItem);
            Enchantment targetEnchant = Enchantments.SILK_TOUCH;

            if (bookEnchantments.containsKey(targetEnchant)) {
                int levelInBook = bookEnchantments.get(targetEnchant);

                ItemStack result = leftItem.copy();
                Map<Enchantment, Integer> itemEnchantments = EnchantmentHelper.getEnchantments(result);

                itemEnchantments.put(targetEnchant, levelInBook);

                EnchantmentHelper.setEnchantments(itemEnchantments, result);

                event.setOutput(result);
                event.setCost(5 * levelInBook);
                event.setMaterialCost(1);
            }
        }
    }
}
