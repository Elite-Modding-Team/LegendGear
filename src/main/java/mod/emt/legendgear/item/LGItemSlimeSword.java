package mod.emt.legendgear.item;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;
import mod.emt.legendgear.util.TooltipHelper;

public class LGItemSlimeSword extends Item
{
    public LGItemSlimeSword()
    {
        super();
        setMaxStackSize(1);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flags)
    {
        TooltipHelper.addWrappedTooltip(tooltip, TextFormatting.GRAY, I18n.format("tooltip.legendgear.slime_sword"));
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, EntityPlayer player)
    {
        if (!player.world.isRemote)
        {
            player.world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_SMALL_SLIME_HURT, SoundCategory.PLAYERS, 1.0F, 1.0F);
        }

        return false;
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
    {
        if (!player.world.isRemote)
        {
            player.world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_SMALL_SLIME_HURT, SoundCategory.PLAYERS, 1.0F, 1.0F);
        }

        return true;
    }

    @Override
    public boolean canDestroyBlockInCreative(World world, BlockPos pos, ItemStack stack, EntityPlayer player)
    {
        return false;
    }
}
