package mod.emt.legendgear.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LGItemRecoveryHeart extends Item
{
    public LGItemRecoveryHeart()
    {
        super();
        setMaxStackSize(1);
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected)
    {
        super.onUpdate(stack, world, entity, itemSlot, isSelected);
        if (entity instanceof EntityPlayer)
        {
            ((EntityPlayer) entity).heal(stack.getCount() * 2);
            stack.shrink(stack.getCount());
        }
    }
}
