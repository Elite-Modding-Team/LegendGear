package mod.emt.legendgear.item.base;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;
import mod.emt.legendgear.init.LGItems;
import mod.emt.legendgear.util.TooltipHelper;

public class LGItemSword extends ItemSword
{
    EnumRarity rarity;

    public LGItemSword(ToolMaterial material, EnumRarity rarity)
    {
        super(material);
        this.rarity = rarity;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flags)
    {
        if (this.equals(LGItems.STARGLASS_SWORD))
        {
            TooltipHelper.addWrappedTooltip(tooltip, TextFormatting.GRAY, I18n.format("tooltip.legendgear.starglass_sword"));
        }
    }

    @Override
    public EnumRarity getRarity(ItemStack stack)
    {
        return rarity;
    }
}
