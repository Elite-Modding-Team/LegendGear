package mod.emt.legendgear.item.base;

import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class LGItemBase extends Item
{
    EnumRarity rarity;

    public LGItemBase(EnumRarity rarity)
    {
        super();
        this.rarity = rarity;
    }

    @Override
    public EnumRarity getRarity(ItemStack stack)
    {
        return rarity;
    }
}
