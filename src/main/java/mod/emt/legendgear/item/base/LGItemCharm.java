package mod.emt.legendgear.item.base;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import mod.emt.legendgear.init.LGItems;
import mod.emt.legendgear.init.LGSoundEvents;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class LGItemCharm extends Item implements IBauble
{
    public LGItemCharm()
    {
        super();
        setMaxStackSize(1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack itemStack)
    {
        return true;
    }

    @Override
    public BaubleType getBaubleType(ItemStack itemStack)
    {
        return BaubleType.CHARM;
    }

    @Override
    public void onEquipped(ItemStack stack, EntityLivingBase player)
    {
        player.playSound(LGSoundEvents.ITEM_BAUBLE_EQUIP.getSoundEvent(), 1.0F, 1.0F);
    }

    @Override
    public void onUnequipped(ItemStack stack, EntityLivingBase player)
    {
        player.playSound(LGSoundEvents.ITEM_BAUBLE_UNEQUIP.getSoundEvent(), 1.0F, 1.0F);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flags)
    {
        if (this.equals(LGItems.BLAST_CHARM))
        {
            tooltip.add(TextFormatting.GRAY + I18n.format("tooltip.legendgear.blast_charm"));
        } else if (this.equals(LGItems.FEATHER_CHARM))
        {
            tooltip.add(TextFormatting.GRAY + I18n.format("tooltip.legendgear.feather_charm"));
        } else if (this.equals(LGItems.PHOENIX_CHARM))
        {
            tooltip.add(TextFormatting.GOLD + I18n.format("tooltip.legendgear.phoenix_charm"));
        }
    }
}
