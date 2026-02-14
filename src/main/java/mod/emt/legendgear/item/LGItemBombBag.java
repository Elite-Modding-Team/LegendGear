package mod.emt.legendgear.item;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;
import mod.emt.legendgear.config.LGConfig;
import mod.emt.legendgear.entity.LGEntityBomb;
import mod.emt.legendgear.util.TooltipHelper;

public class LGItemBombBag extends Item
{
    public LGItemBombBag()
    {
        super();
        setMaxStackSize(1);
        setMaxDamage(LGConfig.GENERAL_SETTINGS.bombBagCapacity);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
    {
        ItemStack bombBagStack = player.getHeldItem(hand);
        if (bombBagStack.getItemDamage() >= bombBagStack.getMaxDamage())
        {
            bombBagStack.setItemDamage(bombBagStack.getMaxDamage());
            return new ActionResult<>(EnumActionResult.PASS, bombBagStack);
        }
        if (!player.capabilities.isCreativeMode) bombBagStack.setItemDamage(bombBagStack.getItemDamage() + 1);
        world.playSound(player, player.getPosition(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
        if (!world.isRemote)
        {
            LGEntityBomb eb = new LGEntityBomb(world, player, 80);
            world.spawnEntity(eb);
        }
        player.swingArm(hand);
        return new ActionResult<>(EnumActionResult.SUCCESS, bombBagStack);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag)
    {
        TooltipHelper.addWrappedTooltip(tooltip, TextFormatting.GRAY, I18n.format("tooltip.legendgear.bomb_bag"));
        tooltip.add(I18n.format(""));

        if (stack.getItemDamage() <= stack.getMaxDamage())
        {
            tooltip.add(I18n.format("tooltip.legendgear.bombs") + ": " + (stack.getMaxDamage() - stack.getItemDamage()) + " / " + stack.getMaxDamage());
        }
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
    {
        if (entity instanceof LGEntityBomb)
        {
            ((LGEntityBomb) entity).fizzle();
            return true;
        }
        return super.onLeftClickEntity(stack, player, entity);
    }
}
