package mod.emt.legendgear.item;

import javax.annotation.Nullable;

import net.minecraft.block.BlockDispenser;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;
import mod.emt.legendgear.entity.LGEntityBomb;

public class LGItemBomb extends Item implements IBehaviorDispenseItem
{
    public LGItemBomb()
    {
        super();
        setMaxStackSize(16);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
    {
        ItemStack itemstack = player.getHeldItem(hand);
        if (!player.capabilities.isCreativeMode) itemstack.shrink(1);
        world.playSound(player, player.getPosition(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
        if (!world.isRemote)
        {
            LGEntityBomb eb = new LGEntityBomb(world, player, 80);
            world.spawnEntity(eb);
        }
        player.swingArm(hand);
        return new ActionResult<>(EnumActionResult.SUCCESS, itemstack);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flags)
    {
        tooltip.add(TextFormatting.GRAY + I18n.format("tooltip.legendgear.bomb_item"));
    }

    @Override
    public ItemStack dispense(IBlockSource source, ItemStack stack)
    {
        World world = source.getWorld();
        IPosition position = BlockDispenser.getDispensePosition(source);
        EnumFacing facing = source.getBlockState().getValue(BlockDispenser.FACING);
        LGEntityBomb eb = new LGEntityBomb(world, position.getX(), position.getY(), position.getZ(), 80);
        eb.setThrowableHeading(facing.getXOffset(), 0.1D, facing.getZOffset(), 0.2F, 0.2F);
        world.spawnEntity(eb);
        stack.splitStack(1);
        return stack;
    }
}
