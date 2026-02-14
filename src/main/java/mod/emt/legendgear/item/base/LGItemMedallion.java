package mod.emt.legendgear.item.base;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import java.util.List;

public abstract class LGItemMedallion extends Item
{

    public LGItemMedallion()
    {
        setMaxStackSize(1);
        setMaxDamage(20);
        hasSubtypes = false;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        if (playerIn.getHeldItem(handIn).getItemDamage() == 0)
        {
            playerIn.setActiveHand(handIn);
            return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
        }
        return new ActionResult<>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
    }

    @Override
    public boolean isDamageable()
    {
        return true;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.BOW;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack)
    {
        return 15;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag)
    {
        if (stack.getItemDamage() <= stack.getMaxDamage())
        {
            tooltip.add(I18n.format("tooltip.legendgear.charges") + ": " + (stack.getMaxDamage() - stack.getItemDamage()) + " / " + stack.getMaxDamage());
        }
    }

    @Override
    public boolean hasEffect(ItemStack stack)
    {
        return (stack.getItemDamage() == 0);
    }

    @Override
    public boolean isRepairable()
    {
        return false;
    }
}
