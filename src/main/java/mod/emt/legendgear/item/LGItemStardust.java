package mod.emt.legendgear.item;

import mod.emt.legendgear.init.LGItems;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LGItemStardust extends Item {
    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack)
    {
        return this.equals(LGItems.INFUSED_STARDUST);
    }

    @Override
    public EnumRarity getRarity(ItemStack stack)
    {
        return this.equals(LGItems.INFUSED_STARDUST) ? EnumRarity.EPIC : EnumRarity.RARE;
    }
}
