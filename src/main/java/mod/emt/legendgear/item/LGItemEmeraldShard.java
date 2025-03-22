package mod.emt.legendgear.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

import mod.emt.legendgear.init.LGItems;
import mod.emt.legendgear.init.LGSoundEvents;

public class LGItemEmeraldShard extends Item
{
    public LGItemEmeraldShard()
    {
        super();
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
    {
        ItemStack heldStack = player.getHeldItem(hand);
        if (heldStack.getItem() == this && heldStack.getCount() >= 8)
        {
            heldStack.shrink(8);
            player.addItemStackToInventory(new ItemStack(LGItems.EMERALD_PIECE));
            world.playSound(player.posX, player.posY, player.posZ, LGSoundEvents.ITEM_EMERALD_EXCHANGE.getSoundEvent(), SoundCategory.PLAYERS, 0.5F, 1F, false);
            return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
        }
        return super.onItemRightClick(world, player, hand);
    }
}
