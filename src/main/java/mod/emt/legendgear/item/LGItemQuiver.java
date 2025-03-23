package mod.emt.legendgear.item;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

public class LGItemQuiver extends Item
{
    public LGItemQuiver()
    {
        super();
        setMaxStackSize(1);
        setMaxDamage(200);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
    {
        ItemStack quiverStack = player.getHeldItem(hand);
        if (player.isSneaking())
        {
            int damage = quiverStack.getItemDamage();
            if (damage < quiverStack.getMaxDamage())
            {
                if (!player.capabilities.isCreativeMode)
                {
                    quiverStack.setItemDamage(damage + 1);
                    quiverStack.setAnimationsToGo(5);
                }
                player.inventory.addItemStackToInventory(new ItemStack(Items.ARROW));
                player.playSound(SoundEvents.ENTITY_IRONGOLEM_ATTACK, 0.4F, 0.9F + (player.getRNG().nextFloat() / 2));
                player.playSound(SoundEvents.ITEM_FLINTANDSTEEL_USE, 0.4F, 0.1F + (player.getRNG().nextFloat() / 2));
                return new ActionResult<>(EnumActionResult.SUCCESS, quiverStack);
            }
        }
        else
        {
            if (quiverStack.getItemDamage() <= 0)
            {
                quiverStack.setItemDamage(0);
                return new ActionResult<>(EnumActionResult.PASS, quiverStack);
            }
            NonNullList<ItemStack> inv = player.inventory.mainInventory;
            for (ItemStack stack : inv)
            {
                if (stack.getItem() == Items.ARROW)
                {
                    quiverStack.setItemDamage(quiverStack.getItemDamage() - 1);
                    quiverStack.setAnimationsToGo(5);
                    if (!player.capabilities.isCreativeMode)
                    {
                        stack.setCount(stack.getCount() - 1);
                        stack.setAnimationsToGo(5);
                    }
                    player.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 0.2F, 0.4F);
                    return new ActionResult<>(EnumActionResult.SUCCESS, quiverStack);
                }
            }
        }
        return new ActionResult<>(EnumActionResult.PASS, quiverStack);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag)
    {
        tooltip.add(TextFormatting.GRAY + I18n.format("tooltip.legendgear.quiver"));
        tooltip.add(I18n.format(""));

        if (stack.getItemDamage() <= stack.getMaxDamage())
        {
            tooltip.add(I18n.format("tooltip.legendgear.arrows") + ": " + (stack.getMaxDamage() - stack.getItemDamage()) + " / " + stack.getMaxDamage());
        }
    }
}
