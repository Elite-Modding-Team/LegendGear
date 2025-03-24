package mod.emt.legendgear.item.base;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;
import mod.emt.legendgear.init.LGItems;

public class LGItemFood extends ItemFood
{
    public int itemUseDuration;

    public LGItemFood(int amount)
    {
        super(amount, 0.6F, false);
    }

    public LGItemFood(int amount, float saturation, boolean isWolfFood)
    {
        super(amount, saturation, isWolfFood);
    }

    public LGItemFood(int amount, float saturation, boolean isWolfFood, int eatingSpeed)
    {
        super(amount, saturation, isWolfFood);
        itemUseDuration = eatingSpeed; // 32 by default
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flags)
    {
        if (stack.getItem() == LGItems.MILK_CHOCOLATE)
        {
            tooltip.add(TextFormatting.GRAY + I18n.format("tooltip.legendgear.milk_chocolate"));
        }
    }

    @Override
    protected void onFoodEaten(ItemStack stack, World world, EntityPlayer player)
    {
        // Milk Chocolate cures all possible effects
        if (!world.isRemote)
        {
            if (stack.getItem() == LGItems.MILK_CHOCOLATE)
            {
                player.curePotionEffects(new ItemStack(Items.MILK_BUCKET));
            }
        }

        super.onFoodEaten(stack, world, player);
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack)
    {
        if (itemUseDuration == 0)
        {
            return 32;
        }

        return itemUseDuration;
    }
}