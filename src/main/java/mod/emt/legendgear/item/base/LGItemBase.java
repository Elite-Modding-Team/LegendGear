package mod.emt.legendgear.item.base;

import javax.annotation.Nullable;

import mod.emt.legendgear.init.LGItems;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;
import mod.emt.legendgear.util.TooltipHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LGItemBase extends Item
{
    EnumRarity rarity;
    String tooltipLangKey;

    public LGItemBase(EnumRarity rarity, String tooltipLangKey)
    {
        super();
        this.rarity = rarity;
        this.tooltipLangKey = tooltipLangKey;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flags)
    {
        if (this.tooltipLangKey.isEmpty()) return;
        TooltipHelper.addWrappedTooltip(tooltip, TextFormatting.GRAY, I18n.format(this.tooltipLangKey));
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
        return this.equals(LGItems.NULL_MEDALLION);
    }
}
