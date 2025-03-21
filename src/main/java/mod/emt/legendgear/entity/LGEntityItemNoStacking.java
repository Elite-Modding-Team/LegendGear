package mod.emt.legendgear.entity;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LGEntityItemNoStacking extends EntityItem
{
    public LGEntityItemNoStacking(World world, double x, double y, double z)
    {
        super(world, x, y, z);
    }

    public LGEntityItemNoStacking(World world, double x, double y, double z, ItemStack stack)
    {
        super(world, x, y, z, stack);
    }

    public LGEntityItemNoStacking(World world)
    {
        super(world);
    }

    @Override
    protected void searchForOtherItemsNearby()
    {
        // NO OP
    }
}
