package mod.emt.legendgear.util;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LGCreativeTab extends CreativeTabs
{
    public LGCreativeTab(int length, String name)
    {
        super(length, name);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ItemStack createIcon()
    {
        return new ItemStack(Items.AIR, 1);
    }
}
