package mod.emt.legendgear.item.base;

import mod.emt.legendgear.init.LGItems;
import net.minecraft.block.Block;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBlockSpecial;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack)
    {
        return this.equals(LGItems.INFUSED_STARSTONE);
    }
}
