package mod.emt.legendgear.item;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
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
import mod.emt.legendgear.init.LGSoundEvents;

public class LGItemEmeraldPiece extends Item
{
    public LGItemEmeraldPiece()
    {
        super();
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
    {
        ItemStack heldStack = player.getHeldItem(hand);
        if (heldStack.getItem() == this && heldStack.getCount() >= LGConfig.GENERAL_SETTINGS.emeraldExchangeRate)
        {
            heldStack.shrink(LGConfig.GENERAL_SETTINGS.emeraldExchangeRate);
            player.addItemStackToInventory(new ItemStack(Items.EMERALD));
            world.playSound(player.posX, player.posY, player.posZ, LGSoundEvents.ITEM_EMERALD_EXCHANGE.getSoundEvent(), SoundCategory.PLAYERS, 0.5F, 1F, false);
            return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
        }
        return super.onItemRightClick(world, player, hand);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flags)
    {
        if (stack.getCount() >= LGConfig.GENERAL_SETTINGS.emeraldExchangeRate)
        {
            tooltip.add(TextFormatting.GRAY + I18n.format("tooltip.legendgear.emerald_piece", stack.getCount() / LGConfig.GENERAL_SETTINGS.emeraldExchangeRate));
        }
    }
}
