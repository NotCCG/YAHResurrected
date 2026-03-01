package net.notccg.yahresurrected.entity.custom;

import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;

public class Hunter extends AbstractHunter {
    public Hunter(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource pRandom, DifficultyInstance pDifficulty) {
        super.populateDefaultEquipmentSlots(pRandom, pDifficulty);
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
        float difficultyModifier = (this.level().getDifficulty() == Difficulty.HARD ? 0.08F : 0.01F);

        int enchantmentTier = pickEnchantmentTier(pRandom);

        if ((pRandom.nextFloat() - difficultyModifier) < 0.25F) enchantWeapon(pRandom, enchantmentTier);
    }

    private int pickEnchantmentTier(RandomSource pRandom) {
        float difficultyModifier = (this.level().getDifficulty() == Difficulty.HARD ? 0.08F : 0.01F);

        float roll = pRandom.nextFloat() - difficultyModifier;

        if (roll < 0.05F) return 4;
        if (roll < 0.15F) return 3;
        if (roll < 0.35F) return 2;
        if (roll < 0.55F) return 1;
        return 0;
    }

    private void enchantWeapon(RandomSource pRandom, int pEnchantmentTier) {
        int difficultyModifier = (this.level().getDifficulty() == Difficulty.HARD ? 3 : 1);

        ItemStack stack = this.getItemBySlot(EquipmentSlot.MAINHAND);
        if (stack.isEmpty()) return;

        int tierEnchantLevel = switch (pEnchantmentTier) {
            case 4 -> 27 + difficultyModifier;
            case 3 -> 21 + difficultyModifier;
            case 2 -> 19 + difficultyModifier;
            case 1 -> 7 + difficultyModifier;
            default -> difficultyModifier;
        };

        ItemStack enchantedItemStack = EnchantmentHelper.enchantItem(pRandom, stack, tierEnchantLevel, false);
        this.setItemSlot(EquipmentSlot.MAINHAND, enchantedItemStack);
        this.setDropChance(EquipmentSlot.MAINHAND, 0.01F);
    }
}

