package mod.emt.legendgear.item.base;

import net.minecraft.block.Block;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBlockSpecial;
import net.minecraft.item.ItemStack;

public class LGItemBlockSpecial extends ItemBlockSpecial
{
    private final EnumRarity rarity;

    public LGItemBlockSpecial(Block block, EnumRarity rarity)
    {
        super(block);
        this.rarity = rarity;
    }

    @Override
    public EnumRarity getRarity(ItemStack stack)
    {
        return rarity;
    }
}
