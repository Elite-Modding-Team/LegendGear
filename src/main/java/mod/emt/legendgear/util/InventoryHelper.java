package mod.emt.legendgear.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class InventoryHelper
{
    public static boolean deleteFirstMatchingItemStack(EntityPlayer player, Item item)
    {
        if (player.getHeldItemOffhand().getItem() == item) {
            player.getHeldItemOffhand().shrink(1);
            return true;
        }
        for (ItemStack stack : player.inventory.mainInventory)
        {
            if (stack.getItem() == item)
            {
                player.inventory.deleteStack(stack);
                return true;
            }
        }
        return false;
    }

    @Nullable
    public static ItemStack getDamagedItemStack(EntityPlayer player, Item item)
    {
        if (player.getHeldItemOffhand().getItem() == item && player.getHeldItemOffhand().getItemDamage() > 0)
        {
            return player.getHeldItemOffhand();
        }
        for (ItemStack is : player.inventory.mainInventory)
        {
            if (is.getItem() == item && is.getItemDamage() > 0)
            {
                return is;
            }
        }
        return null;
    }
}
