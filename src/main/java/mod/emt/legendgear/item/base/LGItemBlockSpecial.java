package mod.emt.legendgear.item.base;

import net.minecraft.block.Block;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBlockSpecial;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import mod.emt.legendgear.init.LGItems;

public class LGItemBlockSpecial extends ItemBlockSpecial
{
    private final EnumRarity rarity;

    public LGItemBlockSpecial(Block block, EnumRarity rarity)
    {
        super(block);
        this.rarity = rarity;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack)
    {
        return this.equals(LGItems.INFUSED_STARSTONE);
    }

    @Override
    public EnumRarity getRarity(ItemStack stack)
    {
        return rarity;
    }
}
