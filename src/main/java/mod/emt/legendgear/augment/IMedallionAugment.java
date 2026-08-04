package mod.emt.legendgear.augment;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public interface IMedallionAugment
{
    /**
     * Executes the augment's special ability.
     */
    void executeAbility(EntityPlayer player, EnumHand hand, ItemStack tool);

    /**
     * Number of ticks required before the augment ability is charged.
     */
    default int getChargeTime()
    {
        return 30;
    }

    /**
     * Number of ticks the player has to activate the augment ability
     * after reaching full charge.
     */
    default int getActivationWindow()
    {
        return 15;
    }

    /**
     * Amount of durability consumed when the augment ability succeeds.
     */
    default int getDurabilityCost()
    {
        return 1;
    }

    /**
     * Default charge rate of the augment.
     */
    default float getChargeRate()
    {
        return 1.0F;
    }
}